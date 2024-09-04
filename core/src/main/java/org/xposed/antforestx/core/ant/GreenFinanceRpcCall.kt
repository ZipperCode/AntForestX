package org.xposed.antforestx.core.ant

import org.json.JSONArray
import org.json.JSONObject
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.toListJson

object GreenFinanceRpcCall {
    suspend fun greenFinanceIndex(): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.mcaplatformunit.common.mobile.newservice.GreenFinancePageQueryService.indexV2",
            "[{\"clientVersion\":\"VERSION2\",\"custType\":\"MERCHANT\"}]"
        )
    }

    suspend fun batchSelfCollect(bsnIds: JSONArray): Result<JSONObject> {
        val json = mapOf(
            "bsnIds" to "bsnIds",
            "clientVersion" to "VERSION2",
            "custType" to "MERCHANT",
            "uid" to UserManager.waitGetCurrentUid()
        ).toListJson()
        return RpcUtil.request(
            "com.alipay.mcaplatformunit.common.mobile.service.GreenFinancePointCollectService.batchSelfCollect",
            json
        )
    }

    suspend fun signInQuery(sceneId: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.signin.query",
            "[{\"cycleCount\":7,\"cycleType\":\"d\",\"extInfo\":{},\"needContinuous\":1,\"sceneId\":\"$sceneId\"}]"
        )
    }

    suspend fun signInTrigger(sceneId: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.signin.trigger",
            "[{\"extInfo\":{},\"sceneId\":\"$sceneId\"}]"
        )
    }

    suspend fun taskQuery(appletId: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.task.taskQuery",
            "[{\"appletId\":\"$appletId\",\"completedBottom\":true}]"
        )
    }

    suspend fun taskTrigger(appletId: String, stageCode: String, taskCenId: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.loanpromoweb.promo.task.taskTrigger",
            "[{\"appletId\":\"" + appletId + "\",\"stageCode\":\"" + stageCode + "\",\"taskCenId\":\"" + taskCenId
                    + "\"}]"
        )
    }
}
