package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.xposed.antforestx.core.ant.AntForestRpcCall
import org.xposed.antforestx.core.ant.AntOceanRpcCall
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.DataInfoManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.isSuccess
import org.xposed.antforestx.core.util.log.enableForest
import org.xposed.antforestx.core.util.log.ocean
import org.xposed.antforestx.core.util.onSuccessCatching
import org.xposed.antforestx.core.util.success
import timber.log.Timber

/**
 * 神奇海洋
 */
class AntOceanTask : ITask {

    private val logger get() = Timber.ocean()

    override suspend fun start() = withContext(Dispatchers.IO + CoroutineName("AntOcean")) {
        Timber.enableForest()
        logger.i("开始神奇海洋任务")
        if (!ConfigManager.forestConfig.enableProtectOcean) {
            logger.w("保护神奇海洋功能未启用")
            return@withContext
        }
        UserManager.waitGetCurrentUid()
        AntOceanRpcCall.queryOceanStatus().onSuccess { json ->
            if (!json.isSuccess()) {
                logger.d("查询神奇海洋状态失败: %s", json)
                return@onSuccess
            }
            if (json.optBoolean("opened")) {
                queryHomePage()
            } else {
                logger.i("神奇海洋未开启")
            }
        }

    }

    private suspend fun queryHomePage() {
        AntOceanRpcCall.queryHomePage().onSuccess { json ->
            if (!json.isSuccess()) {
                logger.d("查询首页失败: %s", json)
                return
            }
            delay(1000)
            collectEnergy(json.optJSONArray("bubbleVOList"))
            cleanOcean(json.optJSONObject("userInfoVO"))
            val ipVO = json.optJSONObject("ipVO")
            if (ipVO != null) {
                val surprisePieceNum = ipVO.optInt("surprisePieceNum")
                if (surprisePieceNum > 0) {
                    ipOpenSurprise()
                }
            }
            queryReplicaHome()
            delay(1000)
            queryMiscInfo()
            // 查询好友排名清理好友海洋
            queryUserRanking()
            // 日常任务
            delay(1000)
            doOceanDailyTask()
            delay(1000)
            receiveTaskAward()
        }.onFailure {
            logger.e(it, "查询首页异常")
        }
    }

    /**
     * 收取海洋能量
     */
    private suspend fun collectEnergy(bubbleVOList: JSONArray?) {
        if (bubbleVOList == null) {
            return
        }

        for (i in 0 until bubbleVOList.length()) {
            val bubbleVO = bubbleVOList.getJSONObject(i)
            if ("ocean" != bubbleVO.optString("channel")) {
                continue
            }
            if ("AVAILABLE" != bubbleVO.optString("collectStatus")) {
                continue
            }
            val id = bubbleVO.optLong("id")
            val userId = bubbleVO.optString("userId") ?: continue
            val bizType = bubbleVO.optString("bizType") ?: continue
            val userName = DataInfoManager.getFriendById(userId)
            AntForestRpcCall.collectEnergy(bizType, userId, id).onSuccess { collectJson ->
                if (collectJson.isSuccess()) {
                    val bubbles = collectJson.optJSONArray("bubbles") ?: return@onSuccess
                    for (j in 0 until bubbles.length()) {
                        val bubble = bubbles.getJSONObject(j)
                        val collectedEnergy = bubble.optInt("collectedEnergy")
                        logger.i("收取海洋能量[%s]的海洋能量[%sg]", userName, collectedEnergy)
                        UserManager.addNewEnergy(collectedEnergy)
                    }
                } else {
                    logger.d("收取海洋能量失败: %s", collectJson)
                }
            }.onFailure {
                logger.e(it, "收取海洋能量异常")
            }
            delay(1000)
        }
    }

    private suspend fun cleanOcean(userInfoVO: JSONObject?) {
        if (userInfoVO == null) {
            return
        }
        val userId = userInfoVO.optString("userId") ?: return
        logger.i("开始清理海洋")
        val rubbishNumber = userInfoVO.optInt("rubbishNumber")
        for (i in 0 until rubbishNumber) {
            AntOceanRpcCall.cleanOcean(userId).onSuccess {
                if (it.isSuccess()) {
                    logger.i("清理海洋成功")
                    checkReward(it.optJSONArray("cleanRewardVOS"))
                } else {
                    logger.d("清理海洋失败: %s", it)
                }
            }.onFailure {
                logger.e(it, "清理海洋异常")
            }
            delay(1000)
        }
    }

