package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.xposed.antforestx.core.ant.RpcUtil.request

/**
 * @author Constanline
 * @since 2023/08/04
 */
object DadaDailyRpcCall {
    suspend fun home(activityId: String): Result<JsonObject> {
        return request(
            "com.alipay.reading.game.dadaDaily.home",
            "[{\"activityId\":$activityId,\"dadaVersion\":\"1.3.0\",\"version\":1}]"
        )
    }

    suspend fun submit(activityId: String, answer: String, questionId: Long): Result<JsonObject> {
        return request(
            "com.alipay.reading.game.dadaDaily.submit",
            "[{\"activityId\":" + activityId + ",\"answer\":\"" + answer + "\",\"dadaVersion\":\"1.3.0\",\"questionId\":" +
                    questionId + ",\"version\":1}]"
        )
    }
}
