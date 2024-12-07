package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.requestV2
import org.xposed.antforestx.core.util.RandomUtils
import org.xposed.antforestx.core.util.toListJson
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID

object AntFarmRpcCall {
    private const val VERSION = "1.8.2302070202.46"

    /**
     * 进入农场
     */
    suspend fun enterFarm(userId: String): Result<JSONObject> {
        // [{"animalId":"","bizCode":"","gotoneScene":"","gotoneTemplateId":"","groupId":"","growthExtInfo":"","inviteUserId":"","masterFarmId":"","queryLastRecordNum":true,"recall":false,"requestType":"NORMAL","sceneCode":"ANTFARM","shareId":"","source":"ANTFOREST","starFarmId":"","subBizCode":"","touchRecordId":"","userId":"2088642929566522","userToken":"","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "animalId" to "",
            "bizCode" to "",
            "gotoneScene" to "",
            "gotoneTemplateId" to "",
            "groupId" to "",
            "growthExtInfo" to "",
            "inviteUserId" to "",
            "masterFarmId" to "",
            "queryLastRecordNum" to true,
            "recall" to false,
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "shareId" to "",
            "source" to "ANTFOREST",
            "starFarmId" to "",
            "subBizCode" to "",
            "touchRecordId" to "",
            "userId" to userId,
            "version" to VERSION
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antfarm.enterFarm", json)
    }

    /**
     * 同步状态
     */
    suspend fun syncAnimalStatus(farmId: String): Result<JSONObject> {
        // [{"farmId":"10231009082740012088642929566522","operTag":"SYNC_MOOD_AFTER_FEED_XSNACKS","operType":"QUERY_EMOTION_INFO","requestType":"NORMAL","sceneCode":"ANTFARM","source":"ANTFOREST","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "farmId" to farmId,
            "operTag" to "SYNC_RESUME_ORCHARD",
            "operType" to "QUERY_USER_INFO|QUERY_FARM_INFO|QUERY_ORCHARD_RIGHTS",
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "ANTFOREST",
            "version" to VERSION
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antfarm.syncAnimalStatus", json)
    }

    suspend fun sleep(): Result<JSONObject> {
        val args1 = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"LOVECABIN\",\"version\":\"unknown\"}]"
        return requestV2("com.alipay.antfarm.sleep", args1)
    }

    suspend fun queryLoveCabin(userId: String): Result<JSONObject> {
        val args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ENTERFARM\",\"userId\":\"" +
                userId + "\",\"version\":\"" + VERSION + "\"}]"
        return requestV2("com.alipay.antfarm.queryLoveCabin", args1)
    }

    suspend fun rewardFriend(consistencyKey: String, friendId: String, productNum: Double, time: Long): Result<JSONObject> {
        // [{"consistencyKey":"208811222616847020886429295665221d4796aa787d4b30bd0a71c4ea772f17","friendId":"2088112226168470","operType":"1","productNum":0.05,"requestType":"NORMAL","sceneCode":"ANTFARM","source":"ANTFOREST","time":1726880112000,"version":"1.8.2302070202.46"}]
        val json = mapOf(
            "consistencyKey" to consistencyKey,
            "friendId" to friendId,
            "operType" to "1",
            "productNum" to productNum,
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "ANTFOREST",
            "time" to time,
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.rewardFriend", json)
    }

    /**
     * 召回小鸡
     */
    suspend fun recallAnimal(animalId: String, currentFarmId: String, masterFarmId: String): Result<JSONObject> {
        // [{"animalId":"20170825231537012088112226168470","currentFarmId":"10180208115419012088602328475548","masterFarmId":"10170825231537012088112226168470","requestType":"NORMAL","sceneCode":"ANTFARM","source":"H5","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "animalId" to animalId,
            "currentFarmId" to currentFarmId,
            "masterFarmId" to masterFarmId,
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "H5",
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.recallAnimal", json)
    }

    suspend fun orchardRecallAnimal(animalId: String, userId: String): Result<JSONObject> {
        val args1 = "[{\"animalId\":\"" + animalId + "\",\"orchardUserId\":\"" + userId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"zhuangyuan_zhaohuixiaoji\",\"version\":\"0.1.2403061630.6\"}]"
        return requestV2("com.alipay.antorchard.recallAnimal", args1)
    }

    suspend fun sendBackAnimal(sendType: String, animalId: String, currentFarmId: String, masterFarmId: String): Result<JSONObject> {
        val args1 = ("[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"sendType\":\""
                + sendType + "\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.sendBackAnimal", args1)
    }

    suspend fun harvestProduce(farmId: String): Result<JSONObject> {
        // [{"farmId":"10170825231537012088112226168470","harvestType":"NORMALEGG","requestType":"NORMAL","sceneCode":"ANTFARM","source":"antfarm","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "farmId" to farmId,
            "harvestType" to "NORMALEGG",
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "antfarm",
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.harvestProduce", json)
    }

    suspend fun listActivityInfo(): Result<JSONObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.listActivityInfo", args1)
    }