    /**
     * 检查奖励
     */
    private suspend fun checkReward(cleanRewardVOS: JSONArray?) {
        if (cleanRewardVOS == null) {
            return
        }
        for (i in 0 until cleanRewardVOS.length()) {
            val cleanRewardVO = cleanRewardVOS.getJSONObject(i)
            val attachRewardBOList = cleanRewardVO.optJSONArray("attachRewardBOList") ?: continue
            if (attachRewardBOList.length() > 0) {
                logger.i("获取碎片奖励")
                var canCombine = true
                for (j in 0 until attachRewardBOList.length()) {
                    val attachRewardBO = attachRewardBOList.getJSONObject(j)
                    if (attachRewardBO.optInt("count") == 0) {
                        canCombine = false
                        break
                    }
                }

                if (canCombine && cleanRewardVO.optBoolean("unlock")) {
                    val id = cleanRewardVO.optString("id") ?: continue
                    combineFish(id)
                }
            }
        }
    }

    /**
     * 迎回
     */
    private suspend fun combineFish(id: String) {
        AntOceanRpcCall.combineFish(id).onSuccess { json ->
            if (!json.isSuccess()) {
                logger.d("合成鱼失败: %s", json)
                return@onSuccess
            }
            val fishDetailVO = json.optJSONObject("fishDetailVO") ?: return@onSuccess
            val name = fishDetailVO.optString("name") ?: return@onSuccess
            logger.i("合成鱼[%s]成功", name)
        }.onFailure {
            logger.e(it, "合成鱼异常")
        }
    }

    private suspend fun ipOpenSurprise() {
        AntOceanRpcCall.ipOpenSurprise().onSuccess {
            if (!it.isSuccess()) {
                logger.d("打开Ip奖励失败: %s", it)
                return@onSuccess
            }
            checkReward(it.optJSONArray("surpriseRewardVOS"))
        }
    }

    private suspend fun queryReplicaHome() {
        AntOceanRpcCall.queryOceanStatus().onSuccess {
            if (it.isSuccess()) {
                if (!it.optBoolean("receivePieceFromFriend")) {
                    logger.i("receivePieceFromFriend == false")
                    return
                }
            }
        }
        delay(1000)
        AntOceanRpcCall.queryReplicaHome().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.d("查询副本首页失败: %s", it)
                return@onSuccessCatching
            }
            if (it.has("userReplicaAssetVO")) {
                val userReplicaAssetVO = it.getJSONObject("userReplicaAssetVO")
                val canCollectAssetNum = userReplicaAssetVO.optInt("canCollectAssetNum")
                collectReplicaAsset(canCollectAssetNum)
            }

