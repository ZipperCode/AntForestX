package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.request
import org.xposed.antforestx.core.util.RandomUtils


/**
 * @author Constanline
 * @since 2023/08/01
 */
object AntOceanRpcCall {
    private const val VERSION = "20230901"
    private val uniqueId: String get() = System.currentTimeMillis().toString() + RandomUtils.nextLong()

    suspend fun queryOceanStatus(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryOceanStatus",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    suspend fun queryHomePage(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryHomePage",
            "[{\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun cleanOcean(userId: String): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.cleanOcean",
            "[{\"cleanedUserId\":\"" + userId + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun ipOpenSurprise(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.ipOpenSurprise",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun collectReplicaAsset(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.collectReplicaAsset",
            "[{\"replicaCode\":\"avatar\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\"" + uniqueId +
                    "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun receiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        return request(
            "com.alipay.antiep.receiveTaskAward",
            "[{\"ignoreLimit\":false,\"requestType\":\"RPC\",\"sceneCode\":\"" + sceneCode + "\",\"source\":\"ANT_FOREST\",\"taskType\":\"" +
                    taskType + "\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun finishTask(sceneCode: String, taskType: String): Result<JSONObject> {
        val outBizNo = taskType + "_" + RandomUtils.nextDouble()
        return request(
            "com.alipay.antiep.finishTask",
            "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"RPC\",\"sceneCode\":\"" +
                    sceneCode + "\",\"source\":\"ANTFOCEAN\",\"taskType\":\"" + taskType + "\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun queryTaskList(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryTaskList",
            "[{\"extend\":{},\"fromAct\":\"dynamic_task\",\"sceneCode\":\"ANTOCEAN_TASK\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" +
                    uniqueId + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun unLockReplicaPhase(replicaCode: String, replicaPhaseCode: String): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.unLockReplicaPhase",
            "[{\"replicaCode\":\"" + replicaCode + "\",\"replicaPhaseCode\":\"" + replicaPhaseCode +
                    "\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\"" + uniqueId + "\",\"version\":\"20220707\"}]"
        )
    }

    suspend fun queryReplicaHome(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryReplicaHome",
            "[{\"replicaCode\":\"avatar\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun repairSeaArea(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.repairSeaArea",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun queryOceanPropList(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryOceanPropList",
            "[{\"propTypeList\":\"UNIVERSAL_PIECE\",\"skipPropId\":false,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" +
                    uniqueId + "\"}]"
        )
    }

    suspend fun querySeaAreaDetailList(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.querySeaAreaDetailList",
            "[{\"seaAreaCode\":\"\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUserId\":\"\",\"uniqueId\":\"" +
                    uniqueId + "\"}]"
        )
    }

    suspend fun queryOceanChapterList(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryOceanChapterList",
            "[{\"source\":\"chInfo_ch_url-https://2021003115672468.h5app.alipay.com/www/atlasOcean.html\",\"uniqueId\":\""
                    + uniqueId + "\"}]"
        )
    }

    suspend fun switchOceanChapter(chapterCode: String): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.switchOceanChapter",
            "[{\"chapterCode\":\"" + chapterCode
                    + "\",\"source\":\"chInfo_ch_url-https://2021003115672468.h5app.alipay.com/www/atlasOcean.html\",\"uniqueId\":\""
                    + uniqueId + "\"}]"
        )
    }

    suspend fun queryMiscInfo(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryMiscInfo",
            "[{\"queryBizTypes\":[\"HOME_TIPS_REFRESH\"],\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" +
                    uniqueId + "\"}]"
        )
    }

    suspend fun combineFish(fishId: String): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.combineFish", "[{\"fishId\":\"" + fishId +
                    "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun querySeaAreaDetailList(bubbleId: String, userId: String): Result<JSONObject> {
        return request(
            "alipay.antmember.forest.h5.collectEnergy",
            "[{\"bubbleIds\":[" + bubbleId + "],\"channel\":\"ocean\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" +
                    uniqueId + "\",\"userId\":\"" + userId + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun cleanFriendOcean(userId: String): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.cleanFriendOcean",
            "[{\"cleanedUserId\":\"" + userId + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun queryFriendPage(userId: String): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryFriendPage",
            "[{\"friendUserId\":\"" + userId + "\",\"interactFlags\":\"T\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" +
                    uniqueId + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun queryUserRanking(): Result<JSONObject> {
        return request(
            "alipay.antocean.ocean.h5.queryUserRanking",
            "[{\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }
}
