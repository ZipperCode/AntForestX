package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.xposed.antforestx.core.ant.AntFarmRpcCall
import org.xposed.antforestx.core.ant.DadaDailyRpcCall
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.DataInfoManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.forEach
import org.xposed.antforestx.core.util.log.enableManor
import org.xposed.antforestx.core.util.log.manor
import org.xposed.antforestx.core.util.onSuccessCatching
import org.xposed.antforestx.core.util.success
import org.zipper.antforestx.data.bean.QuestionData
import timber.log.Timber
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * 蚂蚁庄园
 */
class AntManorTask : ITask {

    private val logger get() = Timber.manor()

    /**
     * 农场id
     */
    private var farmId: String? = null

    // 饲料
    private var foodStock: Int = 0

    // 最大饲料
    private var foodStockLimit = 0

    private var startEatTime = 0L
    private var remainTimeMinutes = 0

    private var farmAnimalSize = 1

    override suspend fun start(): Unit = withContext(Dispatchers.IO + CoroutineName("AntManor")) {
        Timber.enableManor()
        logger.i("开始蚂蚁庄园任务")
        if (!ConfigManager.manorConfig.enable) {
            logger.w("蚂蚁庄园功能未开启")
            return@withContext
        }
        kotlin.runCatching {
            enterFarm()
        }
    }

    private suspend fun enterFarm() {
        val userId = UserManager.waitGetCurrentUid()
        AntFarmRpcCall.enterFarm(userId).onSuccessCatching {
            if (!it.success) {
                logger.i("进入蚂蚁庄园失败")
                return@onSuccessCatching
            }
            val dynamicGlobalConfig = it.getJSONObject("dynamicGlobalConfig")
            val rewardProductNum = dynamicGlobalConfig.getDouble("rewardProductNum")
            val farmVO = it.getJSONObject("farmVO")
            val subFarmVO = farmVO.getJSONObject("subFarmVO")
            this.farmId = subFarmVO.getString("farmId")
            this.foodStock = farmVO.getInt("foodStock")
            this.foodStockLimit = farmVO.getInt("foodStockLimit")
            // 产生的爱心信息
            val farmProduce = farmVO.optJSONObject("farmProduce")
            val benevolenceScore = farmProduce?.getDouble("benevolenceScore") ?: 0.0
            val animals = subFarmVO.optJSONArray("animals")
            val animalList = parseAnimalList(animals)
            animalList.forEach {
                logger.d("animal currFarmId = %s, masterFarmId = %s own = %s", it.currentFarmId, it.masterFarmId, it.isOwner)
            }
            // 打赏好友
            rewardFriend(farmVO, rewardProductNum)
            animalList.firstOrNull { animal -> animal.isOwner }?.let { animal ->
                logger.i("当前状态为%s %s", animal.animalFeedStatus, animal.animalInteractStatus)
                if ("SLEEPY" == animal.animalFeedStatus) {
                    logger.i("睡觉中，不喂食")
                    return@let
                }
                var isAtHome = animal.animalInteractStatus == "HOME"
                if (animal.animalInteractStatus == "GOTOSTEAL") {
                    if (ConfigManager.manorConfig.isRecallChicks) {
                        AntFarmRpcCall.recallAnimal(animal.animalId, animal.currentFarmId, animal.masterFarmId).onSuccessCatching { json ->
                            if (json.success) {
                                isAtHome = true
                                logger.i("召回成功")
                            } else {
                                logger.i("召回失败 %s", it)
                            }
                        }
                    }
                }
                if (isAtHome) {
                    // 食物道具
                    val cuisineList = it.optJSONArray("cuisineList")
                    useFarmFood(cuisineList)
                    // 喂食
                    feedAnimal(animal)
                    // 使用加速卡
                    if (ConfigManager.manorConfig.isUseSpeedCard) {
                        useSpeedProp()
                    }
                }
            }

            // 收便便
            collectManurePot(subFarmVO)
            // 今日礼包
            drawLotteryPlus(it.optJSONObject("lotteryPlusInfo"))
            // 小鸡日历
//            queryChickenDiary()
            // 领饲料
            // 做任务
            listFarmTask()
            happyPrize()
            collectTaskReward()

            collectEgg(subFarmVO)

            // 限时肥料奖励
            it.optJSONObject("orchardRightsInfo")

        }
    }

