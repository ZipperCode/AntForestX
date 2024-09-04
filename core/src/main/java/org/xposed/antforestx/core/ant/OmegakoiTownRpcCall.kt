package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.request
import java.util.UUID

object OmegakoiTownRpcCall {
    private const val version = "2.0"

    private val uuid: String
        get() {
            val sb = StringBuilder()
            for (str in UUID.randomUUID().toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                sb.append(str.substring(str.length / 2))
            }
            return sb.toString()
        }

    suspend fun houseProduct(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.house.product",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\",\"shouldScoreReward\":true}]"
        )
    }

    suspend fun houseBuild(groundId: String, houseId: String): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.house.build",
            "[{\"groundId\":\"" + groundId + "\",\"houseId\":\"" + houseId
                    + "\",\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getUserScore(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.user.getUserScore",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getBalloonsReadyToCollect(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.user.getUserScore",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getUserQuests(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.scenario.getUserQuests",
            "[{\"disableQuests\":true,\"outBizNo\":\"" + UUID.randomUUID().toString()
                    + "\",\"scenarioId\":\"shopNewestTips\"}]"
        )
    }


    suspend fun completeQuest(questId: String, scenarioId: String): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.scenario.completeQuest",
            "[{\"optionIndex\":0,\"outBizNo\":\"" + UUID.randomUUID().toString() + "\",\"questId\":\"" + questId
                    + "\",\"scenarioId\":\"" + scenarioId + "\",\"showType\":\"mayor\"}]"
        )
    }

    suspend fun groundBuy(groundId: String): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.ground.buy",
            "[{\"groundId\":\"" + groundId + "\",\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getCurrentBalloonsByTarget(groundId: String?): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.balloon.getCurrentBalloonsByTarget",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }


    suspend fun getUserTasks(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.task.getUserTasks",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun queryAppInfo(app_id: String): Result<JSONObject> {
        return request(
            "alipay.mappconfig.queryAppInfo",
            "[{\"baseInfoReq\":{\"appIds\":[\"" + app_id
                    + "\"],\"platform\":\"ANDROID\",\"pre\":false,\"width\":0},\"packInfoReq\":{\"bundleid\":\"com.alipay.alipaywallet\",\"channel\":\"offical\",\"client\":\"10.5.36.8100\",\"env\":\"production\",\"platform\":\"android\",\"protocol\":\"1.0\",\"query\":\"{\\\""
                    + app_id + "\\\":{\\\"app_id\\\":\\\"" + app_id
                    + "\\\",\\\"version\\\":\\\"*\\\",\\\"isTarget\\\":\\\"YES\\\"}}\",\"reqmode\":\"async\",\"sdk\":\"1.3.0.0\",\"system\":\"10\"},\"reqType\":2}]"
        )
    }

    suspend fun triggerTaskReward(taskId: String): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.task.triggerTaskReward",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\",\"taskId\":\"" + taskId + "\"}]"
        )
    }

    suspend fun getShareId(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.user.getShareId",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getfengdieData(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.user.getFengdieData",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getSignStatus(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.signIn.getSignInStatus",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun signIn(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.signIn.signIn",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getProduct(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.shop.getProduct",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getUserGrounds(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.ground.getUserGrounds",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun getUserHouses(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.house.getUserHouses",
            "[{\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun collect(houseId: String, id: Long): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.town.v2.house.collect",
            "[{\"houseId\":\"" + houseId + "\",\"id\":" + id
                    + ",\"outBizNo\":\"" + UUID.randomUUID().toString() + "\"}]"
        )
    }

    suspend fun matchCrowd(): Result<JSONObject> {
        return request(
            "com.alipay.omegakoi.common.user.matchCrowd",
            "[{\"crowdCodes\":[\"OUW7WQPH7\",\"OM9K933XZ\"],\"outBizNo\":\"60123460-b6ac-11ee-95b2-3be423343437\"}]"
        )
    }
}