    suspend fun donation(activityId: String, donationAmount: Int): Result<JSONObject> {
        val args1 = ("[{\"activityId\":\"" + activityId + "\",\"donationAmount\":" + donationAmount +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.donation", args1)
    }

    suspend fun listFarmTask(): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ANTFARM","source":"ANTFOREST","topTask":"","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "ANTFOREST",
            "topTask" to "",
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.listFarmTask", json)
    }

    suspend fun finishTask(sceneCode: String, taskType: String): Result<JSONObject> {
        // [{"outBizNo":"SHANGYEHUA_90_1_1727765943630_8bc4f562","requestType":"RPC","sceneCode":"ANTFARM_FOOD_TASK","source":"ADBASICLIB","taskType":"SHANGYEHUA_90_1"}]
        val outBizNo = taskType + "_" + System.currentTimeMillis() + "_" + RandomUtils.getRandom(8)
        val json = mapOf(
            "outBizNo" to outBizNo,
            "requestType" to "RPC",
            "sceneCode" to sceneCode,
            "source" to "ADBASICLIB",
            "taskType" to taskType
        ).toListJson()
        return requestV2("com.alipay.antiep.finishTask", json)
    }

    suspend fun dadaDailyHome(): Result<JSONObject> {
        // [{"activityId":100,"dadaVersion":"1.3.0","version":1}]
        val json = mapOf(
            "activityId" to 100,
            "dadaVersion" to "1.3.0",
            "version" to 1
        ).toListJson()

        return requestV2("com.alipay.reading.game.dadaDaily.home", json)
    }

    suspend fun getAnswerInfo(): Result<JSONObject> {
        val args1 =
            ("[{\"answerSource\":\"foodTask\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.getAnswerInfo", args1)
    }


    suspend fun answerQuestion(quesId: String, answer: Int): Result<JSONObject> {
        val args1 = ("[{\"answers\":\"[{\\\"questionId\\\":\\\"" + quesId + "\\\",\\\"answers\\\":[" + answer +
                "]}]\",\"bizkey\":\"ANSWER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.doFarmTask", args1)
    }

    suspend fun receiveFarmTaskAward(
        taskId: String,
        awardType: String = "ALLPURPOSE",
        requestType: String = "NORMAL",
        source: String = "H5",
        taskSceneCode: String? = null
    ): Result<JSONObject> {
        // [{"awardType":"ALLPURPOSE","requestType":"NORMAL","sceneCode":"ANTFARM","source":"H5","taskId":"30001935487934202088112226168470","version":"1.8.2302070202.46"}]
        val json = mutableMapOf(
            "awardType" to awardType,
            "requestType" to requestType,
            "sceneCode" to "ANTFARM",
            "source" to source,
            "taskId" to taskId,
            "version" to VERSION
        )
        if (!taskSceneCode.isNullOrEmpty()) {
            json["taskSceneCode"] = taskSceneCode
        }
        return requestV2("com.alipay.antfarm.receiveFarmTaskAward", json.toListJson())
    }