    private suspend fun useSpeedProp() {
        syncAnimalStatus()
        logger.i("使用加速卡 remainTimeMinutes = %s", remainTimeMinutes)
        if (remainTimeMinutes <= 0) {
            logger.e("获取剩余时间失败")
            return
        }
        AntFarmRpcCall.listFarmTool().onSuccessCatching {
            if (!it.success) {
                logger.i("获取道具失败")
                return@onSuccessCatching
            }
            val toolList = it.getJSONArray("toolList")
            for (i in 0 until toolList.length()) {
                val tool = toolList.getJSONObject(i)
                val toolType = tool.getString("toolType")
                if ("ACCELERATETOOL" != toolType) {
                    continue
                }
                val toolCount = tool.getInt("toolCount")
                val toolHoldLimit = tool.getInt("toolHoldLimit")
                val toolId = tool.optString("toolId")
                val dataMap = tool.getJSONObject("dataMap")
                val accelerateSeconds = dataMap.optInt("accelerateSeconds")
                if (accelerateSeconds > 0 && toolCount > 0) {
                    for (j in 0 until toolCount) {
                        val time = remainTimeMinutes.minutes - 3600.seconds
                        if (time < 1.hours) {
                            logger.i("使用加速卡后，剩余时间不足一小时，停止使用加速卡 %s", time)
                            break
                        }
                        logger.i("使用加速卡后，剩余时间为： %s", time)
                        AntFarmRpcCall.useFarmTool(farmId!!, toolId, toolType).onSuccessCatching { json ->
                            if (json.success) {
                                logger.i("加速卡使用成功，刷新状态")
                                syncAnimalStatus()
                            }
                        }
                        delay(3000)
                    }
                }

            }
        }
    }

    private suspend fun feedAnimal(animal: Animal) {
        if (animal.animalFeedStatus != "HUNGRY") {
            logger.i("当前状态为%s，不喂食", animal.animalFeedStatus)
            return
        }
        syncAnimalStatus()
        if (foodStock < 180) {
            logger.w("当前饲料不足180，不喂食")
            return
        }
        AntFarmRpcCall.feedAnimal(animal.farmId).onSuccessCatching {
            if (!it.success) {
                logger.i("喂食失败")
                return@onSuccessCatching
            }
            foodStock = it.getInt("foodStock")
            logger.i("喂食成功，当前剩余饲料: %s", foodStock)
        }
    }

    private suspend fun collectEgg(subFarmVO: JSONObject) {
        if (!ConfigManager.manorConfig.enableCollectEgg) {
            logger.i("收取爱心蛋功能未开启")
            return
        }

        val benevolenceScore = subFarmVO.optJSONObject("farmProduce")?.getDouble("benevolenceScore") ?: return
        if (benevolenceScore > 1) {
            logger.i("爱心蛋大于1，进行收取 %s", benevolenceScore)
            farmId?.let { farmId ->
                AntFarmRpcCall.harvestProduce(farmId).onSuccessCatching {
                    if (!it.success) {
                        logger.i("收取爱心蛋失败 %s", it)
                        return@onSuccessCatching
                    }
                    val finalBenevolenceScore = it.getDouble("benevolenceScore")
                    logger.i("收取爱心蛋成功，当前爱心蛋总数为: %s", finalBenevolenceScore)
                }
            }
        }
    }

