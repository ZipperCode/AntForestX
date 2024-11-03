package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.xposed.antforestx.core.ant.AntGoldRpcCall
import org.xposed.antforestx.core.ant.AntMemberRpcCall
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.isSuccess
import org.xposed.antforestx.core.util.log.enableMember
import org.xposed.antforestx.core.util.onSuccessCatching
import org.xposed.antforestx.core.util.safeToInt
import org.xposed.antforestx.core.util.success
import timber.log.Timber

/**
 * 会员中心任务
 */
class AntMemberTask : ITask {

    private val logger get() = Timber.tag("会员签到")
    override suspend fun start(): Unit = withContext(Dispatchers.IO + CoroutineName("AntMember")) {
        Timber.enableMember()
        logger.i("开始处理会员中心积分任务")
        if (!UserManager.validateUser()) {
            return@withContext
        }
        runCatching {
            if (ConfigManager.otherConfig.enableSign) {
                // 签到日历
                queryMemberSigninCalendar()
            }
            if (ConfigManager.otherConfig.enableCollectIntegral) {
                // 收取积分
                queryPointCertV2()
            }
            if (ConfigManager.otherConfig.enableIntegralTask) {
                // 逛一逛任务
                queryAllStatusTaskList()
                // 签到任务
                signPageTaskList()
            }
            if (ConfigManager.otherConfig.enableMerchant) {
                // 商家服务
                merchantQuery()
            }

            // 黄金票
            goldIndex()
        }
    }