    suspend fun listToolTaskDetails(): Result<JSONObject> {
        val args1 = ("[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.listToolTaskDetails", args1)
    }

    suspend fun receiveToolTaskReward(rewardType: String, rewardCount: Int, taskType: String): Result<JSONObject> {
        val args1 = ("[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"rewardCount\":" + rewardCount
                + ",\"rewardType\":\"" + rewardType + "\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskType\":\""
                + taskType + "\",\"version\":\"" + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.receiveToolTaskReward", args1)
    }

    suspend fun feedAnimal(farmId: String): Result<JSONObject> {
        // [{"animalType":"CHICK","canMock":true,"farmId":"10170825231537012088112226168470","requestType":"NORMAL","sceneCode":"ANTFARM","source":"chInfo_ch_appcenter__chsub_9patch","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "animalType" to "CHICK",
            "canMock" to true,
            "farmId" to farmId,
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "H5",
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.feedAnimal", json)
    }

    suspend fun listFarmTool(): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ANTFARM","source":"H5","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "H5",
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.listFarmTool", json)
    }

    suspend fun useFarmTool(targetFarmId: String, toolId: String, toolType: String): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ANTFARM","source":"H5","targetFarmId":"10170825231537012088112226168470","toolId":"17288138843391","toolType":"ACCELERATETOOL","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "H5",
            "targetFarmId" to targetFarmId,
            "toolId" to toolId,
            "toolType" to toolType,
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.useFarmTool", json)
    }

    suspend fun rankingList(pageStartSum: Int): Result<JSONObject> {
        val args1 = ("[{\"pageSize\":20,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"startNum\":"
                + pageStartSum + ",\"version\":\"" + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.rankingList", args1)
    }

    suspend fun notifyFriend(animalId: String, notifiedFarmId: String): Result<JSONObject> {
        val args1 = ("[{\"animalId\":\"" + animalId +
                "\",\"animalType\":\"CHICK\",\"canBeGuest\":true,\"notifiedFarmId\":\"" + notifiedFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.notifyFriend", args1)
    }

    suspend fun feedFriendAnimal(friendFarmId: String): Result<JSONObject> {
        val args1 = ("[{\"animalType\":\"CHICK\",\"canMock\":true,\"friendFarmId\":\"" + friendFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]")
        return requestV2("com.alipay.antfarm.feedFriendAnimal", args1)
    }

    fun farmId2UserId(farmId: String): String {
        val l = farmId.length / 2
        return farmId.substring(l)
    }

    /**
     * 收集便便
     *@param manurePotNO 1 | 1,2,3
     */
    suspend fun collectManurePot(manurePotNO: String): Result<JSONObject> {
        // [{"isSkipTempLimit":false,"manurePotNOs":"1","requestType":"NORMAL","sceneCode":"ANTFARM","source":"ANTFOREST","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "isSkipTempLimit" to false,
            "manurePotNOs" to manurePotNO,
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "ANTFOREST",
            "version" to VERSION
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antfarm.collectManurePot", json)
    }

    suspend fun sign(): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ANTFARM","source":"H5","version":"1.8.2302070202.46"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "H5",
            "version" to VERSION
        ).toListJson()
        return requestV2("com.alipay.antfarm.sign", json)
    }

    suspend fun initFarmGame(gameType: String): Result<JSONObject> {
        if ("flyGame" == gameType) {
            return requestV2(
                "com.alipay.antfarm.initFarmGame",
                "[{\"gameType\":\"flyGame\",\"requestType\":\"RPC\",\"sceneCode\":\"FLAYGAME\"," +
                        "\"source\":\"FARM_game_yundongfly\",\"toolTypes\":\"ACCELERATETOOL,SHARETOOL,NONE\",\"version\":\"\"}]"
            )
        }
        return requestV2(
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

    suspend fun recordFarmGame(gameType: String): Result<JSONObject> {
        val uuid = uuid
        val md5String = getMD5(uuid)
        val score = RandomScore(gameType)
        if ("flyGame" == gameType) {
            val foodCount = score / 50
            return requestV2(
                "com.alipay.antfarm.recordFarmGame",
                "[{\"foodCount\":" + foodCount + ",\"gameType\":\"flyGame\",\"md5\":\"" + md5String
                        + "\",\"requestType\":\"RPC\",\"sceneCode\":\"FLAYGAME\",\"score\":" + score
                        + ",\"source\":\"ANTFARM\",\"toolTypes\":\"ACCELERATETOOL,SHARETOOL,NONE\",\"uuid\":\"" + uuid
                        + "\",\"version\":\"\"}]"
            )
        }
        return requestV2(
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
    suspend fun enterKitchen(userId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.enterKitchen",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\""
                    + userId + "\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun collectDailyFoodMaterial(dailyFoodMaterialAmount: Int): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.collectDailyFoodMaterial",
            "[{\"collectDailyFoodMaterialAmount\":" + dailyFoodMaterialAmount
                    + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun queryFoodMaterialPack(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.queryFoodMaterialPack",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun collectDailyLimitedFoodMaterial(dailyLimitedFoodMaterialAmount: Int): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.collectDailyLimitedFoodMaterial",
            "[{\"collectDailyLimitedFoodMaterialAmount\":" + dailyLimitedFoodMaterialAmount
                    + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun farmFoodMaterialCollect(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.farmFoodMaterialCollect",
            "[{\"collect\":true,\"requestType\":\"RPC\",\"sceneCode\":\"ORCHARD\",\"source\":\"VILLA\",\"version\":\"unknown\"}]"
        )
    }

    suspend fun cook(userId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.cook",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\""
                    + userId + "\",\"version\":\"unknown\"}]"
        )
    }

    /**
     * 使用食物道具
     */
    suspend fun useFarmFood(cookbookId: String, cuisineId: String): Result<JSONObject> {
        // [{"cookbookId":"240901renjianyouqiuwei","cuisineId":"240901huahaoyueyuanbing","requestType":"NORMAL","sceneCode":"ANTFARM","source":"ANTFOREST","useCuisine":true,"version":"1.8.2302070202.46"}]
        val json = mapOf(
            "cookbookId" to cookbookId,
            "cuisineId" to cuisineId,
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "useCuisine" to true,
            "version" to VERSION
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antfarm.useFarmFood", json)
    }

    suspend fun collectKitchenGarbage(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.collectKitchenGarbage",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"VILLA\",\"version\":\"unknown\"}]"
        )
    }

    /* 日常任务 */
    suspend fun doFarmTask(bizKey: String, requestType: String = "NORMAL", source: String = "H5", taskSceneCode: String? = null): Result<JSONObject> {
        // [{"bizKey":"ANTMEMBER_RICHANGQIANDAO","requestType":"NORMAL","sceneCode":"ANTFARM","source":"H5","version":"1.8.2302070202.46"}]
        val json = mutableMapOf(
            "bizKey" to bizKey,
            "requestType" to requestType,
            "sceneCode" to "ANTFARM",
            "source" to source,
            "version" to VERSION
        )
        if (!taskSceneCode.isNullOrEmpty()) {
            json["taskSceneCode"] = taskSceneCode
        }

        return requestV2("com.alipay.antfarm.doFarmTask",json.toListJson())
    }

    suspend fun queryTabVideoUrl(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.queryTabVideoUrl",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }

    suspend fun videoDeliverModule(bizId: String): Result<JSONObject> {
        return requestV2(
            "alipay.content.reading.life.deliver.module",
            "[{\"bizId\":\"" + bizId
                    + "\",\"bizType\":\"CONTENT\",\"chInfo\":\"ch_antFarm\",\"refer\":\"antFarm\",\"timestamp\":\""
                    + System.currentTimeMillis() + "\"}]"
        )
    }

    suspend fun videoTrigger(bizId: String): Result<JSONObject> {
        return requestV2(
            "alipay.content.reading.life.prize.trigger",
            "[{\"bizId\":\"" + bizId
                    + "\",\"bizType\":\"CONTENT\",\"prizeFlowNum\":\"VIDEO_TASK\",\"prizeType\":\"farmFeed\"}]"
        )
    }

    /* 惊喜礼包 */
    suspend fun drawLotteryPlus(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.drawLotteryPlus",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5 \",\"version\":\"\"}]"
        )
    }

    /* 小麦 */
    suspend fun acceptGift(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.acceptGift",
            "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun visitFriend(friendFarmId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.visitFriend",
            "[{\"friendFarmId\":\"" + friendFarmId
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    /* 小鸡日记 */
    suspend fun queryChickenDiaryList(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antfarm.queryChickenDiaryList",
            """[{"queryDayStr":"","requestType":"NORMAL","sceneCode":"DIARY","source":"antfarm_chicken_bubble"}]"""
        )
    }

    suspend fun queryChickenDiary(queryDayStr: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.queryChickenDiary",
            "[{\"queryDayStr\":\"" + queryDayStr
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"DIARY\",\"source\":\"antfarm_icon\"}]"
        )
    }

    suspend fun diaryTietie(diaryDate: String, roleId: String): Result<JSONObject> {
        // [{"diaryDate":"2024-09-21","requestType":"NORMAL","roleId":"NEW","sceneCode":"DIARY","source":"antfarm_chicken_bubble"}]
        val json = mapOf(
            "diaryDate" to diaryDate,
            "requestType" to "NORMAL",
            "roleId" to roleId,
            "sceneCode" to "DIARY",
            "source" to "antfarm_chicken_bubble"
        ).toListJson()
        return requestV2("com.alipay.antfarm.diaryTietie", json)
    }

    suspend fun visitAnimal(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.visitAnimal",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION +
                    "\"}]"
        )
    }

    suspend fun feedFriendAnimalVisit(friendFarmId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.feedFriendAnimal",
            "[{\"friendFarmId\":\"" + friendFarmId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\"," +
                    "\"source\":\"visitChicken\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun visitAnimalSendPrize(token: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.visitAnimalSendPrize",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"token\":\"" + token +
                    "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun enterDrawMachine(): Result<JSONObject> {
        // [{"requestType":"RPC","sceneCode":"ANTFARM","source":"antfarm_villa"}]
        val json = mapOf(
            "requestType" to "RPC",
            "sceneCode" to "ANTFARM",
            "source" to "antfarm_villa"
        ).toListJson()
        return requestV2("com.alipay.antfarm.enterDrawMachine", json)
    }

    suspend fun happyPrizeListFarmTask(): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ANTFARM","signSceneCode":"","source":"H5","taskSceneCode":"ANTFARM_DRAW_TIMES_TASK","topTask":""}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ANTFARM",
            "signSceneCode" to "",
            "source" to "H5",
            "taskSceneCode" to "ANTFARM_DRAW_TIMES_TASK",
            "topTask" to ""
        ).toListJson()

        return requestV2("com.alipay.antfarm.listFarmTask", json)
    }

    suspend fun drawPrize(activityId: String): Result<JSONObject> {
        // [{"activityId":"CP142434674","requestType":"RPC","sceneCode":"ANTFARM","source":"antfarm_villa"}]
        val json = mapOf(
            "activityId" to activityId,
            "requestType" to "RPC",
            "sceneCode" to "ANTFARM",
            "source" to "antfarm_villa"
        ).toListJson()
        return requestV2("com.alipay.antfarm.DrawPrize", json)
    }

    /**
     * 庄园来访
     */
    suspend fun receiveOrchardVisitAward(): Result<JSONObject> {
        // [{"diversionSource":"antfarm","requestType":"NORMAL","sceneCode":"ORCHARD","source":"ANTFARM_ORCHARD_PLUS","version":"v2"}]
        val json = mapOf(
            "diversionSource" to "antfarm",
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ANTFARM_ORCHARD_PLUS",
            "version" to "v2"
        )

        return requestV2("com.alipay.antorchard.receiveOrchardVisitAward", json.toListJson())
    }
}
