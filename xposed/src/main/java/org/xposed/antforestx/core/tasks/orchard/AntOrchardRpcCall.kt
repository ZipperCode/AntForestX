package org.xposed.antforestx.core.tasks.orchard

import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.requestV2
import org.xposed.antforestx.core.util.toListJson

object AntOrchardRpcCall {
    private const val VERSION = "0.1.2407011521.59"

    /**
     * 果园首页
     */
    suspend fun orchardIndex(): Result<JSONObject> {
        // [{\"inHomepage\":\"true\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\"0.1.2407011521.59\"}]
        val map = mapOf(
            "inHomepage" to "true",
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ch_appcenter__chsub_9patch",
            "version" to VERSION
        )
        return requestV2("com.alipay.antfarm.orchardIndex", map.toListJson())
    }

    suspend fun mowGrassInfo(): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ORCHARD","showRanking":false,"source":"ANTFARM_ORCHARD_PLUS","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "showRanking" to false,
            "source" to "ANTFARM_ORCHARD_PLUS",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.mowGrassInfo", json.toListJson())
    }

    /**
     * 动物显示信息
     */
    suspend fun queryAnimalShowInfo(userId: String): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ORCHARD","source":"ANTORCHARD","userId":"2088642929566522","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ANTORCHARD",
            "userId" to userId,
            "version" to VERSION
        )
        return requestV2("com.alipay.antfarm.queryAnimalShowInfo", json.toListJson())
    }

    suspend fun batchHireAnimalRecommend(orchardUserId: String): Result<JSONObject> {
        val map = mapOf(
            "orchardUserId" to orchardUserId,
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "sceneType" to "weed",
            "source" to "ch_appcenter__chsub_9patch",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.batchHireAnimalRecommend", map.toListJson())
    }

    suspend fun batchHireAnimal(recommendGroupList: List<String>): Result<JSONObject> {
        val map = mapOf(
            "recommendGroupList" to recommendGroupList.joinToString(","),
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "sceneType" to "weed",
            "source" to "ch_appcenter__chsub_9patch",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.batchHireAnimal", map.toListJson())
    }

    /**
     * 额外信息
     */
    suspend fun extraInfoGet(): Result<JSONObject> {
        // [{"from":"entry","requestType":"NORMAL","sceneCode":"FUGUO","source":"ANTFARM_ORCHARD_PLUS","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "from" to "entry",
            "requestType" to "NORMAL",
            "sceneCode" to "FUGUO",
            "source" to "ANTFARM_ORCHARD_PLUS",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.extraInfoGet", json.toListJson())
    }

    suspend fun extraInfoSet(): Result<JSONObject> {
        val map = mapOf(
            "bizCode" to "fertilizerPacket",
            "bizParam" to mapOf("action" to "queryCollectFertilizerPacket"),
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ch_appcenter__chsub_9patch",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.extraInfoSet", map.toListJson())
    }

    suspend fun refinedOperation(): Result<JSONObject> {
        // [{"actionId":"ENTERORCHARD","inHomepage":"false","requestType":"NORMAL","sceneCode":"ORCHARD","source":"ANTFARM_ORCHARD_PLUS","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "actionId" to "ENTERORCHARD",
            "inHomepage" to "false",
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ANTFARM_ORCHARD_PLUS",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.refinedOperation", json.toListJson())
    }

    suspend fun querySubplotsActivity(treeLevel: String): Result<JSONObject> {
        // [{"activityType":["WISH","BATTLE","HELP_FARMER","DEFOLIATION","CAMP_TAKEOVER","LIMITED_TIME_CHALLENGE"],"inHomepage":false,"requestType":"NORMAL","sceneCode":"ORCHARD","source":"ANTFARM_ORCHARD_PLUS","treeLevel":"11","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "activityType" to listOf("WISH", "BATTLE", "HELP_FARMER", "DEFOLIATION", "CAMP_TAKEOVER"),
            "inHomepage" to false,
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ANTFARM_ORCHARD_PLUS",
            "treeLevel" to treeLevel,
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.querySubplotsActivity", json.toListJson())
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

    suspend fun triggerSubplotsActivity(activityId: String, activityType: String, optionKey: String): Result<JSONObject> {
        val json = mapOf(
            "activityId" to activityId,
            "activityType" to activityType,
            "optionKey" to optionKey,
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ch_appcenter__chsub_9patch",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.triggerSubplotsActivity", json.toListJson())
    }

    suspend fun receiveOrchardRights(activityId: String, activityType: String): Result<JSONObject> {
        val map = mapOf(
            "activityId" to activityId,
            "activityType" to activityType,
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ch_appcenter__chsub_9patch",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.receiveOrchardRights", map.toListJson())
    }

    /**
     * 七日礼包
     */
    suspend fun drawLottery(): Result<JSONObject> {
        val map = mapOf(
            "lotteryScene" to "receiveLotteryPlus",
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ch_appcenter__chsub_9patch",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.drawLottery", map.toListJson())
    }

    suspend fun orchardSyncIndex(): Result<JSONObject> {
        val map = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ch_appcenter__chsub_9patch",
            "syncIndexTypes" to "QUERY_MAIN_ACCOUNT_INFO",
            "version" to VERSION
        )
        return requestV2("com.alipay.antorchard.orchardSyncIndex", map.toListJson())
    }

    /**
     * 果园施肥
     */
    suspend fun orchardSpreadManure(wua: String): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ORCHARD","source":"ANTFARM_ORCHARD_PLUS","useBatchSpread":true,"useWua":true,"version":"0.1.2408191557.23","wua":"q8Q1_Yqr8NZrp2z9eyDnhj7pEasokmVOTSIZ7r3YSC5UyhzWfnTPAtLc3m+/mnQQvEyb5MoGw0yhbjYYRuuJb/94PSneWKI+7l0KblwCoJ9/NahErOHHUPIYOFrBK0SU6cWRTDLQmxBbBj3clpnqHvK7+Bl1A+pAYrsmYtScQNmN6iz6HJWV6NeJK4OxRnFQt/2Uuqsru6rkVAtEu+WqbR16ofbYGfz/tTU24FxScYdKzmHuF8Yvq2b39JJUMSyvQ+WKVPrWq4BnC9E5QAna1ovNzYJlD16x8y2olOZ0gHTvW3DrtdHVg0ceaP5lU/x6u3U3eS8E+wQk3ctFtkACx/tZKGB74ALOjuxIOAqPGPGdyfM/KSV3Nsff39PXTsovVGT1v"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ANTFARM_ORCHARD_PLUS",
            "useBatchSpread" to true,
            "useWua" to true,
            "version" to VERSION,
            "wua" to wua
        )

        return requestV2(
            "com.alipay.antfarm.orchardSpreadManure",
            json.toListJson()
        )
    }

    /**
     * 收取任务奖励
     * {"code":"100000000","desc":"处理成功","incAwardCount":600,"provideRightsSuccess":true,"returnData":"{\"PROVIDE_RIGHTS_NEW\":\"\\\"{\\\\\\\"taskType\\\\\\\":\\\\\\\"OSMA_GROUP_19_STEP_1\\\\\\\",\\\\\\\"addManureReason\\\\\\\":\\\\\\\"alipay_spread_manure_activity_add\\\\\\\",\\\\\\\"uniqueId\\\\\\\":\\\\\\\"antfarm\\\\\\\",\\\\\\\"awardType\\\\\\\":\\\\\\\"orchardManure\\\\\\\"}\\\"\"}","success":true}
     */
    suspend fun receiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        // [{"ignoreLimit":false,"requestType":"NORMAL","sceneCode":"ORCHARD_SPREAD_MANURE_ACTIVITY","source":"ANTFARM_ORCHARD_PLUS","taskType":"OSMA_GROUP_19_STEP_1","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "ignoreLimit" to false,
            "requestType" to "NORMAL",
            "sceneCode" to sceneCode,
            "source" to "ANTFARM_ORCHARD_PLUS",
            "taskType" to taskType,
            "version" to VERSION
        )
        return requestV2("com.alipay.antiep.receiveTaskAward", json.toListJson())
    }

    /**
     * 果园任务列表
     */
    suspend fun orchardListTask(): Result<JSONObject> {
        // [{"plantHiddenMMC":"false","requestType":"NORMAL","sceneCode":"ORCHARD","source":"ANTFARM_ORCHARD_PLUS","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "plantHiddenMMC" to false,
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "source" to "ANTFARM_ORCHARD_PLUS",
            "version" to VERSION
        )
        return requestV2("com.alipay.antfarm.orchardListTask", json.toListJson())
    }

    /**
     * 签到
     */
    suspend fun orchardSign(): Result<JSONObject> {
        // [{"requestType":"NORMAL","sceneCode":"ORCHARD","signScene":"ANTFARM_ORCHARD_SIGN_V2","source":"ch_appcenter__chsub_9patch","version":"0.1.2408191557.23"}]
        val json = mapOf(
            "requestType" to "NORMAL",
            "sceneCode" to "ORCHARD",
            "signScene" to "ANTFARM_ORCHARD_SIGN_V2",
            "source" to "ANTFARM_ORCHARD_PLUS",
            "version" to VERSION
        )
        return requestV2("com.alipay.antfarm.orchardSign", json.toListJson())
    }

    /**
     * 完成任务
     */
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

    suspend fun prizeTrigger(contentId:String): Result<JSONObject> {
        val map = mapOf(
            "activityType" to "antorchard",
            "seriesId" to "",
            "contentId" to contentId
        )
        return requestV2("alipay.content.cntactivitymodule.activity.prize.trigger", map.toListJson())
    }
}
