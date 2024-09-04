package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.request

object KBMemberRpcCall {
    private const val version = "2.0"

    suspend fun rpcCall_signIn(): Result<JSONObject> {
        val args1 = "[{\"sceneCode\":\"KOUBEI_INTEGRAL\",\"source\":\"ALIPAY_TAB\",\"version\":\"" + version + "\"}]"
        return request("alipay.kbmemberprod.action.signIn", args1)
    }
}
