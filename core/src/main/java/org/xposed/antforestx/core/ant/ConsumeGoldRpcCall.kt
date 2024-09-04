package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.request
import java.util.Locale
import java.util.UUID

object ConsumeGoldRpcCall {
    private val requestId: String
        // private static final String VERSION = "20230522";
        get() {
            val sb = StringBuilder()
            for (str in UUID.randomUUID().toString().split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                sb.append(str.substring(str.length / 2))
            }
            return sb.toString().uppercase(Locale.getDefault())
        }

    suspend fun taskV2Index(taskSceneCode: String): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.taskV2.index",
            "[{\"alipayAppVersion\":\"10.5.36.8100\",\"appClient\":\"Android\",\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                    + UUID.randomUUID().toString()
                    + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"LowVersion\",\"taskSceneCode\":\""
                    + taskSceneCode + "\",\"userType\":\"new\"}]"
        )
    }

    suspend fun consumeGoldIndex(): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.index",
            "[{\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                    + UUID.randomUUID().toString()
                    + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"LowVersion\"}]"
        )
    }

    suspend fun signinCalendar(): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.task.signin.calendar",
            "[{}]"
        )
    }

    suspend fun openBoxAward(): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.task.openBoxAward",
            "[{\"actionAwardDetails\":[{\"actionType\":\"date_sign_start\"}],\"bizType\":\"CONSUME_GOLD\",\"boxType\":\"CONSUME_GOLD_SIGN_DATE\",\"clientVersion\":\"6.3.0\",\"timeScaleType\":0,\"userType\":\"new\"}]"
        )
    }

    suspend fun taskV2TriggerSignUp(taskId: String): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.taskV2.trigger",
            "[{\"taskId\":\"$taskId\",\"triggerAction\":\"SIGN_UP\"}]"
        )
    }

    suspend fun taskV2TriggerSend(taskId: String): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.taskV2.trigger",
            "[{\"taskId\":\"$taskId\",\"triggerAction\":\"SEND\"}]"
        )
    }

    suspend fun taskV2TriggerReceive(taskId: String): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.taskV2.trigger",
            "[{\"taskId\":\"$taskId\",\"triggerAction\":\"RECEIVE\"}]"
        )
    }

    suspend fun promoTrigger(): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.index.promo.trigger",
            "[{\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                    + UUID.randomUUID().toString()
                    + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"UnFavorite\",\"requestId\":\""
                    + requestId + "\"}]"
        )
    }

    suspend fun advertisement(outBizNo: String): Result<JSONObject> {
        return request(
            "alipay.mobile.ipsponsorprod.consume.gold.send.promo.advertisement",
            "[{\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                    + UUID.randomUUID().toString()
                    + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"UnFavorite\",\"outBizNo\":\""
                    + outBizNo
                    + "\",\"type\":\"HOME_PROMO_ADVERTISEMENT\",\"userType\":\"new\"}]"
        )
    }
}