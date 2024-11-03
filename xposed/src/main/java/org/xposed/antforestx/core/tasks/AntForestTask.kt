package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import org.xposed.antforestx.core.ant.AntForestRpcCall
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.DataInfoManager
import org.xposed.antforestx.core.manager.RecordManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.AntToast
import org.xposed.antforestx.core.util.isNullOrEmpty
import org.xposed.antforestx.core.util.isSuccess
import org.xposed.antforestx.core.util.log.antForest
import org.xposed.antforestx.core.util.log.enableForest
import org.xposed.antforestx.core.util.onSuccessCatching
import org.xposed.antforestx.core.util.runCatchSuspend
import org.xposed.antforestx.core.util.runCatchSuspendResult
import org.xposed.antforestx.core.util.success
import org.xposed.antforestx.core.util.toStringList
import org.xposed.forestx.core.utils.AppCoroutine
import org.zipper.antforestx.data.bean.AntForestPropData
import org.zipper.antforestx.data.bean.AntForestPropInfo
import org.zipper.antforestx.data.bean.LabelType
import org.zipper.antforestx.data.bean.VitalityExchangedPropData
import org.zipper.antforestx.data.bean.VitalityPropInfo
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.Volatile

/**
 * 蚂蚁森林
 */
class AntForestTask : ITask {

    private val logger get() = Timber.antForest()

    private var propList = ArrayList<AntForestPropInfo>()

    /**
     * 双击卡结束时间
     */
    @Volatile
    private var doubleEndTime: Long = 0

    override suspend fun start() {
        Timber.enableForest()
        // 更新用户道具
        updatePopList()
        val job = AppCoroutine.launch(Dispatchers.IO) {
            consumeCollectTask()
        }
        // 自身能量
        collectSelfEnergy()
        // 能量雨
        energyRain()
        // 任务列表
        queryTaskList()

        // 找能量
        takeEnergy()
        updatePopList()
//        // 查询动态
//        queryDynamicsIndex()
        job.cancel()
    }

    /**
     * 收能量
     */
    private suspend fun collectSelfEnergy() {
        if (!ConfigManager.forestConfig.isCollectEnergy) {
            return
        }
        logger.i("开始收取自身能量")
        AntForestRpcCall.queryHomePage().onSuccessCatching {
            if (!it.success) {
                logger.e("查询首页失败 %s", it)
                return@onSuccessCatching
            }
            enterEnergyHome(it, false)
        }
        logger.i("收取自身能量完成")
    }

    /**
     * 找能量
     */
    private suspend fun takeEnergy() {
        if (!ConfigManager.forestConfig.enableCollectFriends) {
            logger.d("不收取好友能量")
            return
        }

        val hasMoreUser = AtomicBoolean(true)
        while (hasMoreUser.get()) {
            AntForestRpcCall.takeLook().onSuccessCatching {
                if (!it.success) {
                    hasMoreUser.set(false)
                    logger.e("查询能量列表失败 %s", it)
                    return@onSuccessCatching
                }
                if (!it.has("friendId")) {
                    hasMoreUser.set(false)
                    // 没有更多好友了
                    return@onSuccessCatching
                }
                val friendId = it.getString("friendId")
                AntForestRpcCall.queryFriendHomePage(friendId).onSuccessCatching HomePage@{ res ->
                    if (!res.success) {
                        logger.e("查询好友详情失败 %s", res)
                        return@HomePage
                    }
                    enterEnergyHome(res, ConfigManager.forestConfig.isBatchRobEnergy)
                }
            }

            delay(ConfigManager.forestConfig.collectInterval)
        }

        AntForestRpcCall.takeLookEnd().onSuccessCatching {
            if (!it.success) {
                logger.e("查询能量列表失败 %s", it)
                return@onSuccessCatching
            }
            // 今日获取活力值
            val todayReceiveVitalityAmount = it.getInt("todayReceiveVitalityAmount")
            // 今日能量
            val todayRobEnergy = it.getInt("todayRobEnergy")
            // 当前能量
            val currentEnergy = it.getInt("currentEnergy")
        }
    }