            if (it.has("userCurrentPhaseVO")) {
                val userCurrentPhaseVO = it.getJSONObject("userCurrentPhaseVO")
                val phaseCode = userCurrentPhaseVO.getString("phaseCode")
                val code: String = it.getJSONObject("userReplicaInfoVO").getString("code")
                if ("COMPLETED" == userCurrentPhaseVO.getString("phaseStatus")) {
                    unLockReplicaPhase(code, phaseCode)
                }
            }

        }
        delay(1000)
    }

    private suspend fun collectReplicaAsset(canCollectAssetNum: Int) {
        delay(1000)
        for (i in 0 until canCollectAssetNum) {
            AntOceanRpcCall.collectReplicaAsset().onSuccess {
                if (!it.isSuccess()) {
                    logger.d("领取副本奖励失败: %s", it)
                    return@onSuccess
                }
                logger.i("[学习海洋科普知识]#潘多拉能量+1")
            }
        }
    }

    private suspend fun unLockReplicaPhase(replicaCode: String, replicaPhaseCode: String) {
        delay(1000)
        AntOceanRpcCall.unLockReplicaPhase(replicaCode, replicaPhaseCode).onSuccessCatching {
            if (!it.isSuccess()) {
                return@onSuccessCatching
            }
            val name = it.getJSONObject("currentPhaseInfo").getJSONObject("extInfo").getString("name")
            logger.i("解锁[%s]成功", name)
        }
    }

    private suspend fun queryMiscInfo() {
        AntOceanRpcCall.queryMiscInfo().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询用户信息失败: %s", it)
                return@onSuccessCatching
            }
            val miscHandlerVOMap = it.getJSONObject("miscHandlerVOMap")
            val homeTipsRefresh = miscHandlerVOMap.getJSONObject("HOME_TIPS_REFRESH")
            if (homeTipsRefresh.optBoolean("fishCanBeCombined") || homeTipsRefresh.optBoolean("canBeRepaired")) {
                querySeaAreaDetailList()
                delay(500)
            }
            queryOceanChapterList()
        }
    }

    /**
     * 查询海洋区域详情
     */
    private suspend fun querySeaAreaDetailList() {
        AntOceanRpcCall.querySeaAreaDetailList().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询海洋区域详情失败: %s", it)
                return@onSuccessCatching
            }
            val seaAreaNum = it.getInt("seaAreaNum")
            val fixSeaAreaNum = it.getInt("fixSeaAreaNum")
            val currentSeaAreaIndex = it.getInt("currentSeaAreaIndex")
            if (fixSeaAreaNum in (currentSeaAreaIndex + 1)..<seaAreaNum) {
                queryOceanPropList()
            }
            val seaAreaVOs = it.getJSONArray("seaAreaVOs")
            for (i in 0 until seaAreaVOs.length()) {
                val seaAreaVO = seaAreaVOs.getJSONObject(i)
                val fishVOs = seaAreaVO.getJSONArray("fishVO")
                for (j in 0 until fishVOs.length()) {
                    val fishVO = fishVOs.getJSONObject(j)
                    if (!fishVO.getBoolean("unlock") && "COMPLETED" == fishVO.getString("status")) {
                        val fishId = fishVO.getString("id")
                        combineFish(fishId)
                        delay(1000)
                    }
                }
            }
        }
    }

    private suspend fun queryOceanPropList() {
        AntOceanRpcCall.queryOceanPropList().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询海洋道具列表失败: %s", it)
                return@onSuccessCatching
            }
            AntOceanRpcCall.repairSeaArea()
        }

        delay(1000)
    }

    private suspend fun queryOceanChapterList() {
        AntOceanRpcCall.queryOceanChapterList().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询海洋章节列表失败: %s", it)
                return@onSuccessCatching
            }
            val currentChapterCode = it.getString("currentChapterCode")
            val chapterVOs = it.getJSONArray("userChapterDetailVOList")
            var isFinish = false
            var dstChapterCode = ""
            var dstChapterName = ""
            for (i in 0 until chapterVOs.length()) {
                val chapterVO = chapterVOs.getJSONObject(i)
                val repairedSeaAreaNum = chapterVO.getInt("repairedSeaAreaNum")
                val seaAreaNum = chapterVO.getInt("seaAreaNum")
                if (chapterVO.getString("chapterCode") == currentChapterCode) {
                    isFinish = repairedSeaAreaNum >= seaAreaNum
                } else {
                    if (repairedSeaAreaNum >= seaAreaNum || !chapterVO.getBoolean("chapterOpen")) {
                        continue
                    }
                    dstChapterName = chapterVO.getString("chapterName")
                    dstChapterCode = chapterVO.getString("chapterCode")
                }
            }
            if (isFinish && dstChapterCode.isNotEmpty()) {
                switchOceanChapter(dstChapterCode, dstChapterName)
            }
        }
        delay(1000)
    }

    private suspend fun switchOceanChapter(dstChapterCode: String, dstChapterName: String) {
        AntOceanRpcCall.switchOceanChapter(dstChapterCode).onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("切换海洋章节失败: %s", it)
                return@onSuccessCatching
            }
            logger.i("切换到[$dstChapterName]系列")
        }
        delay(600)
    }

    /**
     * 查询用户排名
     */
    private suspend fun queryUserRanking() {
        AntOceanRpcCall.queryUserRanking().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询用户排名失败: %s", it)
                return@onSuccessCatching
            }
            val fillFlagVOList = it.getJSONArray("fillFlagVOList")
            for (i in 0 until fillFlagVOList.length()) {
                val fillFlagVO = fillFlagVOList.getJSONObject(i)
                cleanFriendOcean(fillFlagVO)
            }
        }
        delay(1000)
    }

    /**
     * 清理好友海洋
     */
    private suspend fun cleanFriendOcean(fillFlagVO: JSONObject) {
        if (!fillFlagVO.optBoolean("canClean")) {
            return
        }

        val userId = fillFlagVO.getString("userId")
        if (ConfigManager.forestConfig.noCollectUserList.contains(userId)) {
            logger.i("跳过好友海洋清理 %s", userId)
        }
        AntOceanRpcCall.queryFriendPage(userId).onSuccess {
            if (!it.isSuccess()) {
                logger.e("查询好友信息失败: %s", it)
                return@onSuccess
            }
            AntOceanRpcCall.cleanFriendOcean(userId).onSuccessCatching { cleanJson ->
                if (!cleanJson.isSuccess()) {
                    logger.e("清理好友海洋失败: %s", cleanJson)
                    return@onSuccessCatching
                }
                checkReward(cleanJson.optJSONArray("cleanRewardVOS"))
            }
        }
        delay(1000)
    }

    /**
     * 做海洋日常任务
     */
    private suspend fun doOceanDailyTask() {
        logger.i("开始日常任务")
        AntOceanRpcCall.queryTaskList().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询海洋任务列表失败: %s", it)
                return@onSuccessCatching
            }
            val antOceanTaskVOList = it.getJSONArray("antOceanTaskVOList")
            for (i in 0 until antOceanTaskVOList.length()) {
                val antOceanTaskVO = antOceanTaskVOList.getJSONObject(i)
                val taskStatus = antOceanTaskVO.getString("taskStatus")
                if (taskStatus != "TODO") {
                    continue
                }
                val bizInfo = JSONObject(antOceanTaskVO.getString("bizInfo"))
                if (!bizInfo.has("taskType")) {
                    continue
                }
                val taskType = bizInfo.getString("taskType")
                if (bizInfo.optBoolean("autoCompleteTask", false) || taskType.startsWith("DAOLIU_")) {
                    val sceneCode = it.optString("sceneCode")
                    finishTask(sceneCode, taskType)
                }
                delay(1000)
            }
        }

    }

    private suspend fun finishTask(sceneCode: String, taskType: String) {
        AntOceanRpcCall.finishTask(sceneCode, taskType).onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("完成[$taskType]任务失败: %s", it)
                return@onSuccessCatching
            }
            val taskTitle = it.optString("taskTitle")
            logger.i("完成海洋任务: %s", taskTitle)
        }
    }

    private suspend fun receiveTaskAward() {
        logger.i("开始收取任务奖励")
        AntOceanRpcCall.queryTaskList().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询海洋任务列表失败: %s", it)
                return@onSuccessCatching
            }
            val antOceanTaskVOList = it.getJSONArray("antOceanTaskVOList")
            for (i in 0 until antOceanTaskVOList.length()) {
                val antOceanTaskVO = antOceanTaskVOList.getJSONObject(i)
                val taskStatus = antOceanTaskVO.getString("taskStatus")
                if (taskStatus != "FINISHED") {
                    continue
                }
                val bizInfo = JSONObject(antOceanTaskVO.optString("bizInfo"))
                val taskType: String = antOceanTaskVO.getString("taskType")
                val sceneCode: String = antOceanTaskVO.getString("sceneCode")
                AntOceanRpcCall.receiveTaskAward(sceneCode, taskType).onSuccess { receiveJson ->
                    if (!receiveJson.success) {
                        logger.e("领取[$taskType]任务奖励失败: %s", it)
                        return@onSuccess
                    }
                    val taskTitle = bizInfo.optString("taskTitle", taskType)
                    val taskDesc = bizInfo.optString("taskDesc", taskType)
                    logger.i("领取奖励🎖️%s#%s", taskTitle, taskDesc)
                }
                delay(1000)
            }
        }
    }
}