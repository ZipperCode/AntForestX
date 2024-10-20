package org.xposed.antforestx.core.ant

import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.requestV2
import org.xposed.antforestx.core.util.toListJson

object AntCooperateRpcCall {
    private const val VERSION = "20230501"

    /**
     * 合种数
     */
    suspend fun queryUserCooperatePlantList(): Result<JSONObject> {
        // [{"cooperationId":"","plantedTips":"N","source":"chInfo_ch_url-https://render.alipay.com/p/yuyan/180020010001247580/home.html"}]
        val json = mapOf(
            "cooperationId" to "",
            "plantedTips" to "N",
        ).toListJson()

        return requestV2("alipay.antmember.forest.h5.queryUserCooperatePlantList", json)
    }

    suspend fun queryCooperatePlant(coopId: String): Result<JSONObject> {
        val args1 = "[{\"cooperationId\":\"$coopId\"}]"
        return requestV2("alipay.antmember.forest.h5.queryCooperatePlant", args1)
    }

    suspend fun cooperateWater(uid: String, coopId: String, count: Int): Result<JSONObject> {
        // [{"bizNo":"2088112226168470_0xurr5b1r32tfs19pd45111lzsnv8470_1726489285875","cooperationId":"0xurr5b1r32tfs19pd45111lzsnv8470","energyCount":10,"source":"","version":"20230501"}]
        val json = mapOf(
            "bizNo" to "$uid-$coopId-${System.currentTimeMillis()}",
            "cooperationId" to coopId,
            "energyCount" to count,
            "source" to "",
            "version" to VERSION
        ).toListJson()
        return requestV2("alipay.antmember.forest.h5.cooperateWater", json)
    }
}