    /**
     * 打赏好友
     */
    private suspend fun rewardFriend(farmVo: JSONObject?, rewardProductNum: Double) = kotlin.runCatching {
        if (farmVo == null) {
            return@runCatching
        }
        if (!ConfigManager.manorConfig.isRewardFriend) {
            logger.i("打赏好友功能未开启")
            return@runCatching
        }
        val subFarmVO = farmVo.getJSONObject("subFarmVO")
        val rewardList = subFarmVO.getJSONArray("rewardList")
        for (i in 0 until rewardList.length()) {
            val rewardVO = rewardList.getJSONObject(i)
            val consistencyKey = rewardVO.getString("consistencyKey")
            val friendId = rewardVO.getString("friendId")
            val time = rewardVO.getLong("time")
            logger.i("打赏好友: %s %s爱心", friendId, rewardProductNum)
            AntFarmRpcCall.rewardFriend(consistencyKey, friendId, rewardProductNum, time).onSuccessCatching {
                if (!it.success) {
                    logger.d("打赏好友失败 %s", it)
                    return@onSuccessCatching
                }
                logger.i("打赏好友%s成功", friendId)
            }
        }
    }

    /**
     * 使用食物道具
     */
    private suspend fun useFarmFood(cuisineList: JSONArray?) = kotlin.runCatching {
        if (cuisineList == null) {
            return@runCatching
        }
        if (!ConfigManager.manorConfig.isUseSpecialFoods) {
            logger.i("使用特殊食物道具功能未开启")
            return@runCatching
        }

        for (i in 0 until cuisineList.length()) {
            val cuisineVO = cuisineList.getJSONObject(i)
            val cookbookId = cuisineVO.getString("cookbookId")
            val count = cuisineVO.getInt("count")
            val cuisineId = cuisineVO.getString("cuisineId")
            val name = cuisineVO.getString("name")

            for (j in 0 until count) {
                logger.i("使用食物道具: %s", name)
                AntFarmRpcCall.useFarmFood(cookbookId, cuisineId).onSuccessCatching {
                    if (!it.success) {
                        logger.i("使用食物道具失败")
                        return@onSuccessCatching
                    }
                    logger.i("使用食物道具[%s]成功", name)
                }
            }
        }
        syncAnimalStatus()
    }

    /**
     * 收便便
     */
    private suspend fun collectManurePot(subFarm: JSONObject?) = kotlin.runCatching {
        if (subFarm == null) {
            return@runCatching
        }
        logger.i("执行收便便")
        val manurePotList = subFarm.getJSONObject("manureVO").getJSONArray("manurePotList")
        for (i in 0 until manurePotList.length()) {
            val manurePotVO = manurePotList.getJSONObject(i)
            val manurePotNum = manurePotVO.getDouble("manurePotNum")
            if (manurePotNum > 100) {
                val manurePotNO = manurePotVO.getString("manurePotNO")
                AntFarmRpcCall.collectManurePot(manurePotNO).onSuccessCatching {
                    if (!it.success) {
                        logger.i("收便便失败")
                        return@onSuccessCatching
                    }
                    val collectManurePotNum = it.getInt("collectManurePotNum")
                    logger.i("收便便成功, 收到%s个便便", collectManurePotNum)
                }
            }
        }


    }

    /**
     * 惊喜礼包
     */
    private suspend fun drawLotteryPlus(lotteryPlusInfo: JSONObject?) {
        if (lotteryPlusInfo == null) {
            return
        }
        logger.i("领取今日礼包")
        kotlin.runCatching {
            if (!lotteryPlusInfo.has("userSevenDaysGiftsItem")) {
                return
            }
            val itemId = lotteryPlusInfo.getString("itemId")
            val userSevenDaysGiftsItem = lotteryPlusInfo.getJSONObject("userSevenDaysGiftsItem")
            val userEverydayGiftItems = userSevenDaysGiftsItem.getJSONArray("userEverydayGiftItems")
            for (i in 0 until userEverydayGiftItems.length()) {
                val userEverydayGiftItem = userEverydayGiftItems.getJSONObject(i)
                if (userEverydayGiftItem.getString("itemId") != itemId) {
                    continue
                }
                val received = userEverydayGiftItem.getBoolean("received")
                if (received) {
                    logger.i("今日礼包已经领取")
                    return
                }
                val singleDesc = userEverydayGiftItem.getString("singleDesc")
                val awardCount = userEverydayGiftItem.getInt("awardCount")
                if (singleDesc.contains("饲料")) {
                    if (awardCount + foodStock > foodStockLimit) {
                        logger.i("领取礼包%s失败，饲料已经上限", singleDesc)
                        return
                    }
                }
                AntFarmRpcCall.drawLotteryPlus().onSuccessCatching {
                    if (!it.success) {
                        logger.i("领取今日礼包[%s]失败", singleDesc)
                        return@onSuccessCatching
                    }
                    logger.i("领取今日礼包成功，获得%s%s", awardCount, singleDesc)
                }
            }
        }
    }