    private suspend fun enterEnergyHome(homeJson: JSONObject, batchCollect: Boolean = true) {
        val userEnergy = homeJson.getJSONObject("userEnergy")
        val userId = userEnergy.getString("userId")
        val canCollectEnergy = userEnergy.getBoolean("canCollectEnergy")
        val canCollectGiftBox = userEnergy.getBoolean("canCollectGiftBox")
        val canHelpCollect = userEnergy.getBoolean("canHelpCollect")
        val canProtectBubble = userEnergy.getBoolean("canProtectBubble")
        var displayName = userEnergy.getString("displayName")
        val loginId = userEnergy.optString("loginId") ?: "*null*"
        if (displayName.isNullOrEmpty()) {
            displayName = "我的"
        }
        logger.i("进入用户[%s(%s)]的蚂蚁森林 是否批量 %s", displayName, loginId, batchCollect)
        val waitCollectList = ArrayList<CollectTaskInfo>()
        val bubbles = homeJson.getJSONArray("bubbles")
        if (batchCollect) {
            val bundleIds = mutableListOf<Long>()
            for (i in 0 until bubbles.length()) {
                val bubble = bubbles.getJSONObject(i)
                val id = bubble.getLong("id")
                // 收取状态
                val collectStatus = bubble.getString("collectStatus")
                val business = bubble.getJSONObject("business")
                val bigIconDisplayName = business.getString("bigIconDisplayName")
                val produceTime = bubble.getLong("produceTime")
                logger.d("能量球状态 status = %s bigIconDisplayName = %s", collectStatus, bigIconDisplayName)
                if (collectStatus.isAvailable()) {
                    val bundleId = bubble.getLong("id")
                    bundleIds.add(bundleId)
                } else if (collectStatus.isWaiting()) {
                    val collectTaskInfo = CollectTaskInfo(userId, listOf(id), collectStatus, produceTime, bigIconDisplayName)
                    waitCollectList.add(collectTaskInfo)
                }
            }
            submitCollectTask {
                logger.i("开始批量收取能量 %s", waitCollectList)
                handleBatchCollectEnergy(userId, bundleIds)
            }
            return
        }


        for (i in 0 until bubbles.length()) {
            val bubble = bubbles.getJSONObject(i)
            // 收取状态
            val collectStatus = bubble.getString("collectStatus")
            // 完整能量
            val fullEnergy = bubble.getInt("fullEnergy")
            val business = bubble.getJSONObject("business")
            val bigIconDisplayName = business.getString("bigIconDisplayName")
            val produceTime = bubble.getLong("produceTime")
            val id = bubble.getLong("id")
            logger.d("能量球状态 status = %s energy = %s bigIconDisplayName = %s", collectStatus, fullEnergy, bigIconDisplayName)
            if (collectStatus.isAvailable()) {
                val collectTaskInfo = CollectTaskInfo(userId, listOf(id), collectStatus, produceTime, bigIconDisplayName)
                submitCollectTask {
                    logger.i("开始收取能量 %s", collectTaskInfo)
                    handleCollectEnergy(collectTaskInfo)
                }
            }
        }
    }

    private suspend fun handleCollectEnergy(task: CollectTaskInfo) {
        if (ConfigManager.forestConfig.unCollectFriendList.contains(task.userId)) {
            logger.i("不偷取[%s]的能量", DataInfoManager.getFriendById(task.userId))
            return
        }
        var useDoubleCard = false
        if (ConfigManager.canUseDoubleProp) {
            updateDoubleTime()
            if (doubleEndTime < System.currentTimeMillis() && RecordManager.canUseDoublePropToday()) {
                // 双击卡过期且可以使用
                useDoubleCard()
                useDoubleCard = true
            }
        }
        AntForestRpcCall.collectEnergy(null, task.userId, task.bubbleIds, false).onSuccessCatching {
            if (!it.success) {
                logger.e("收取能量失败 %s", it)
                throw RuntimeException("收取能量失败, ${it.optString("resultDesc")}")
            }
            val userBaseInfo = it.getJSONObject("userBaseInfo")
            var displayName = userBaseInfo.optString("displayName")
            if (displayName.isNullOrEmpty()) {
                displayName = "我的"
            }
            val bubbles = it.getJSONArray("bubbles")
            var collectEnergyCount = 0
            for (i in 0 until bubbles.length()) {
                val bubble = bubbles.getJSONObject(i)
                val collectedEnergy = bubble.getInt("collectedEnergy")
                val canBeRobbedAgain = bubble.getBoolean("canBeRobbedAgain")
                val id = bubble.getLong("id")
                if (canBeRobbedAgain) {
                    val result = AntForestRpcCall.collectEnergy(null, task.userId, listOf(id), false)
                    if (result.isSuccess && result.getOrNull()?.success == true) {
                        collectEnergyCount += result.getOrThrow().getJSONArray("bubbles").getJSONObject(0).getInt("collectedEnergy")
                    }
                }
                collectEnergyCount += collectedEnergy

            }
            val msg =
                "收取[${displayName}]能量#${collectEnergyCount}g${if (useDoubleCard) "#双击卡" else ""}"
            logger.i(msg)
            AntToast.showShort(msg)
            appendEnergy(collectEnergyCount)
        }.onFailure {
            if (it is RuntimeException) {
                throw it
            }
        }
    }