    /**
     * 签到日历
     */
    private suspend fun queryMemberSigninCalendar() {
        AntMemberRpcCall.queryMemberSigninCalendar().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询会员签到日历失败: %s", it)
                return@onSuccessCatching
            }
            val autoSignInSuccess = it.getBoolean("autoSignInSuccess")
            val currentSigninStatus = it.getBoolean("currentSigninStatus")
            // 签到积分
            val signinPoint = it.getInt("signinPoint")
            // 总签到积分
            val signinSumPoint = it.getInt("signinSumPoint")
            logger.i(
                "是否自动签到: %s, 当天签到状态:%s, 签到积分: %s, 总签到积分: %s",
                autoSignInSuccess,
                currentSigninStatus,
                signinPoint,
                signinSumPoint
            )
            if (!currentSigninStatus) {
                AntMemberRpcCall.memberSignin().onSuccessCatching signReturn@{ json ->
                    if (!json.isSuccess()) {
                        logger.e("会员签到失败: %s", json)
                        return@signReturn
                    }
                    if (it.optBoolean("currentSigninStatus")) {
                        logger.i("会员每日签到成功")
                        appendPoint(signinPoint.toString())
                    }
                }
            }
        }
        // TODO 查询月签到进行补签
    }


    private suspend fun queryPointCertV2() {
        AntMemberRpcCall.queryPointCertV2().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询积分证书失败: %s", it)
                return@onSuccessCatching
            }
            delay(500)
            var certList = JSONArray()
            if (it.has("resultMap")) {
                val resultMap = it.getJSONObject("resultMap")
                val queryPointCert = resultMap.getJSONObject("com.alipay.alipaymember.biz.rpc.pointcert.h5.queryPointCertV2")
                certList = queryPointCert.getJSONArray("certList")
            } else if (it.has("certList")) {
                certList = it.getJSONArray("certList")
            }
            var totalPoint = 0
            for (i in 0 until certList.length()) {
                val cert = certList.getJSONObject(i)
                val bizTitle = cert.getString("bizTitle")
                val id = cert.getString("id")
                val producePoint = cert.getInt("producePoint")
                totalPoint += producePoint
//                AntMemberRpcCall.receivePointByUser(id).onSuccessCatching PointRec@{ json ->
//                    if (!json.success) {
//                        logger.e("收取积分失败: %s", json)
//                        return@PointRec
//                    }
//                    logger.i("收取积分成功，名称: %s, 积分: %s", bizTitle, producePoint)
//                    appendPoint(producePoint.toString())
//                }
            }
            AntMemberRpcCall.receiveAllPointByUser().onSuccessCatching Point@{ json ->
                if (!json.success) {
                    logger.e("领取全部积分失败: %s", it)
                    return@Point
                }
                logger.i("领取全部积分成功, 总共收取 %s 积分", totalPoint)
                appendPoint(totalPoint.toString())
            }
        }
    }

    /**
     * 会员页任务
     */
    private suspend fun signPageTaskList() {
        AntMemberRpcCall.signPageTaskList().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询任务列表失败: %s", it)
                return@onSuccessCatching
            }
            val categoryTaskList = it.getJSONArray("categoryTaskList")
            for (i in 0 until categoryTaskList.length()) {
                val taskCategory = categoryTaskList.getJSONObject(i)
                val taskList = taskCategory.getJSONArray("taskList")
                val groupCode = taskCategory.optString("groupCode")
                val type = taskCategory.getString("type")

                if (groupCode == "TASK_CONV" || type == "OTHERS") {
                    logger.i(
                        "不执行其他任务，分类 = %s, 类型 = %s, 任务数量 = %s",
                        groupCode, type, taskList.length()
                    )
                    continue
                }
                logger.i(
                    "执行分类任务，当前分类为 = %s, 类型 = %s, 任务数量 = %s",
                    groupCode, type, taskList.length()
                )
                for (j in 0 until taskList.length()) {
                    val task = taskList.getJSONObject(j)
                    val taskConfigInfo = task.getJSONObject("taskConfigInfo")
                    val oraScm = taskConfigInfo.optString("oraScm")
                    val id = taskConfigInfo.getLong("id")
                    val name = taskConfigInfo.getString("name")
                    var current = 0
                    var count = 1
                    val extInfo = task.optJSONObject("extInfo")
                    if (extInfo != null) {
                        current = extInfo.optString("PERIOD_CURRENT_COUNT").safeToInt()
                        count = extInfo.optString("PERIOD_TARGET_COUNT").safeToInt()
                    }
                    logger.i("任务: %s %s current = %s, count = %s", id, name, current, count)
                    while (current < count) {
                        handleTask(id, oraScm, name)
                        current++
                    }
                }
            }
        }
    }

    /**
     * 接受任务
     */
    private suspend fun handleTask(taskId: Long, oarScm: String, name: String) {
        AntMemberRpcCall.applyTask(name, oarScm, taskId).onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("领取任务失败: %s", it)
                return@onSuccessCatching
            }
            logger.i("领取任务成功，任务名称: %s", name)
            val taskConfigInfo = it.getJSONObject("processInfo").getJSONObject("taskConfigInfo")
            val awardParam = taskConfigInfo.getJSONObject("awardParam")
            val awardParamPoint = awardParam.getString("awardParamPoint")
            val isPoint = taskConfigInfo.getString("awardType") == "POINT"
            val targetBusiness = taskConfigInfo.getJSONArray("targetBusiness")
            AntMemberRpcCall.executeTask(targetBusiness.getString(0)).onSuccessCatching ExeTask@{ exeJson ->
                if (!exeJson.isSuccess()) {
                    logger.e("执行任务失败: %s", exeJson)
                    return@ExeTask
                }
                logger.i("完成任务成功，任务名称: %s, 积分: %s", name, awardParamPoint)
                if (isPoint) {
                    appendPoint(awardParamPoint)
                }
            }

        }
        delay(1000)
    }

    /**
     * 逛一逛领积分
     */
    private suspend fun queryAllStatusTaskList() {
        logger.i("查询逛一逛任务")
        AntMemberRpcCall.queryAllStatusTaskList().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询任务列表失败: %s", it)
                return@onSuccessCatching
            }

            val availableTaskList = it.getJSONArray("availableTaskList")
            logger.i("逛一逛任务列表 数量: %s", availableTaskList.length())
            for (i in 0 until availableTaskList.length()) {
                val task = availableTaskList.getJSONObject(i)
                val taskConfigInfo = task.getJSONObject("taskConfigInfo")
                val id = taskConfigInfo.getLong("id")
                val name = taskConfigInfo.getString("name")
                val targetBusiness = taskConfigInfo.getJSONArray("targetBusiness")
                val taskStage = taskConfigInfo.getInt("taskStage")
                val awardParam = taskConfigInfo.optJSONObject("awardParam")
                val awardParamPoint = awardParam?.getString("awardParamPoint") ?: "0"
                val isPoint = taskConfigInfo.getString("awardType") == "POINT"

                logger.i("执行逛一逛积分任务: [阶段-%s] %s_%s 可获取积分 %s", taskStage, id, name, awardParamPoint)
                AntMemberRpcCall.executeTask2(id.toString(), targetBusiness.optString(0)).onSuccessCatching ExeTask@{ json ->
                    if (!json.isSuccess()) {
                        logger.e("执行逛一逛积分任务失败: %s", json)
                        return@ExeTask
                    }
                    logger.i("执行逛一逛任务成功，获取积分: %s", awardParamPoint)
                    if (isPoint) {
                        appendPoint(awardParamPoint)
                    }
                }
                delay(5000)
            }

        }
        logger.i("逛一逛任务执行完成")
    }

    /**
     * 累积积分
     */
    private suspend fun appendPoint(point: String) {

    }

    /**
     * 查询用户芝麻粒
     */
    private suspend fun getUserPotential() {
        AntMemberRpcCall.getUserPotential().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询用户潜力失败: %s", it)
                return@onSuccessCatching
            }
            val currentPoints = it.getInt("currentPoints")
            logger.i("查询用户芝麻粒值成功，当前剩余: %s", currentPoints)
            UserManager.updateUserCreditPoints(currentPoints)
        }
    }

    /**
     * 芝麻粒任务列表
     * // TODO 完成任务判断和收取
     */
    private suspend fun queryListV3() {
        AntMemberRpcCall.queryListV3().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询芝麻粒任务列表失败: %s", it)
                return@onSuccessCatching
            }
            val toCompleteVOS = it.getJSONArray("toCompleteVOS")
            for (i in 0 until toCompleteVOS.length()) {
                val task = toCompleteVOS.getJSONObject(i)
                val finishFlag = task.getBoolean("finishFlag")
                if (finishFlag) {
                    continue
                }
                val templateId = task.getString("templateId")
                val needCompleteNum = task.getInt("needCompleteNum")
                val completedNum = task.getInt("completedNum")
                val rewardAmount = task.getInt("rewardAmount")
                val title = task.getString("title")
                if (completedNum >= needCompleteNum) {
                    logger.i("芝麻粒任务: %s 已完成", title)
                    continue
                }
                joinActivity(templateId)
            }
        }
    }

    private suspend fun joinActivity(templateId: String) {
        AntMemberRpcCall.joinActivity(templateId).onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("参与芝麻粒任务失败: %s", it)
                return@onSuccessCatching
            }
            logger.i("参与芝麻粒任务成功")
        }
    }

    /**
     * 黄金票签到
     */
    private suspend fun goldIndex() {

        AntGoldRpcCall.index().onSuccessCatching {
            if (!it.getBoolean("success")) {
                logger.e("查询金库首页失败: %s", it)
                return@onSuccessCatching
            }
            val result = it.getJSONObject("result")
            val cardModel = result.getJSONArray("cardModel")
            for (i in 0 until cardModel.length()) {
                val card = cardModel.getJSONObject(i)
                val cardTypeId = card.getString("cardTypeId")
                if (cardTypeId != "H5_GOLDBILL_ASSERT") {
                    continue
                }
                val jsonResult = card.getJSONObject("dataModel")
                    .getJSONObject("jsonResult")

                // 签到领黄金票
                if (jsonResult.getBoolean("isTodaySigned")) {
                    return@onSuccessCatching
                }

                val signInBubbleConfig = jsonResult
                    .getJSONObject("fengdie")
                    .getJSONObject("signInBubbleConfig")
                val campId = signInBubbleConfig.getString("campId")
                AntGoldRpcCall.collect(campId)
            }

        }
    }

    private suspend fun merchantQuery() {
        logger.i("商家服务任务执行开始")
        AntMemberRpcCall.merchantQuery().onSuccessCatching {
            if (!it.success) {
                logger.e("查询商家任务列表失败: %s", it)
                return@onSuccessCatching
            }
            if (ConfigManager.otherConfig.enableMerchantSign) {
                // 签到
                merchantSignIn()
            }
            if (ConfigManager.otherConfig.enableMerchantTask) {
                // 任务
                merchantTask()
                merchantTask()
            }
        }
        logger.i("商家服务任务执行完成")
    }

    private suspend fun merchantSignIn() {
        logger.i("商家积分签到查询开始")
        AntMemberRpcCall.merchantSignInQuery().onSuccessCatching {
            if (!it.success) {
                logger.e("商家签到查询失败: %s", it)
                return@onSuccessCatching
            }
            val data = it.getJSONObject("data")
            val signIn = data.getBoolean("signIn")
            if (signIn) {
                logger.i("商家积分签到，今日已经签到")
                return@onSuccessCatching
            }
            val signInDwc = data.getString("signInDwc")
            logger.i("进行商家签到 %s", signInDwc)
            AntMemberRpcCall.merchantSignInExecute().onSuccessCatching SignIn@{ json ->
                if (!json.success) {
                    logger.d("商家积分签到失败，%s", json)
                    return@SignIn
                }
                val data2 = json.getJSONObject("data")
                if (data2.getString("signInResult") == "SUCCESS") {
                    logger.i("商家积分签到成功，获得%s", data2.getInt("todayReward"))
                    return@SignIn
                }
            }
            logger.i("商家签到成功")
        }
    }

    private suspend fun merchantTask() {
        logger.i("商家积分任务查询开始")
        AntMemberRpcCall.merchantTaskListQuery().onSuccessCatching {
            if (!it.success) {
                logger.e("商家积分任务查询失败: %s", it)
                return@onSuccessCatching
            }
            val moduleList = it.getJSONObject("data").getJSONArray("moduleList")
            logger.i("商家积分模块数量：%s", moduleList.length())
            for (i in 0 until moduleList.length()) {
                val module = moduleList.getJSONObject(i)
                val planCode = module.getString("planCode")
                if (planCode == "MORE") {
                    // 更多任务
                    val taskList = module.getJSONArray("taskList")
                    logger.i("商家积分更多任务数量：%s", taskList.length())
                    for (j in 0 until taskList.length()) {
                        val task = taskList.getJSONObject(j)
                        val title = task.getString("title")
                        val rewardValue = task.getInt("rewardValue")
                        val reward = task.getString("reward")
                        val status = task.getString("status")
                        if (status == "NEED_RECEIVE") {
                            val pointBallId = task.optString("pointBallId")
                            // 收取奖励
                            logger.i("商家积分任务 [%s %s] 收取奖励，%s", title, rewardValue, reward)
                            merchantBallReceive(pointBallId)
                        } else if (status == "PROCESSING") {
                            val taskCode = task.getString("taskCode")
                            logger.i("商家积分任务 [%s %s] 待执行", title, reward)
                            // 未执行
                            merchantTaskReceive(taskCode)
                        }
                        delay(2000)
                    }
                }
            }
        }
        delay(5000)
    }

    private suspend fun merchantTaskReceive(taskCode: String) {
        AntMemberRpcCall.merchantTaskReceive(taskCode).onSuccessCatching {
            if (!it.success) {
                logger.e("商家积分任务领取失败: %s", it)
                return@onSuccessCatching
            }
            val receiveResult = it.getJSONObject("data").getBoolean("receiveResult")
            logger.i("商家积分任务领取结果: %s", receiveResult)
        }
    }

    private suspend fun merchantBallReceive(ballId: String) {
        AntMemberRpcCall.merchantBallReceive(listOf(ballId)).onSuccessCatching {
            if (!it.success) {
                logger.e("商家积分领取失败: %s", it)
                return@onSuccessCatching
            }
            val data = it.getJSONObject("data")
            val allBallsFinished = data.getBoolean("allBallsFinished")
            logger.i("商家积分领取结果: %s, 共获得 %s", allBallsFinished, data.getInt("pointReceived"))
        }
    }
}