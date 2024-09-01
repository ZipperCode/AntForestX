package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.xposed.antforestx.core.ant.RpcUtil.request

object AncientTreeRpcCall {
    private const val VERSION = "20230522"

    suspend fun homePage(selectCityCode: String): Result<JsonObject> {
        return request(
            "alipay.greenmatrix.rpc.h5.ancienttree.homePage",
            "[{\"cityCode\":\"330100\",\"selectCityCode\":\"" + selectCityCode
                    + "\",\"source\":\"antforesthome\"}]"
        )
    }

    suspend fun queryTreeItemsForExchange(cityCode: String): Result<JsonObject> {
        return request(
            "alipay.antforest.forest.h5.queryTreeItemsForExchange",
            "[{\"cityCode\":\"" + cityCode
                    + "\",\"itemTypes\":\"\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun districtDetail(districtCode: String): Result<JsonObject> {
        return request(
            "alipay.greenmatrix.rpc.h5.ancienttree.districtDetail",
            "[{\"districtCode\":\"$districtCode\",\"source\":\"antforesthome\"}]"
        )
    }

    suspend fun projectDetail(ancientTreeProjectId: String, cityCode: String): Result<JsonObject> {
        return request(
            "alipay.greenmatrix.rpc.h5.ancienttree.projectDetail",
            "[{\"ancientTreeProjectId\":\"" + ancientTreeProjectId
                    + "\",\"channel\":\"ONLINE\",\"cityCode\":\"" + cityCode
                    + "\",\"source\":\"ancientreethome\"}]"
        )
    }

    suspend fun protect(activityId: String, ancientTreeProjectId: String, cityCode: String): Result<JsonObject> {
        return request(
            "alipay.greenmatrix.rpc.h5.ancienttree.protect",
            "[{\"ancientTreeActivityId\":\"" + activityId + "\",\"ancientTreeProjectId\":\""
                    + ancientTreeProjectId + "\",\"cityCode\":\"" + cityCode
                    + "\",\"source\":\"ancientreethome\"}]"
        )
    }
}
