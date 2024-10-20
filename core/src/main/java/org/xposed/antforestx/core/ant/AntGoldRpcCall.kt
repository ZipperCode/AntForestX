package org.xposed.antforestx.core.ant

import org.json.JSONObject
import org.xposed.antforestx.core.util.toListJson

object AntGoldRpcCall {

    suspend fun index(): Result<JSONObject> {
        // [{"pageTemplateCode":"H5_GOLDBILL","params":{"client_pkg_version":"0.0.6"},"url":"https://68687437.h5app.alipay.com/www/index.html?bizScene=todayRecommend&enableWK=YES&isAssertThumbnail=N"}]
        val json = mapOf(
            "pageTemplateCode" to "H5_GOLDBILL",
            "params" to mapOf(
                "client_pkg_version" to "0.0.6"
            ),
            "url" to "https://68687437.h5app.alipay.com/www/index.html?bizScene=todayRecommend&enableWK=YES&isAssertThumbnail=N"
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.wealthgoldtwa.needle.goldbill.index", json)
    }

    suspend fun collect(campId: String): Result<JSONObject> {
        // [{"campId":"CP1417744","directModeDisableCollect":true,"from":"signIn","trigger":"Y"}]
        val json = mapOf(
            "campId" to campId,
            "directModeDisableCollect" to true,
            "from" to "signIn",
            "trigger" to "Y"
        ).toListJson()

        return RpcUtil.requestV2("com.alipay.wealthgoldtwa.goldbill.v2.index.collect", """[{"action":"collect","pageType":"index","taskExt":"{}"}]""")
    }
}