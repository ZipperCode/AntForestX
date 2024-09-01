package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.xposed.antforestx.core.ant.RpcUtil.request
import org.xposed.antforestx.core.util.RandomUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID

object AntFarmRpcCall {
    private const val VERSION = "1.8.2302070202.46"

    suspend fun enterFarm(farmId: String, userId: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.enterFarm",
            "[{\"animalId\":\"\",\"farmId\":\"" + farmId +
                    "\",\"gotoneScene\":\"\",\"gotoneTemplateId\":\"\",\"masterFarmId\":\"\",\"queryLastRecordNum\":true,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ANTFOREST\",\"touchRecordId\":\"\",\"userId\":\""
                    + userId + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun syncAnimalStatus(farmId: String): Result<JsonObject> {
        val args1 = ("[{\"farmId\":\"" + farmId +
                "\",\"operType\":\"FEEDSYNC\",\"queryFoodStockInfo\":false,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"userId\":\""
                + farmId2UserId(farmId) + "\",\"version\":\"" + VERSION + "\"}]")
        return request("com.alipay.antfarm.syncAnimalStatus", args1)
    }

    suspend fun sleep(): Result<JsonObject> {
        val args1 = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"LOVECABIN\",\"version\":\"unknown\"}]"
        return request("com.alipay.antfarm.sleep", args1)
    }

    suspend fun queryLoveCabin(userId: String): Result<JsonObject> {
        val args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ENTERFARM\",\"userId\":\"" +
                userId + "\",\"version\":\"" + VERSION + "\"}]"
        return request("com.alipay.antfarm.queryLoveCabin", args1)
    }

    suspend fun rewardFriend(consistencyKey: String, friendId: String, productNum: String, time: String): Result<JsonObject> {
        val args1 = ("[{\"canMock\":true,\"consistencyKey\":\"" + consistencyKey
                + "\",\"friendId\":\"" + friendId + "\",\"operType\":\"1\",\"productNum\":" + productNum +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"time\":"
                + time + ",\"version\":\"" + VERSION + "\"}]")
        return request("com.alipay.antfarm.rewardFriend", args1)
    }

    suspend fun recallAnimal(animalId: String, currentFarmId: String, masterFarmId: String): Result<JsonObject> {
        val args1 = ("[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.recallAnimal", args1)
    }

    suspend fun orchardRecallAnimal(animalId: String, userId: String): Result<JsonObject> {
        val args1 = "[{\"animalId\":\"" + animalId + "\",\"orchardUserId\":\"" + userId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"zhuangyuan_zhaohuixiaoji\",\"version\":\"0.1.2403061630.6\"}]"
        return request("com.alipay.antorchard.recallAnimal", args1)
    }

    suspend fun sendBackAnimal(sendType: String, animalId: String, currentFarmId: String, masterFarmId: String): Result<JsonObject> {
        val args1 = ("[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"sendType\":\""
                + sendType + "\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.sendBackAnimal", args1)
    }

    suspend fun harvestProduce(farmId: String): Result<JsonObject> {
        val args1 = ("[{\"canMock\":true,\"farmId\":\"" + farmId +
                "\",\"giftType\":\"\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.harvestProduce", args1)
    }

    suspend fun listActivityInfo(): Result<JsonObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.listActivityInfo", args1)
    }

    suspend fun donation(activityId: String, donationAmount: Int): Result<JsonObject> {
        val args1 = ("[{\"activityId\":\"" + activityId + "\",\"donationAmount\":" + donationAmount +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.donation", args1)
    }

    suspend fun listFarmTask(): Result<JsonObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.listFarmTask", args1)
    }

    suspend fun getAnswerInfo(): Result<JsonObject> {
        val args1 =
            ("[{\"answerSource\":\"foodTask\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]")
        return request("com.alipay.antfarm.getAnswerInfo", args1)
    }


    suspend fun answerQuestion(quesId: String, answer: Int): Result<JsonObject> {
        val args1 = ("[{\"answers\":\"[{\\\"questionId\\\":\\\"" + quesId + "\\\",\\\"answers\\\":[" + answer +
                "]}]\",\"bizkey\":\"ANSWER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.doFarmTask", args1)
    }

