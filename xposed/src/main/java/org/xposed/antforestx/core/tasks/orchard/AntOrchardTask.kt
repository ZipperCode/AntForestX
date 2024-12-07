package org.xposed.antforestx.core.tasks.orchard

import org.json.JSONObject
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.tasks.ITask
import org.xposed.antforestx.core.util.array
import org.xposed.antforestx.core.util.bool
import org.xposed.antforestx.core.util.int
import org.xposed.antforestx.core.util.isNullOrEmpty
import org.xposed.antforestx.core.util.obj
import org.xposed.antforestx.core.util.onSuccessCatching
import org.xposed.antforestx.core.util.runCatchSuspend
import org.xposed.antforestx.core.util.str
import org.xposed.antforestx.core.util.success
import timber.log.Timber

class AntOrchardTask : ITask {

    private val logger get() = Timber.tag("农场")

    private var stageLevel: Int = 0

    private var userId: String = ""


    override suspend fun start() {
        runCatchSuspend {
            AntOrchardRpcCall.orchardIndex().onSuccessCatching {
                if (!it.success) {
                    logger.d("获取农场信息失败")
                    return@onSuccessCatching
                }
                val userOpenOrchard = it.optBoolean("userOpenOrchard")
                if (!userOpenOrchard) {
                    logger.i("未开启农场")
                    return@onSuccessCatching
                }
                stageLevel = JSONObject(it.getString("taobaoData")).obj("gameInfo").obj("plantInfo").obj("seedStage")
                    .getInt("stageLevel")

                lotteryPlusInfo(it)
                mowGrassInfo()
            }
        }
    }

    private suspend fun lotteryPlusInfo(jsonObject: JSONObject) {
        logger.i("检查七日礼包领取状态 %s", jsonObject.has("lotteryPlusInfo"))
        if (!jsonObject.has("lotteryPlusInfo")) {
            return
        }
        val lotteryPlusInfo = jsonObject.obj("lotteryPlusInfo")
        if (!lotteryPlusInfo.has("userSevenDaysGiftsItem")) {
            return
        }
        runCatchSuspend {
            val item = lotteryPlusInfo.getString("itemId")
            val userEverydayGiftItems = lotteryPlusInfo.obj("userSevenDaysGiftsItem").array("userEverydayGiftItems")
            for (i in 0 until userEverydayGiftItems.length()) {
                val userEverydayGiftItem = userEverydayGiftItems.getJSONObject(i)
                if (userEverydayGiftItem.str("itemId") != item) {
                    continue
                }
                if (userEverydayGiftItem.bool("received")) {
                    logger.i("已领取七日礼包")
                    return@runCatchSuspend
                }
                AntOrchardRpcCall.drawLottery().onSuccessCatching {
                    if (!it.success) {
                        logger.i("领取七日礼包失败，%s", it)
                        return@onSuccessCatching
                    }
                    val userEverydayGiftItems2 = it.obj("lotteryPlusInfo").obj("userSevenDaysGiftsItem").array("userEverydayGiftItems")
                    for (j in 0 until userEverydayGiftItems2.length()) {
                        val userEverydayGiftItem2 = userEverydayGiftItems2.getJSONObject(j)
                        if (userEverydayGiftItem2.str("itemId") != item) {
                            continue
                        }
                        logger.i("领取七日礼包成功, 获得饲料[%sg]", userEverydayGiftItem2.int("awardCount"))
                        return@onSuccessCatching
                    }
                }
            }
        }
    }

    private suspend fun mowGrassInfo() {
        AntOrchardRpcCall.mowGrassInfo().onSuccessCatching {
            if (!it.success) {
                logger.d("获取农场信息失败")
                return@onSuccessCatching
            }
            userId = it.getString("userId")
            if (ConfigManager.manorConfig.batchHireAnimal
                && it.optBoolean("hireCountOnceLimit", true)
                && it.optBoolean("hireCountOneDayLimit", true)
            ) {
                batchHireAnimalRecommend()
            }

        }
    }

