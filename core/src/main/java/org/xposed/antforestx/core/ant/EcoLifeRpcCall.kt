package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.xposed.antforestx.core.ant.RpcUtil.request

object EcoLifeRpcCall {
    suspend fun queryHomePage(): Result<JsonObject> {
        return request(
            "alipay.ecolife.rpc.h5.queryHomePage",
            "[{\"channel\":\"ALIPAY\",\"source\":\"search_brandbox\"}]"
        )
    }

    suspend fun tick(actionId: String, channel: String, dayPoint: String, photoguangpan: Boolean): Result<JsonObject> {
        var args1: String? = null
        args1 = if (photoguangpan) {
            ("[{\"actionId\":\"photoguangpan\",\"channel\":\"" + channel + "\",\"dayPoint\":\"" + dayPoint
                    + "\",\"source\":\"search_brandbox\"}]")
        } else {
            ("[{\"actionId\":\"" + actionId + "\",\"channel\":\""
                    + channel + "\",\"dayPoint\":\"" + dayPoint
                    + "\",\"generateEnergy\":false,\"source\":\"search_brandbox\"}]")
        }
        return request("alipay.ecolife.rpc.h5.tick", args1)
    }

    suspend fun queryDish(channel: String, dayPoint: String): Result<JsonObject> {
        return request(
            "alipay.ecolife.rpc.h5.queryDish",
            "[{\"channel\":\"" + channel + "\",\"dayPoint\":\"" + dayPoint
                    + "\",\"source\":\"photo-comparison\"}]"
        )
    }
}
