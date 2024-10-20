package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject

/**
 * @author Constanline
 * @since 2023/08/04
 */
object DadaDailyRpcCall {
    suspend fun home(activityId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.reading.game.dadaDaily.home",
            "[{\"activityId\":$activityId,\"dadaVersion\":\"1.3.0\",\"version\":1}]"
        )
    }

    suspend fun submit(activityId: String, answer: String, questionId: Long): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.reading.game.dadaDaily.submit",
            "[{\"activityId\":" + activityId + ",\"answer\":\"" + answer + "\",\"dadaVersion\":\"1.3.0\",\"questionId\":" +
                    questionId + ",\"version\":1}]"
        )
    }
}