    suspend fun receiveFarmTaskAward(taskId: String): Result<JsonObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\""
                + taskId + "\",\"version\":\"" + VERSION + "\"}]")
        return request("com.alipay.antfarm.receiveFarmTaskAward", args1)
    }

    suspend fun listToolTaskDetails(): Result<JsonObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.listToolTaskDetails", args1)
    }

    suspend fun receiveToolTaskReward(rewardType: String, rewardCount: Int, taskType: String): Result<JsonObject> {
        val args1 = ("[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"rewardCount\":" + rewardCount
                + ",\"rewardType\":\"" + rewardType + "\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskType\":\""
                + taskType + "\",\"version\":\"" + VERSION + "\"}]")
        return request("com.alipay.antfarm.receiveToolTaskReward", args1)
    }

    suspend fun feedAnimal(farmId: String): Result<JsonObject> {
        val args1 = ("[{\"animalType\":\"CHICK\",\"canMock\":true,\"farmId\":\"" + farmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.feedAnimal", args1)
    }

    suspend fun listFarmTool(): Result<JsonObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.listFarmTool", args1)
    }

    suspend fun useFarmTool(targetFarmId: String, toolId: String, toolType: String): Result<JsonObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"targetFarmId\":\""
                + targetFarmId + "\",\"toolId\":\"" + toolId + "\",\"toolType\":\"" + toolType + "\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.useFarmTool", args1)
    }

    suspend fun rankingList(pageStartSum: Int): Result<JsonObject> {
        val args1 = ("[{\"pageSize\":20,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"startNum\":"
                + pageStartSum + ",\"version\":\"" + VERSION + "\"}]")
        return request("com.alipay.antfarm.rankingList", args1)
    }

    suspend fun notifyFriend(animalId: String, notifiedFarmId: String): Result<JsonObject> {
        val args1 = ("[{\"animalId\":\"" + animalId +
                "\",\"animalType\":\"CHICK\",\"canBeGuest\":true,\"notifiedFarmId\":\"" + notifiedFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.notifyFriend", args1)
    }

    suspend fun feedFriendAnimal(friendFarmId: String): Result<JsonObject> {
        val args1 = ("[{\"animalType\":\"CHICK\",\"canMock\":true,\"friendFarmId\":\"" + friendFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return request("com.alipay.antfarm.feedFriendAnimal", args1)
    }

    fun farmId2UserId(farmId: String): String {
        val l = farmId.length / 2
        return farmId.substring(l)
    }

    suspend fun collectManurePot(manurePotNO: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.collectManurePot", "[{\"manurePotNOs\":\"" + manurePotNO +
                    "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }

    suspend fun sign(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.sign",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }

    suspend fun initFarmGame(gameType: String): Result<JsonObject> {
        if ("flyGame" == gameType) {
            return request(
                "com.alipay.antfarm.initFarmGame",
                "[{\"gameType\":\"flyGame\",\"requestType\":\"RPC\",\"sceneCode\":\"FLAYGAME\"," +
                        "\"source\":\"FARM_game_yundongfly\",\"toolTypes\":\"ACCELERATETOOL,SHARETOOL,NONE\",\"version\":\"\"}]"
            )
        }
        return request(
            "com.alipay.antfarm.initFarmGame",
            "[{\"gameType\":\"" + gameType
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\"}]"
        )
    }

    suspend fun RandomScore(str: String): Int {
        return if ("starGame" == str) {
            RandomUtils.nextInt(200, 300)
        } else if ("jumpGame" == str) {
            RandomUtils.nextInt(150, 170) * 10
        } else if ("flyGame" == str) {
            RandomUtils.nextInt(5000, 8000)
        } else if ("hitGame" == str) {
            RandomUtils.nextInt(60, 100)
        } else {
            210
        }
    }

    suspend fun recordFarmGame(gameType: String): Result<JsonObject> {
        val uuid = uuid
        val md5String = getMD5(uuid)
        val score = RandomScore(gameType)
        if ("flyGame" == gameType) {
            val foodCount = score / 50
            return request(
                "com.alipay.antfarm.recordFarmGame",
                "[{\"foodCount\":" + foodCount + ",\"gameType\":\"flyGame\",\"md5\":\"" + md5String
                        + "\",\"requestType\":\"RPC\",\"sceneCode\":\"FLAYGAME\",\"score\":" + score
                        + ",\"source\":\"ANTFARM\",\"toolTypes\":\"ACCELERATETOOL,SHARETOOL,NONE\",\"uuid\":\"" + uuid
                        + "\",\"version\":\"\"}]"
            )
        }
        return request(
            "com.alipay.antfarm.recordFarmGame",
            "[{\"gameType\":\"" + gameType + "\",\"md5\":\"" + md5String
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"score\":" + score
                    + ",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\",\"uuid\":\"" + uuid
                    + "\"}]"
        )
    }

    private val uuid: String
        get() {
            val sb = StringBuilder()
            for (str in UUID.randomUUID().toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                sb.append(str.substring(str.length / 2))
            }
            return sb.toString()
        }

    fun getMD5(password: String): String {
        try {
            // 得到一个信息摘要器
            val digest = MessageDigest.getInstance("md5")
            val result = digest.digest(password.toByteArray())
            val buffer = StringBuilder()
            // 把没一个byte 做一个与运算 0xff;
            for (b in result) {
                // 与运算
                val number = b.toInt() and 0xff // 加盐
                val str = Integer.toHexString(number)
                if (str.length == 1) {
                    buffer.append("0")
                }
                buffer.append(str)
            }

            // 标准的md5加密后的结果
            return buffer.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }
    }

    /* 小鸡厨房 */
    suspend fun enterKitchen(userId: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.enterKitchen",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\""
                    + userId + "\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun collectDailyFoodMaterial(dailyFoodMaterialAmount: Int): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.collectDailyFoodMaterial",
            "[{\"collectDailyFoodMaterialAmount\":" + dailyFoodMaterialAmount
                    + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun queryFoodMaterialPack(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.queryFoodMaterialPack",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun collectDailyLimitedFoodMaterial(dailyLimitedFoodMaterialAmount: Int): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.collectDailyLimitedFoodMaterial",
            "[{\"collectDailyLimitedFoodMaterialAmount\":" + dailyLimitedFoodMaterialAmount
                    + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun farmFoodMaterialCollect(): Result<JsonObject> {
        return request(
            "com.alipay.antorchard.farmFoodMaterialCollect",
            "[{\"collect\":true,\"requestType\":\"RPC\",\"sceneCode\":\"ORCHARD\",\"source\":\"VILLA\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun cook(userId: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.cook",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\""
                    + userId + "\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun useFarmFood(cookbookId: String, cuisineId: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.useFarmFood",
            "[{\"cookbookId\":\"" + cookbookId + "\",\"cuisineId\":\"" + cuisineId
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"useCuisine\":true,\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun collectKitchenGarbage(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.collectKitchenGarbage",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"VILLA\",\"version\":\"unknown\"}]"
        )
    }

    /* 日常任务 */
    suspend fun doFarmTask(bizKey: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.doFarmTask",
            "[{\"bizKey\":\"" + bizKey
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun queryTabVideoUrl(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.queryTabVideoUrl",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }

    suspend fun videoDeliverModule(bizId: String): Result<JsonObject> {
        return request(
            "alipay.content.reading.life.deliver.module",
            "[{\"bizId\":\"" + bizId
                    + "\",\"bizType\":\"CONTENT\",\"chInfo\":\"ch_antFarm\",\"refer\":\"antFarm\",\"timestamp\":\""
                    + System.currentTimeMillis() + "\"}]"
        )
    }

    suspend fun videoTrigger(bizId: String): Result<JsonObject> {
        return request(
            "alipay.content.reading.life.prize.trigger",
            "[{\"bizId\":\"" + bizId
                    + "\",\"bizType\":\"CONTENT\",\"prizeFlowNum\":\"VIDEO_TASK\",\"prizeType\":\"farmFeed\"}]"
        )
    }

    /* 惊喜礼包 */
    suspend fun drawLotteryPlus(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.drawLotteryPlus",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5 \",\"version\":\"\"}]"
        )
    }

    /* 小麦 */
    suspend fun acceptGift(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.acceptGift",
            "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun visitFriend(friendFarmId: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.visitFriend",
            "[{\"friendFarmId\":\"" + friendFarmId
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    /* 小鸡日记 */
    suspend fun queryChickenDiaryList(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.queryChickenDiaryList",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"DIARY\",\"source\":\"antfarm_icon\"}]"
        )
    }

    suspend fun queryChickenDiary(queryDayStr: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.queryChickenDiary",
            "[{\"queryDayStr\":\"" + queryDayStr
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"DIARY\",\"source\":\"antfarm_icon\"}]"
        )
    }

    suspend fun diaryTietie(diaryDate: String, roleId: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.diaryTietie",
            "[{\"diaryDate\":\"" + diaryDate + "\",\"requestType\":\"NORMAL\",\"roleId\":\"" + roleId
                    + "\",\"sceneCode\":\"DIARY\",\"source\":\"antfarm_icon\"}]"
        )
    }

    suspend fun visitAnimal(): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.visitAnimal",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION +
                    "\"}]"
        )
    }

    suspend fun feedFriendAnimalVisit(friendFarmId: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.feedFriendAnimal",
            "[{\"friendFarmId\":\"" + friendFarmId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\"," +
                    "\"source\":\"visitChicken\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun visitAnimalSendPrize(token: String): Result<JsonObject> {
        return request(
            "com.alipay.antfarm.visitAnimalSendPrize",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"token\":\"" + token +
                    "\",\"version\":\"" + VERSION + "\"}]"
        )
    }
}
