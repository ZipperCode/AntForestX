package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.util.RandomUtils
import org.xposed.antforestx.core.util.toListJson

object ReserveRpcCall {
    private const val VERSION = "20230501"
    private const val VERSION2 = "20230522"
    private const val VERSION3 = "20231031"

    private val uniqueId: String
        get() = System.currentTimeMillis().toString() + RandomUtils.nextLong()

    suspend fun queryTreeItemsForExchange(): Result<JSONObject> {
        val json = mapOf(
            "cityCode" to "370100",
            "itemTypes" to "",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to VERSION2
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryTreeItemsForExchange", json)
    }

    suspend fun queryTreeForExchange(projectId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.queryTreeForExchange",
            "[{\"projectId\":\"" + projectId + "\",\"version\":\"" + VERSION
                    + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    suspend fun exchangeTree(projectId: String): Result<JSONObject> {
        val projectId_num = projectId.toInt()
        return RpcUtil.requestV2(
            "alipay.antmember.forest.h5.exchangeTree",
            "[{\"projectId\":" + projectId_num + ",\"sToken\":\"" + System.currentTimeMillis() + "\",\"version\":\""
                    + VERSION + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    /* 净滩行动 */
    suspend fun queryCultivationList(): Result<JSONObject> {
        val json = mapOf(
            "source" to "ANT_FOREST",
            "version" to VERSION3
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.queryCultivationList", json)
    }

    suspend fun queryCultivationDetail(cultivationCode: String, projectCode: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antocean.ocean.h5.queryCultivationDetail",
            "[{\"cultivationCode\":\"" + cultivationCode + "\",\"projectCode\":\"" + projectCode
                    + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun oceanExchangeTree(cultivationCode: String, projectCode: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antocean.ocean.h5.exchangeTree",
            "[{\"cultivationCode\":\"" + cultivationCode + "\",\"projectCode\":\"" + projectCode
                    + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }
}
