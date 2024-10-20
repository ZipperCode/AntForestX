package org.xposed.antforestx.core.ant

import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.requestV2

object AncientTreeRpcCall {
    private const val VERSION = "20240704"

    suspend fun homePage(selectCityCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.greenmatrix.rpc.h5.ancienttree.homePage",
            "[{\"cityCode\":\"330100\",\"selectCityCode\":\"" + selectCityCode
                    + "\",\"source\":\"antforesthome\"}]"
        )
    }

    suspend fun queryTreeItemsForExchange(cityCode: String): Result<JSONObject> {
        // [{"applyActions":"AVAILABLE,ENERGY_LACK","itemTypes":"","source":"chInfo_ch_appid-60000002","version":"20240704"}]
        val json = mapOf(
            "applyActions" to "AVAILABLE,ENERGY_LACK",
            "itemTypes" to "",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to VERSION
        )

        return requestV2(
            "alipay.antforest.forest.h5.queryTreeItemsForExchange",
            "[{\"cityCode\":\"" + cityCode
                    + "\",\"itemTypes\":\"\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun districtDetail(districtCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.greenmatrix.rpc.h5.ancienttree.districtDetail",
            "[{\"districtCode\":\"$districtCode\",\"source\":\"antforesthome\"}]"
        )
    }

    suspend fun projectDetail(ancientTreeProjectId: String, cityCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.greenmatrix.rpc.h5.ancienttree.projectDetail",
            "[{\"ancientTreeProjectId\":\"" + ancientTreeProjectId
                    + "\",\"channel\":\"ONLINE\",\"cityCode\":\"" + cityCode
                    + "\",\"source\":\"ancientreethome\"}]"
        )
    }

    suspend fun protect(activityId: String, ancientTreeProjectId: String, cityCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.greenmatrix.rpc.h5.ancienttree.protect",
            "[{\"ancientTreeActivityId\":\"" + activityId + "\",\"ancientTreeProjectId\":\""
                    + ancientTreeProjectId + "\",\"cityCode\":\"" + cityCode
                    + "\",\"source\":\"ancientreethome\"}]"
        )
    }
}