    /**
     * 批量收取
     */
    private suspend fun handleBatchCollectEnergy(userId: String, bubbleIds: List<Long>, doubleClick: Boolean = false): Int {
        if (ConfigManager.forestConfig.unCollectFriendList.contains(userId)) {
            logger.i("不偷取[%s]的能量", DataInfoManager.getFriendById(userId))
            return 0
        }
        if (bubbleIds.isEmpty()) {
            return 0
        }
        logger.i("一键收取[%s]能量", UserManager.getAlipayUserName(userId))
        if (ConfigManager.canUseDoubleProp) {
            updateDoubleTime()
            if (doubleEndTime < System.currentTimeMillis() && RecordManager.canUseDoublePropToday()) {
                // 双击卡过期且可以使用
                useDoubleCard()
            }
            delay(2000)
        }
        var collectEnergyCount = 0
        delay(ConfigManager.forestConfig.collectInterval)
        AntForestRpcCall.collectEnergy(null, userId, bubbleIds, true).onSuccessCatching {
            if (!it.success) {
                logger.e("一键收取能量失败 %s", it)
                throw RuntimeException("一键收取能量失败, ${it.optString("resultDesc")}")
            }
            val userBaseInfo = it.getJSONObject("userBaseInfo")
            var displayName = userBaseInfo.optString("displayName")
            if (displayName.isNullOrEmpty()) {
                displayName = "我的"
            }
            val bubbles = it.getJSONArray("bubbles")

            val collectDoubleIds = mutableListOf<Long>()
            for (i in 0 until bubbles.length()) {
                val bubble = bubbles.getJSONObject(i)
                val collectedEnergy = bubble.getInt("collectedEnergy")
                val canBeRobbedAgain = bubble.getBoolean("canBeRobbedAgain")
                val id = bubble.getLong("id")
                if (canBeRobbedAgain) {
                    collectDoubleIds.add(id)
                }
                collectEnergyCount += collectedEnergy
            }
            collectEnergyCount += handleBatchCollectEnergy(userId, collectDoubleIds, doubleClick)

            val msg = "批量收取[${displayName}]能量#${collectEnergyCount}g${if (doubleClick) "#双击卡" else ""}"
            logger.i(msg)
            AntToast.showShort(msg)
            appendEnergy(collectEnergyCount)
        }
        return collectEnergyCount
    }

    /**
     * 能量雨
     */
    private suspend fun energyRain() {
        if (!ConfigManager.forestConfig.isCollectEnergyRain) {
            logger.i("收集能量雨失败，开关关闭")
            return
        }

        AntForestRpcCall.queryEnergyRainHome().onSuccessCatching {
            if (!it.success) {
                logger.e("查询能量雨首页失败 %s", it)
                return@onSuccessCatching
            }
            val canGrantStatus = it.getBoolean("canGrantStatus")
            val canPlayToday = it.getBoolean("canPlayToday")
            if (canPlayToday) {
                startEnergyRain()
            }
            if (canGrantStatus) {
                logger.i("有送能量雨的机会")
//                queryEnergyRainCanGrantList()
                val grantInfos = it.getJSONArray("grantInfos")
                sendEnergyRain(grantInfos)
            }
        }
    }

