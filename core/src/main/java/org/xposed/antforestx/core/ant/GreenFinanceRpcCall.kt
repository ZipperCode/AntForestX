package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONArray
import pansong291.xposed.quickenergy.util.FriendIdMap

object GreenFinanceRpcCall {
    suspend fun greenFinanceIndex(): Result<JsonObject> {
        return RpcUtil.request(
            "com.alipay.mcaplatformunit.common.mobile.newservice.GreenFinancePageQueryService.indexV2",
            "[{\"clientVersion\":\"VERSION2\",\"custType\":\"MERCHANT\"}]"
        )
    }

    suspend fun batchSelfCollect(bsnIds: JSONArray): Result<JsonObject> {
        return RpcUtil.request(
            "com.alipay.mcaplatformunit.common.mobile.service.GreenFinancePointCollectService.batchSelfCollect",
            ("[{\"bsnIds\":" + bsnIds + ",\"clientVersion\":\"VERSION2\",\"custType\":\"MERCHANT\",\"uid\":\""
                    + FriendIdMap.getCurrentUid()).toString() + "\"}]"
        )
    }

    suspend fun signInQuery(sceneId: String): Result<JsonObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.signin.query",
            "[{\"cycleCount\":7,\"cycleType\":\"d\",\"extInfo\":{},\"needContinuous\":1,\"sceneId\":\"$sceneId\"}]"
        )
    }

    suspend fun signInTrigger(sceneId: String): Result<JsonObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.signin.trigger",
            "[{\"extInfo\":{},\"sceneId\":\"$sceneId\"}]"
        )
    }

    suspend fun taskQuery(appletId: String): Result<JsonObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.task.taskQuery",
            "[{\"appletId\":\"$appletId\",\"completedBottom\":true}]"
        )
    }

    suspend fun taskTrigger(appletId: String, stageCode: String, taskCenId: String): Result<JsonObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.task.taskTrigger",
            "[{\"appletId\":\"" + appletId + "\",\"stageCode\":\"" + stageCode + "\",\"taskCenId\":\"" + taskCenId
                    + "\"}]"
        )
    }
}
