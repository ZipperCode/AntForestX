package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.request

object AntCooperateRpcCall {
    private const val VERSION = "20230501"

    suspend fun queryUserCooperatePlantList(): Result<JSONObject> {
        return request("alipay.antmember.forest.h5.queryUserCooperatePlantList", "[{}]")
    }

    suspend fun queryCooperatePlant(coopId: String): Result<JSONObject> {
        val args1 = "[{\"cooperationId\":\"$coopId\"}]"
        return request("alipay.antmember.forest.h5.queryCooperatePlant", args1)
    }

    suspend fun cooperateWater(uid: String, coopId: String, count: Int): Result<JSONObject> {
        return request(
            "alipay.antmember.forest.h5.cooperateWater",
            "[{\"bizNo\":\"" + uid + "_" + coopId + "_" + System.currentTimeMillis() + "\",\"cooperationId\":\""
                    + coopId + "\",\"energyCount\":" + count + ",\"source\":\"\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }
}