    /**
     * 贴贴小鸡
     */
    private suspend fun queryChickenDiary() {
        AntFarmRpcCall.queryChickenDiaryList().onSuccessCatching {
            if (!it.success) {
                logger.e("贴贴小鸡失败")
                return@onSuccessCatching
            }
            val chickenDiaryBriefList = it.getJSONObject("data").getJSONArray("chickenDiaryBriefList")
            val chickenDiary = it.getJSONObject("data").getJSONObject("chickenDiary")
            val diaryDateStr = chickenDiary.getString("diaryDateStr")
            if (!it.has("hasTietie")) {
                return@onSuccessCatching
            }
            if (it.optBoolean("hasTietie", true)) {
                return@onSuccessCatching
            }
            doTietie(diaryDateStr)
            if (!chickenDiary.has("statisticsList")) {
                return@onSuccessCatching
            }
            val statisticsList = chickenDiary.getJSONArray("statisticsList")
            for (i in 0 until statisticsList.length()) {
                val statisticsVO = statisticsList.getJSONObject(i)
                val tietieRoleId = statisticsVO.getString("tietieRoleId")
                doTietie(diaryDateStr, tietieRoleId)
            }
        }
    }

    private suspend fun doTietie(diaryDateStr: String, roleId: String = "NEW") {
        AntFarmRpcCall.diaryTietie(diaryDateStr, roleId).onSuccessCatching TT@{ json ->
            if (!json.success) {
                logger.i("贴贴小鸡失败")
                return@TT
            }
            val prizeType = json.getString("prizeType")
            val prizeNum = json.getInt("prizeNum")
            logger.i("贴贴小鸡成功[%s_%s]", prizeType, prizeNum)
        }

    }

