package org.xposed.antforestx.core.ant

import org.json.JSONObject
import org.xposed.antforestx.core.util.toListJson

/**
 * 生态保护
 */
object AntEcologyRpcCall {
    /**
     * 生态保护树列表
     */
    suspend fun queryTreeItemsForExchange(): Result<JSONObject> {
        // [{"applyActions":"AVAILABLE,ENERGY_LACK","itemTypes":"","source":"chInfo_ch_appid-60000002","version":"20240704"}]
        val json = mapOf(
            "applyActions" to "AVAILABLE,ENERGY_LACK",
            "itemTypes" to "",
            "source" to "chInfo_ch_appid-60000002",
            "version" to "20240704"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryTreeItemsForExchange", json)
    }
}