    private suspend fun startEnergyRain() {
        logger.i("开启能量雨")
        AntForestRpcCall.startEnergyRain().onSuccessCatching {
            if (!it.success) {
                logger.e("能量雨失败 %s", it)
                return@onSuccessCatching
            }
            val token = it.getString("token")
            var totalRain = 0
            val bubbleEnergyList = it.getJSONObject("difficultyInfo").getJSONArray("bubbleEnergyList")
            for (i in 0 until bubbleEnergyList.length()) {
                totalRain += bubbleEnergyList.getInt(i)
            }
            delay(5000)
            AntForestRpcCall.energyRainSettlement(totalRain, token).onSuccess { json ->
                if (json.success) {
                    val vitalityAmount = json.optJSONObject("extInfo")?.optInt("vitalityAmount") ?: 0
                    logger.i("收集能量雨，获得了[%s能量]、[%s活力值]", totalRain, vitalityAmount)
                    appendVitalityAmount(vitalityAmount)
                    delay(100)
                    appendEnergy(totalRain)
                }
            }
        }
    }

    /**
     * 查询是否可送能量雨
     */
    private suspend fun queryEnergyRainCanGrantList() {
        AntForestRpcCall.queryEnergyRainCanGrantList().onSuccessCatching {
            if (!it.success) {
                logger.e("查询能量雨可领列表失败 %s", it)
                return@onSuccessCatching
            }
            val grantInfos = it.getJSONArray("grantInfos")
            sendEnergyRain(grantInfos)
        }
    }

    private suspend fun sendEnergyRain(grantInfos: JSONArray) {
        val energyFriendRainList = ConfigManager.forestConfig.energyFriendRainList
        if (energyFriendRainList.isEmpty()) {
            logger.i("能量雨没有好友可送")
            return
        }
        for (i in 0 until grantInfos.length()) {
            val grantInfo = grantInfos.getJSONObject(i)
            if (!grantInfo.getBoolean("canGrantedStatus")) {
                continue
            }
            val userId = grantInfo.getString("userId")
            if (!energyFriendRainList.contains(userId)) {
                continue
            }
            logger.i("尝试送能量雨给[%s]", DataInfoManager.getFriendById(userId))
            AntForestRpcCall.grantEnergyRainChance(userId).onSuccessCatching Grant@{ json ->
                if (json.success) {
                    logger.i("送能量雨给[%s]成功", DataInfoManager.getFriendById(userId))
                    delay(1000)
                    startEnergyRain()
                } else {
                    logger.i("送能量雨给[%s]失败", DataInfoManager.getFriendById(userId))
                }
            }
        }
    }

    private val collectTaskChannel = Channel<suspend () -> Unit>(Channel.UNLIMITED)
    private val executeTaskCount = AtomicInteger(0)