    private suspend fun extraGet() {
        AntOrchardRpcCall.extraInfoGet().onSuccessCatching {
            if (!it.success) {
                logger.d("获取额外信息失败")
                return@onSuccessCatching
            }
            val extraInfo = it.obj("data").obj("extraData").obj("fertilizerPacket")
            if (extraInfo.str("todayFertilizerWaitTake") != "status") {
                return@onSuccessCatching
            }
            val num = extraInfo.int("todayFertilizerNum")
        }
    }

    private suspend fun batchHireAnimalRecommend() {
        logger.i("一键捉鸡除草")
        AntOrchardRpcCall.batchHireAnimalRecommend(userId).onSuccessCatching {
            if (!it.success) {
                logger.d("获取批量雇佣动物推荐列表失败")
                return@onSuccessCatching
            }
            val recommendGroupList = it.array("recommendGroupList")
            if (recommendGroupList.isNullOrEmpty()) {
                return@onSuccessCatching
            }
            var batchList = mutableListOf<String>()
            for (i in 0 until recommendGroupList.length()) {
                val recommendGroup = recommendGroupList.getJSONObject(i)
                val animalUserId = recommendGroup.str("animalUserId")
                val earnManureCount = recommendGroup.int("earnManureCount")
                val groupId = recommendGroup.str("groupId")
                val orchardUserId = recommendGroup.str("orchardUserId")
                batchList.add(
                    """
 {"animalUserId": "$animalUserId", "earnManureCount": "$earnManureCount", "groupId": "$groupId", "orchardUserId": "$orchardUserId"}
                    """.trimIndent()
                )
            }
            if (batchList.isEmpty()) {
                return@onSuccessCatching
            }
            if (batchList.size > 5) {
                batchList = batchList.subList(0, 5)
            }
            AntOrchardRpcCall.batchHireAnimal(batchList)
        }
    }

    private suspend fun handleTask() {
        if (!ConfigManager.manorConfig.doOrchardTask) {
            return
        }
        AntOrchardRpcCall.orchardListTask().onSuccessCatching {
            if (!it.success) {
                logger.d("获取任务列表失败")
                return@onSuccessCatching
            }
            if (it.has("signList") && !it.obj("signList").bool("currentKeySigned")) {
                AntOrchardRpcCall.orchardSign().onSuccessCatching { sign ->
                    if (sign.success) {
                        logger.i("农场签到成功")
                    }
                }
            }
            val taskList = it.array("taskList")
            val noActionList = listOf("TRIGGER", "ADD_HOME", "PUSH_SUBSCRIBE")
            for (i in 0 until taskList.length()) {
                val task = taskList.getJSONObject(i)
                if (task.str("taskStatus") == "TODO") {
                    val title = task.obj("taskDisplayConfig").str("title")
                    val actionType = task.str("actionType")
                    val taskId = task.str("taskId")
                    logger.i("开始执行任务[%s]", title)
                    if (!noActionList.contains(actionType)) {
                        if (taskId == "ORCHARD_NORMAL_TAB3") {
                            AntOrchardRpcCall.prizeTrigger(taskId)
                        }
                    }
                    AntOrchardRpcCall.finishTask(userId, task.str("sceneCode"), taskId).onSuccessCatching { taskJson ->
                        if (taskJson.success) {
                            logger.i("完成任务[%s]", title)
                        }
                    }

                }
            }
        }
        AntOrchardRpcCall.orchardListTask().onSuccessCatching {
            if (!it.success) {
                logger.d("获取任务列表失败")
                return@onSuccessCatching
            }
            val taskList = it.array("taskList")
            for (i in 0 until taskList.length()) {
                val task = taskList.getJSONObject(i)
                if (task.str("FINISHED") == "FINISHED") {
                    val title = task.obj("taskDisplayConfig").str("title")
                    val taskPlantType = task.str("taskPlantType")
                    val taskId = task.str("taskId")
                    val awardCount = task.int("awardCount")
                    AntOrchardRpcCall.triggerTbTask(taskId, taskPlantType).onSuccessCatching Task@{ taskJson ->
                        if (!taskJson.success) {
                            logger.d("领取任务奖励失败 %s", it)
                            return@Task
                        }
                        logger.i("领取任务奖励[%s]x%dg肥料", title, awardCount)
                    }
                }
            }
        }
    }

    private suspend fun orchardSpreadManure() {

    }
}