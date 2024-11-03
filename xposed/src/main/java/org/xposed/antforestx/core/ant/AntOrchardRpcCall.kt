package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.requestV2

object AntOrchardRpcCall {
    private const val VERSION = "0.1.2401111000.31"

    suspend fun orchardIndex(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.orchardIndex",
            "[{\"inHomepage\":\"true\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun mowGrassInfo(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.mowGrassInfo",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"showRanking\":true,\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun batchHireAnimalRecommend(orchardUserId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.batchHireAnimalRecommend",
            "[{\"orchardUserId\":\"" + orchardUserId
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"sceneType\":\"weed\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun batchHireAnimal(recommendGroupList: List<String?>?): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.batchHireAnimal",
            "[{\"recommendGroupList\":[" + java.lang.String.join(",", recommendGroupList)
                    + "],\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"sceneType\":\"weed\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun extraInfoGet(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.extraInfoGet",
            "[{\"from\":\"entry\",\"requestType\":\"NORMAL\",\"sceneCode\":\"FUGUO\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun extraInfoSet(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.extraInfoSet",
            "[{\"bizCode\":\"fertilizerPacket\",\"bizParam\":{\"action\":\"queryCollectFertilizerPacket\"},\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun querySubplotsActivity(treeLevel: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.querySubplotsActivity",
            "[{\"activityType\":[\"WISH\",\"BATTLE\",\"HELP_FARMER\",\"DEFOLIATION\",\"CAMP_TAKEOVER\"],\"inHomepage\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"treeLevel\":\""
                    + treeLevel + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun triggerSubplotsActivity(activityId: String, activityType: String, optionKey: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.triggerSubplotsActivity",
            "[{\"activityId\":\"" + activityId + "\",\"activityType\":\"" + activityType
                    + "\",\"optionKey\":\"" + optionKey
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun receiveOrchardRights(activityId: String, activityType: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.receiveOrchardRights",
            "[{\"activityId\":\"" + activityId + "\",\"activityType\":\"" + activityType
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    /* 七日礼包 */
    suspend fun drawLottery(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.drawLottery",
            "[{\"lotteryScene\":\"receiveLotteryPlus\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun orchardSyncIndex(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.orchardSyncIndex",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"syncIndexTypes\":\"QUERY_MAIN_ACCOUNT_INFO\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun orchardSpreadManure(wua: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.orchardSpreadManure",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"useWua\":true,\"version\":\""
                    + VERSION + "\",\"wua\":\"" + wua + "\"}]"
        )
    }

    suspend fun receiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antiep.receiveTaskAward",
            "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"" + sceneCode
                    + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"taskType\":\""
                    + taskType + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun orchardListTask(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.orchardListTask",
            "[{\"plantHiddenMMC\":\"false\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun orchardSign(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.orchardSign",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"signScene\":\"ANTFARM_ORCHARD_SIGN_V2\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun finishTask(userId: String, sceneCode: String, taskType: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antiep.finishTask",
            "[{\"outBizNo\":\"" + userId + System.currentTimeMillis()
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"" + sceneCode
                    + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"taskType\":\""
                    + taskType + "\",\"userId\":\"" + userId + "\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }

    suspend fun triggerTbTask(taskId: String, taskPlantType: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.triggerTbTask",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"taskId\":\""
                    + taskId + "\",\"taskPlantType\":\"" + taskPlantType
                    + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun orchardSelectSeed(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antfarm.orchardSelectSeed",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"seedCode\":\"rp\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    /* 砸金蛋 */
    suspend fun queryGameCenter(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.queryGameCenter",
            "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun noticeGame(appId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.noticeGame",
            "[{\"appId\":\"" + appId
                    + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun submitUserAction(gameId: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.gamecenteruprod.biz.rpc.v3.submitUserAction",
            "[{\"actionCode\":\"enterGame\",\"gameId\":\"" + gameId
                    + "\",\"paladinxVersion\":\"2.0.13\",\"source\":\"gameFramework\"}]"
        )
    }

    suspend fun submitUserPlayDurationAction(gameAppId: String, source: String): Result<JSONObject> {
        return requestV2(
            "com.alipay.gamecenteruprod.biz.rpc.v3.submitUserPlayDurationAction",
            "[{\"gameAppId\":\"" + gameAppId + "\",\"playTime\":32,\"source\":\"" + source
                    + "\",\"statisticTag\":\"\"}]"
        )
    }

    suspend fun smashedGoldenEgg(): Result<JSONObject> {
        return requestV2(
            "com.alipay.antorchard.smashedGoldenEgg",
            "[{\"requestType\":\"NORMAL\",\"seneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION
                    + "\"}]"
        )
    }
}