    /**
     * 提交任务
     */
    private suspend fun submitCollectTask(block: suspend () -> Unit) {
        collectTaskChannel.send(block)
        val collectInterval = ConfigManager.forestConfig.collectInterval
        delay(collectInterval)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun consumeCollectTask() {
        while (true) {
            val startTime = System.currentTimeMillis()
            var currentMinuteTasks = 0
            val limitCount = ConfigManager.limitCountInMinute
            val enableLimit = ConfigManager.isLimitForestCollect
            while (System.currentTimeMillis() - startTime < TimeUnit.MINUTES.toMillis(1)) {
                // 每次执行任务后检查当前已执行的任务
                if (collectTaskChannel.isEmpty || (currentMinuteTasks >= limitCount && enableLimit)) {
                    logger.d("当前已执行任务数 %s", currentMinuteTasks)
                    break // 达到最大任务数，暂停
                }

                val task = collectTaskChannel.receive() // 接收任务
                logger.d("执行任务 %s", task)
                executeTaskCount.incrementAndGet()
                currentMinuteTasks++
                runCatchSuspendResult {
                    task.invoke()
                }.onFailure {
                    logger.e(it, "任务执行异常，等待10s")
                    // 出现异常，等待10s
                    delay(10000)
                }
            }
            val delayTime = TimeUnit.MINUTES.toMillis(1) - (System.currentTimeMillis() - startTime)
            if (delayTime > 0) {
                delay(delayTime)
            }
        }
    }


    /**
     * 活力值任务
     */
    private suspend fun queryTaskList() {
        if (!ConfigManager.forestConfig.enableForestTask) {
            logger.d("森林任务未启用")
            return
        }
        AntForestRpcCall.queryTaskList().onSuccessCatching { json ->
            if (!json.isSuccess()) {
                logger.e("查询任务列表失败 %s", json)
                return@onSuccessCatching
            }
            // 活力值签到
            forestSign(json.optJSONArray("forestSignVOList"))
            delay(1000)
            // 任务
            forestTasksNew(json.optJSONArray("forestTasksNew"))
        }
        delay(5000)
        AntForestRpcCall.queryTaskList().onSuccessCatching { json ->
            // 任务收取奖励
            forestTasksNew(json.optJSONArray("forestTasksNew"))
        }
    }

    /**
     * 活力值签到
     */
    private suspend fun forestSign(forestSignVOList: JSONArray?) {
        if (forestSignVOList == null) {
            return
        }
        logger.i("检查到活力值签到任务")
        for (i in 0 until forestSignVOList.length()) {
            val forestSignVO = forestSignVOList.optJSONObject(i)
            val currentSignKey = forestSignVO.optString("currentSignKey") ?: continue
            val signRecords = forestSignVO.optJSONArray("signRecords") ?: continue
            for (j in 0 until signRecords.length()) {
                val signRecord = signRecords.optJSONObject(j)
                val signKey = signRecord.optString("signKey") ?: continue
                if (currentSignKey == signKey && !signRecord.optBoolean("signed")) {
                    val awardCount = signRecord.getInt("awardCount")
                    AntForestRpcCall.vitalitySign().onSuccessCatching Sign@{ signJson ->
                        if (signJson.success) {
                            logger.i("活力值签到成功，获得活力值：%s", awardCount)
                        }
                    }
                }
            }
        }
    }

    /**
     * 活力值任务列表
     */
    private suspend fun forestTasksNew(forestTasksNew: JSONArray?) = runCatchSuspend {
        if (forestTasksNew.isNullOrEmpty()) {
            return@runCatchSuspend
        }
        logger.i("解析签到任务列表")

        for (i in 0 until forestTasksNew!!.length()) {
            val foreTask = forestTasksNew.getJSONObject(i)
            val taskGroupType = foreTask.getString("taskGroupType")
            val taskInfoList = foreTask.getJSONArray("taskInfoList")
            if (taskInfoList.isNullOrEmpty() || taskGroupType != "COMPLIATION") {
                continue
            }
            for (j in 0 until taskInfoList.length()) {
                val taskInfo = taskInfoList.optJSONObject(j)
                if (taskInfo.has("childTaskTypeList")) {
                    val childTaskTypeList = taskInfo.getJSONArray("childTaskTypeList")
                    if (childTaskTypeList.length() > 0) {
                        logger.i("发现子任务 %s，开始执行子任务", childTaskTypeList.length())
                        for (k in 0 until childTaskTypeList.length()) {
                            val childTask = childTaskTypeList.getJSONObject(k)
                            val childTaskBaseInfo = childTask.getJSONObject("taskBaseInfo")
                            val taskStatus = childTaskBaseInfo.getString("taskStatus")
                            val sceneCode = childTaskBaseInfo.getString("sceneCode")
                            val taskType = childTaskBaseInfo.getString("taskType")
                            val bizInfo = childTaskBaseInfo.optString("bizInfo")
                            if (taskStatus.isFinished()) {
                                receiveTaskAward(sceneCode, taskType, bizInfo)
                            } else if (taskStatus.isTodo()) {
                                finishTask(sceneCode, taskType, bizInfo)
                            }
                        }
                    }
                }
                val taskBaseInfo = taskInfo.getJSONObject("taskBaseInfo")
                val taskStatus = taskBaseInfo.getString("taskStatus")
                val sceneCode = taskBaseInfo.getString("sceneCode")
                val taskType = taskBaseInfo.getString("taskType")

                if (taskStatus.isFinished()) {
                    receiveTaskAward(sceneCode, taskType, taskBaseInfo.optString("bizInfo"))
                } else if (taskStatus.isTodo()) {
                    finishTask(sceneCode, taskType, taskBaseInfo.optString("bizInfo"))
                }
                delay(500)
            }

        }
    }


    /**
     * 活力值兑换
     */
    private suspend fun vitalityExchange() {
        updateUserVitality()
        updateExchangedPropList()
        // TODO 获取用户选择的道具id，兑换
    }

    /**
     * 更新活力值兑换的道具列表
     */
    private suspend fun updateExchangedPropList() {
        val propList = VitalityExchangedPropData()
        for (entry in LabelType.entries) {
            AntForestRpcCall.itemList(entry.label).onSuccessCatching {
                if (!it.success) {
                    logger.e("查询兑换列表失败 %s", it)
                    return@onSuccessCatching
                }
                val itemInfoVOList = it.getJSONArray("itemInfoVOList")
                for (i in 0 until itemInfoVOList.length()) {
                    val itemInfoVO = itemInfoVOList.optJSONObject(i)
                    val skuId = itemInfoVO.optString("skuId") ?: continue
                    val spuId = itemInfoVO.optString("spuId") ?: continue
                    val rightsConfigId = itemInfoVO.optString("rightsConfigId") ?: continue
                    val skuName = itemInfoVO.optString("skuName") ?: continue
                    val priceJson = itemInfoVO.optJSONObject("price") ?: continue
                    val amount = priceJson.optDouble("amount")
                    val priceCent = priceJson.optLong("cent")
                    val prop = VitalityPropInfo(entry, skuId, spuId, rightsConfigId, skuName, amount, priceCent)
                    propList.add(prop)
                }
            }
        }

        // 更新道具列表到本地
        DataInfoManager.updateVitalityInfo(propList)

    }

    /**
     * 过期活力值
     */
    private var overDueVitalityAmount = 0

    /**
     * 总活力值
     */
    private var totalVitalityAmount = 0

    /**
     * 查询用户活力值
     */
    private suspend fun updateUserVitality() {
        AntForestRpcCall.queryVitalityStoreIndex().onSuccessCatching {
            if (!it.success) {
                logger.e("查询兑换列表失败 %s", it)
                return@onSuccessCatching
            }
            val userVitalityInfoVO = it.optJSONObject("userVitalityInfoVO") ?: return@onSuccessCatching
            overDueVitalityAmount = userVitalityInfoVO.optInt("overDueVitalityAmount")
            totalVitalityAmount = userVitalityInfoVO.optInt("totalVitalityAmount")
        }
    }

    private suspend fun updateDoubleTime() {
        logger.i("更新双击卡结束时间")
        AntForestRpcCall.queryHomePage().onSuccess { json ->
            var usingUserPropsNew = json.optJSONArray("loginUserUsingPropNew") ?: return@onSuccess
            if (usingUserPropsNew.length() == 0) {
                usingUserPropsNew = json.optJSONArray("usingUserPropsNew") ?: return@onSuccess
            }

            for (i in 0 until usingUserPropsNew.length()) {
                val prop = usingUserPropsNew.optJSONObject(i)
                val propType = prop.optString("propType") ?: continue
                if ("ENERGY_DOUBLE_CLICK" == propType || "LIMIT_TIME_ENERGY_DOUBLE_CLICK" == propType) {
                    doubleEndTime = prop.optLong("endTime")
                    logger.i("双倍卡剩余时间⏰${(doubleEndTime - System.currentTimeMillis()) / 1000}")
                }
            }
        }
        delay(500)
    }

    /**
     * 使用双击卡
     */
    private suspend fun useDoubleCard() {
        updatePopList()
        // 限时双击卡
        val timeProp = propList.filter {
            it.propType == "TIME_ENERGY_DOUBLE_CLICK"
        }

        val doubleProp = propList.filter {
            it.propType == "ENERGY_DOUBLE_CLICK"
        }

        if (timeProp.isEmpty() && doubleProp.isEmpty()) {
            return
        }

        val useSuccess = suspend {
            UserManager.updateDoubleClickUse()
            updateDoubleTime()
        }

        // 优先使用限时
        if (timeProp.isNotEmpty()) {
            val prop = timeProp.first()
            AntForestRpcCall.consumeProp(prop.propId, prop.propType).onSuccessCatching {
                if (it.success) {
                    logger.i("使用限时双击卡成功")
                    useSuccess()
                    return@onSuccessCatching
                } else {
                    logger.e("使用限时双击卡失败 %s", it)
                }
            }
            return
        }

        val prop = doubleProp.first()
        AntForestRpcCall.consumeProp(prop.propId, prop.propType).onSuccessCatching {
            if (it.success) {
                logger.i("使用双击卡成功")
                useSuccess()
                return@onSuccessCatching
            } else {
                logger.e("使用双击卡失败 %s", it)
            }
        }

    }

    /**
     * 更新拥有的道具列表
     */
    private suspend fun updatePopList() {
        AntForestRpcCall.queryPropList(false).onSuccessCatching {
            if (it.success) {
                val forestPropVOList = it.getJSONArray("forestPropVOList")
                val list = AntForestPropData(forestPropVOList.length())
                for (i in 0 until forestPropVOList.length()) {
                    val prop = forestPropVOList.getJSONObject(i)
                    val propType = prop.getString("propType")
                    val propGroup = prop.getString("propGroup")
                    val propIdList = prop.getJSONArray("propIdList")
                    val recentExpireTime = prop.optLong("recentExpireTime")
                    val holdsNum = prop.optInt("holdsNum")
                    val propConfigVO = prop.getJSONObject("propConfigVO")
                    val propName = propConfigVO.getString("propName")
                    val durationTime = propConfigVO.getLong("durationTime")
                    list.add(AntForestPropInfo(propIdList.toStringList().first(), propType, propName, durationTime, holdsNum, propGroup, recentExpireTime))
                }
                DataInfoManager.updateForestPropData(list)
                propList = list
            }
        }
    }


    /**
     * 今日动态
     */
    private suspend fun queryDynamicsIndex() {
        AntForestRpcCall.queryDynamicsIndex().onSuccess { json ->
            if (!json.isSuccess()) {
                return@onSuccess
            }
            val todayEnergySummary = json.optJSONObject("todayEnergySummary") ?: return@onSuccess
            val title = todayEnergySummary.optString("title")
            val leftText = todayEnergySummary.optString("leftText")
            val rightText = todayEnergySummary.optString("rightText")
            logger.i("今日动态: %s %s %s", title, leftText, rightText)
        }
    }

    /**
     * 收取任务奖励
     */
    private suspend fun receiveTaskAward(sceneCode: String, taskType: String, bizInfoString: String) {
        AntForestRpcCall.receiveTaskAward(sceneCode, taskType).onSuccessCatching { taskJson ->
            if (!taskJson.success) {
                return@onSuccessCatching
            }
            val bizInfo = JSONObject(bizInfoString)
            val taskTitle = bizInfo.optString("taskTitle")
            val awardCount = taskJson.optInt("awardCount")
            logger.i("收取任务[%s]奖励 %s活力值", taskTitle, awardCount)
            appendVitalityAmount(awardCount)
        }
        delay(200)
    }

    /**
     * 完成任务
     */
    private suspend fun finishTask(sceneCode: String, taskType: String, bizInfoString: String) {
        if (taskType == "SHARETASK") {
            // 邀请好友不执行
            return
        }
        logger.i("执行完成任务, bizInfo: %s", bizInfoString)
        AntForestRpcCall.finishTask(sceneCode, taskType).onSuccessCatching {
            if (!it.success) {
                logger.e("完成任务失败 %s %s %s", sceneCode, taskType, bizInfoString)
                return@onSuccessCatching
            }
            val bizInfo = JSONObject(bizInfoString)
            val taskTitle = bizInfo.optString("taskTitle")
            logger.i("完成任务[%s] %s %s", taskTitle, sceneCode, taskType)
        }
        delay(200)
    }

    /**
     * 活力值增加
     */
    private suspend fun appendVitalityAmount(count: Int) {

    }

    private suspend fun appendEnergy(count: Int) {
        RecordManager.addEnergy(count)
    }

    data class PropInfo(
        val propType: String,
        val propName: String,
        val durationTime: Long,
        val holdsNum: Int,
        val propGroup: String,
        val propIdList: JSONArray,
        val recentExpireTime: Long
    )

    data class CollectTaskInfo(
        val userId: String,
        val bubbleIds: List<Long>,
        val collectStatus: String,
        val productTime: Long,
        val energyName: String,
    )

    data class BatchCollectTaskInfo(
        val userId: String,
        val bubbleIds: List<Long>,
        val collectStatus: String,
        val minProductTime: Long
    )

    private fun String.isTodo() = this == "TODO"
    private fun String.isFinished() = this == "FINISHED"
    private fun String.isReceived() = this == "RECEIVED"

    private fun String.isAvailable() = this == "AVAILABLE"

    private fun String.isWaiting() = this == "WAITING"
}