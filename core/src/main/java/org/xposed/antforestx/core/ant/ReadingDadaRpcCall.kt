package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.request
import org.xposed.antforestx.core.util.toListJson


/**
 * @author Constanline
 * @since 2023/08/22
 */
object ReadingDadaRpcCall {
    private const val VERSION = "1"

    suspend fun submitAnswer(activityId: String?, outBizId: String?, questionId: String, answer: String): Result<JSONObject> {
        return request(
            "com.alipay.reading.game.dada.openDailyAnswer.submitAnswer",
            "[{\"activityId\":\"" + activityId + "\",\"answer\":\"" + answer + "\",\"dadaVersion\":\"1.3.0\"," +
                    (if (outBizId.isNullOrEmpty()) "" else "\"outBizId\":\"$outBizId\",") +
                    "\"questionId\":\"" + questionId + "\",\"version\":" + VERSION + "}]"
        )
    }

    suspend fun getQuestion(activityId: String): Result<JSONObject> {
        val json = mapOf(
            "activityId" to activityId,
            "dadaVersion" to "1.3.0",
            "version" to VERSION
        ).toListJson()
        return request("com.alipay.reading.game.dada.openDailyAnswer.getQuestion", json)
    }
}