    private suspend fun listFarmTask() {
        syncAnimalStatus()
        logger.i("执行任务列表-开始签到处理")
        AntFarmRpcCall.listFarmTask().onSuccessCatching {
            if (!it.success) {
                logger.i("获取任务列表失败")
                return@onSuccessCatching
            }
            val signListObject = it.getJSONObject("signList")
            val continuousCount = signListObject.getInt("continuousCount")
            val currentSignKey = signListObject.getString("currentSignKey")
            logger.i("连续签到天数：%s, 当前签到日期 = %s", continuousCount, currentSignKey)
            val signList = signListObject.getJSONArray("signList")
            for (i in 0 until signList.length()) {
                val signVO = signList.getJSONObject(i)
                val signKey = signVO.getString("signKey")
                if (signKey != currentSignKey) {
                    continue
                }
                val awardCount = signVO.getInt("awardCount")
                val signed = signVO.getBoolean("signed")
                if (signed) {
                    logger.i("签到任务已经签到")
                } else if (awardCount <= foodStockLimit - foodStock) {
                    logger.i("饲料未满，可以开始签到")
                    AntFarmRpcCall.sign().onSuccessCatching Sign@{ json ->
                        if (!it.success) {
                            logger.i("签到失败")
                            return@Sign
                        }
                        logger.i("签到成功，获得%s饲料", awardCount)
                    }
                } else {
                    logger.i("饲料已满，无法签到")
                }
                return@onSuccessCatching
            }
        }
        syncAnimalStatus()
        AntFarmRpcCall.listFarmTask().onSuccessCatching {
            if (!it.success) {
                logger.i("获取任务列表失败")
                return@onSuccessCatching
            }
            val farmTaskList = it.getJSONArray("farmTaskList")
            for (i in 0 until farmTaskList.length()) {
                val taskVO = farmTaskList.getJSONObject(i)
                val awardCount = taskVO.getInt("awardCount")
                val title = taskVO.getString("title")
                val taskId = taskVO.getString("taskId")
                val rightsTimes = taskVO.getInt("rightsTimes")
                val rightsTimesLimit = taskVO.getInt("rightsTimesLimit")
                val taskStatus = taskVO.getString("taskStatus")
                val taskMode = taskVO.optString("taskMode")
                val bizKey = taskVO.getString("bizKey")
                if (!taskStatus.isTodo()) {
                    continue
                }
                if (title == "庄园小课堂") {
                    doQuestion(taskId, taskStatus, awardCount)
                } else {
                    val deliveryControlItem = taskVO.optJSONObject("deliveryControlItem")?.optString("iepTaskTracer")
                    val sceneCode = taskVO.optJSONObject("deliveryControlItem")?.optString("sceneCode")
                    if (deliveryControlItem?.contains("sceneCode:ANTFARM_FOOD_TASK") == true) {
                        logger.i("开始执行任务 %s", title)
                        for (j in rightsTimes until rightsTimesLimit) {
                            if (sceneCode.isNullOrEmpty() || taskMode == "VIEW") {
                                doFarmTask(bizKey, title)
                            } else {
                                finishTask(taskId, sceneCode, title)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun collectTaskReward() {
        // 收取任务奖励
        if (foodStock >= foodStockLimit) {
            logger.i("饲料已满，不执行收取任务")
            return
        }
        logger.i("开始收取任务饲料奖励")
        AntFarmRpcCall.listFarmTask().onSuccessCatching {
            if (!it.success) {
                logger.i("获取任务列表失败")
                return@onSuccessCatching
            }
            val farmTaskList = it.getJSONArray("farmTaskList")
            for (i in 0 until farmTaskList.length()) {
                val taskVO = farmTaskList.getJSONObject(i)
                val awardCount = taskVO.getInt("awardCount")
                val taskId = taskVO.getString("taskId")
                val taskStatus = taskVO.getString("taskStatus")
                val canReceiveAwardCount = taskVO.getInt("canReceiveAwardCount")
                if (taskStatus.isReceive()) {
                    logger.d("任务已领取，跳过 %s", taskId)
                    continue
                }
                if (!taskStatus.isFinished()) {
                    logger.d("任务未完成，跳过 %s", taskId)
                    continue
                }
                if (taskVO.optString("awardSubType") == "241101jinyushuijingjiao") {
                    logger.i("金鱼水晶饺任务直接领取")
                    AntFarmRpcCall.receiveFarmTaskAward(taskId)
                    continue
                }
                if (awardCount > foodStockLimit - foodStock) {
                    logger.i("饲料已满，无法领取奖励")
                    break
                }
                logger.i("开始领取任务奖励 %s %s", taskId, awardCount)
                AntFarmRpcCall.receiveFarmTaskAward(taskId).onSuccessCatching Reword@{ json ->
                    if (!json.success) {
                        logger.i("领取任务奖励失败")
                        return@Reword
                    }
                    logger.i("领取任务奖励成功，获得%s饲料", canReceiveAwardCount)
                }
                delay(1000)
                syncAnimalStatus()
            }
        }
    }

    /**
     * 抽抽乐
     */
    private suspend fun happyPrize() {
        logger.i("开始快乐抽抽乐")
        AntFarmRpcCall.enterDrawMachine()
        syncAnimalStatus()
        AntFarmRpcCall.happyPrizeListFarmTask().onSuccessCatching {
            if (!it.success) {
                logger.i("获取抽奖列表失败")
                return@onSuccessCatching
            }
            val farmTaskList = it.getJSONArray("farmTaskList")
            for (i in 0 until farmTaskList.length()) {
                val taskVO = farmTaskList.getJSONObject(i)
                val rightsTimes = taskVO.getInt("rightsTimes")
                val rightsTimesLimit = taskVO.getInt("rightsTimesLimit")
                val taskStatus = taskVO.getString("taskStatus")
                val taskId = taskVO.getString("taskId")
                val title = taskVO.optString("title")
                val bizKey = taskVO.getString("bizKey")
                if (taskStatus.isReceive() || taskStatus.isFinished()) {
                    continue
                }

                if (taskVO.getString("taskMode") == "VIEW" || taskVO.has("innerAction")) {
                    continue
                }

                logger.i("开始执行抽抽乐任务 %s", taskVO.optString("title"))
                for (j in rightsTimes until rightsTimesLimit) {
                    if (taskId == "EXCHANGE_TASK") {
                        // 饲料兑换
                        if (foodStock - 90 < 0) {
                            break
                        }
                        // [{"bizKey":"EXCHANGE_TASK","requestType":"RPC","sceneCode":"ANTFARM","source":"antfarm_villa","taskSceneCode":"ANTFARM_DRAW_TIMES_TASK"}]
                        AntFarmRpcCall.doFarmTask(bizKey, "RPC", "antfarm_villa", "ANTFARM_DRAW_TIMES_TASK")
                            .onSuccessCatching DoFarmTask@{ res ->
                                if (res.success) {
                                    foodStock -= 90
                                }
                            }
                    } else {
                        finishTask(taskId, "ANTFARM_DRAW_TIMES_TASK", title)
                    }
                }
            }
        }
        syncAnimalStatus()
        AntFarmRpcCall.happyPrizeListFarmTask().onSuccessCatching {
            if (!it.success) {
                logger.i("获取抽奖列表失败")
                return@onSuccessCatching
            }
            val farmTaskList = it.getJSONArray("farmTaskList")
            for (i in 0 until farmTaskList.length()) {
                val taskVO = farmTaskList.getJSONObject(i)
                val taskStatus = taskVO.getString("taskStatus")
                val taskId = taskVO.getString("taskId")
                val title = taskVO.optString("title")
                val bizKey = taskVO.getString("bizKey")
                val awardType = taskVO.getString("awardType")
                if (taskStatus.isFinished()) {
                    logger.i("抽抽乐任务已完成，收取奖励 %s 任务", title)
                    // [{"awardType":"DRAW_TIMES","requestType":"RPC","sceneCode":"ANTFARM","source":"antfarm_villa","taskId":"SHANGYEHUA_DRAW_TIMES","taskSceneCode":"ANTFARM_DRAW_TIMES_TASK"}]
                    // [{"awardType":"DRAW_TIMES","requestType":"RPC","sceneCode":"ANTFARM","source":"H5","taskId":"SHANGYEHUA_DRAW_TIMES","version":"1.8.2302070202.46"}]
                    AntFarmRpcCall.receiveFarmTaskAward(taskId, awardType, "RPC", "antfarm_villa", "ANTFARM_DRAW_TIMES_TASK")
                        .onSuccessCatching Reword@{ json ->
                            if (!json.success) {
                                logger.i("领取任务奖励失败 %s", title)
                                return@Reword
                            }
                            logger.i("领取抽抽任务奖励成功 %s", title)
                        }
                    continue
                }
            }
        }

        // 抽奖
        AntFarmRpcCall.enterDrawMachine().onSuccessCatching {
            if (!it.success) {
                logger.i("进入抽奖失败")
                return@onSuccessCatching
            }
            val leftDrawTimes = it.optJSONObject("userInfo")?.optInt("leftDrawTimes") ?: return@onSuccessCatching
            if (leftDrawTimes < 0) {
                return@onSuccessCatching
            }
            val activityId = it.optJSONObject("drawActivityInfo")?.optString("activityId") ?: return@onSuccessCatching
            for (i in 0 until leftDrawTimes) {
                AntFarmRpcCall.drawPrize(activityId).onSuccessCatching drawPrize@{ json ->
                    if (!json.success) {
                        logger.i("抽奖失败, %s", json)
                        return@drawPrize
                    }
                    val title = json.optString("title")
                    logger.i("抽奖成功，获得 %s", title)
                }
                delay(3000)
            }
        }
    }

    private suspend fun receiveOrchardVisitAward() {
        logger.i("执行庄园来访奖励")
        AntFarmRpcCall.receiveOrchardVisitAward().onSuccessCatching {
            if (!it.success) {
                return@onSuccessCatching
            }

//            AntOrchardRpcCall.orchardSpreadManure()
        }
    }

    private suspend fun doQuestion(taskId: String, taskStatus: String, awardCount: Int) {
        if (!ConfigManager.manorConfig.enableAnswerQuestion) {
            logger.i("庄园小课堂任务开关关闭")
            return
        }
        if (taskStatus.isTodo()) {
            logger.i("庄园小课堂任务未完成，开始答题")
            DadaDailyRpcCall.home("100").onSuccessCatching {
                if (!it.success) {
                    return@onSuccessCatching
                }
                val question = it.getJSONObject("question")
                val questionId = question.getLong("questionId")
                val title = question.getString("title")
                val labels = question.getJSONArray("label")
                logger.i("题目: %s, 选项 = %s", title, labels)
                val questionData = DataInfoManager.getQuestionById(questionId.toString())
                if (questionData != null) {
                    logger.i("题目已存在，直接提交答案")
                    DadaDailyRpcCall.submit("100", questionData.answer, questionId)
                    return@onSuccessCatching
                }
                val answer = labels.getString(0)
                val anotherAnswer = labels.getString(1)

                DadaDailyRpcCall.submit("100", answer, questionId).onSuccessCatching Submit@{ json ->
                    if (!json.success) {
                        logger.d("提交答案失败 %s", json)
                        return@Submit
                    }
                    val extInfo = json.getJSONObject("extInfo")
                    val answerResult = extInfo.getString("answer")
                    if (answerResult == answer) {
                        logger.i("提交答案正确，答案：%s", answerResult)
                        DataInfoManager.updateQuestionData(
                            QuestionData(
                                questionId, title, answer
                            )
                        )
                    } else {
                        logger.i("提交答案错误，答案：%s", answerResult)
                        DataInfoManager.updateQuestionData(
                            QuestionData(
                                questionId, title, anotherAnswer
                            )
                        )
                    }
                }

            }
        } else if (taskStatus.isReceive()) {
            syncAnimalStatus()
            if (awardCount > 0 && awardCount < foodStockLimit - foodStock) {
                logger.i("饲料未满，可以领取奖励")
                AntFarmRpcCall.receiveFarmTaskAward(taskId).onSuccessCatching {
                    if (!it.success) {
                        logger.i("领取奖励失败 %s", it)
                        return@onSuccessCatching
                    }
                    logger.i("领取奖励成功，获得%s饲料", awardCount)
                }
            }
            syncAnimalStatus()
        }
    }

    private suspend fun doFarmTask(bizKey: String, title: String) {
        AntFarmRpcCall.doFarmTask(bizKey).onSuccessCatching {
            if (!it.success) {
                logger.i("执行任务失败, %s", it)
                return@onSuccessCatching
            }
            val awardCount = it.getInt("awardCount")
            logger.i("完成任务: %s, 可获取饲料 = %s", title, awardCount)
        }

    }

    private suspend fun finishTask(bizKey: String, sceneCode: String, title: String) {
        AntFarmRpcCall.finishTask(sceneCode, bizKey).onSuccessCatching {
            if (!it.success) {
                logger.i("完成任务奖励失败, %s", it)
                return@onSuccessCatching
            }
            logger.i("完成任务: %s", title)
        }
    }

    private fun parseAnimalList(animals: JSONArray?): List<Animal> {
        if (animals == null) {
            return emptyList()
        }
        val animalList = mutableListOf<Animal>()
        kotlin.runCatching {
            for (i in 0 until animals.length()) {
                val animalVO = animals.getJSONObject(i)
                val animalId = animalVO.getString("animalId")
                val farmId = animalVO.getString("farmId")
                val currentFarmId = animalVO.getString("currentFarmId")
                val masterFarmId = animalVO.getString("masterFarmId")
                val animalBuff = animalVO.getString("animalBuff")
                val subAnimalType = animalVO.getString("subAnimalType")
                val currentFarmMasterUserId = animalVO.getString("currentFarmMasterUserId")
                val locationType = animalVO.optString("locationType")
                val animalStatusVO = animalVO.getJSONObject("animalStatusVO")
                val animalFeedStatus = animalStatusVO.getString("animalFeedStatus")
                val animalInteractStatus = animalStatusVO.getString("animalInteractStatus")
                val animal = Animal(
                    animalId = animalId,
                    farmId = farmId,
                    currentFarmId = currentFarmId,
                    masterFarmId = masterFarmId,
                    animalBuff = animalBuff,
                    subAnimalType = subAnimalType,
                    currentFarmMasterUserId = currentFarmMasterUserId,
                    locationType = locationType,
                    animalFeedStatus = animalFeedStatus,
                    animalInteractStatus = animalInteractStatus,
                    farmId == masterFarmId
                )
                animalList.add(animal)
            }
        }

        return animalList
    }

    private suspend fun syncAnimalStatus() {
        if (farmId.isNullOrEmpty()) {
            return
        }
        AntFarmRpcCall.syncAnimalStatus(farmId!!).onSuccessCatching {
            if (!it.success) {
                logger.i("同步动物状态失败")
                return@onSuccessCatching
            }
            val subFarmVO = it.getJSONObject("subFarmVO")
            foodStock = subFarmVO.optInt("foodStock")
            foodStockLimit = subFarmVO.optInt("foodStockLimit", 180)
            logger.i("当前饲料 = %s/%s", foodStock, foodStockLimit)
            val animals = subFarmVO.optJSONArray("animals")
            farmAnimalSize = animals?.length() ?: 1
            animals?.forEach { animal ->
                if (animal.optString("farmId") == animal.optString("masterFarmId")) {
                    val animalStatusVO = animal.optJSONObject("animalStatusVO")
                    val animalFeedStatus = animalStatusVO?.optString("animalFeedStatus")
                    logger.i("刷新#animalFeedStatus = %s", animalFeedStatus)
                    if (animalFeedStatus != "EATING") {
                        logger.i("当前动物没有在吃")
                        startEatTime = 0
                        remainTimeMinutes = 0
                        return@forEach
                    }
                    startEatTime = animal.optLong("startEatTime")
                    logger.d("startEatTime = %s", startEatTime)
                    val startDuration = startEatTime.milliseconds
                    val endDuration = startEatTime.milliseconds + 4.hours
                    val currentDuration = System.currentTimeMillis().milliseconds
                    val remainTime = endDuration - System.currentTimeMillis().milliseconds
                    remainTimeMinutes = remainTime.inWholeMinutes.toInt()
                    logger.i("当前动物正在吃，已持续 %s, 剩余 %s 分钟", currentDuration - startDuration, remainTimeMinutes)
                }
            }
        }
        delay(500)
    }

    data class Animal(
        val animalId: String,
        val farmId: String,
        val currentFarmId: String,
        val masterFarmId: String,
        val animalBuff: String,
        val subAnimalType: String,
        val currentFarmMasterUserId: String,
        val locationType: String,
        val animalFeedStatus: String,
        val animalInteractStatus: String,
        val isOwner: Boolean = false
    )

    private fun String.isTodo(): Boolean {
        return this == "TODO"
    }

    private fun String.isFinished(): Boolean {
        return this == "FINISHED"
    }

    private fun String.isReceive(): Boolean {
        return this == "RECEIVED"
    }
}