package org.xposed.antforestx.core.ant

import org.json.JSONObject
import org.xposed.antforestx.core.ant.RpcUtil.requestV2
import org.xposed.antforestx.core.util.RandomUtils
import org.xposed.antforestx.core.util.toListJson


/**
 * @author Constanline
 * @since 2023/08/01
 */
object AntOceanRpcCall {
    private const val VERSION = "20240115"
    private val uniqueId: String get() = System.currentTimeMillis().toString() + RandomUtils.nextLong()

    /**
     * 查询海洋状态
     */
    suspend fun queryOceanStatus(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antocean.ocean.h5.queryOceanStatus",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    /**
     * 海洋首页
     */
    suspend fun queryHomePage(): Result<JSONObject> {
        // [{"source":"ANT_FOREST","uniqueId":"17264899722293414652720870086","version":"20240115"}]
        val json = mapOf(
            "source" to "ANT_FOREST",
            "uniqueId" to uniqueId,
            "version" to VERSION
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.queryHomePage", json)
    }

    /**
     * 清理海洋
     */
    suspend fun cleanOcean(userId: String): Result<JSONObject> {
        // [{"cleanedUserId":"2088112226168470","source":"ANT_FOREST","uniqueId":"17264902008255217787429357321"}]
        val json = mapOf(
            "cleanedUserId" to userId,
            "source" to "ANT_FOREST",
            "uniqueId" to uniqueId
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.cleanOcean", json)
    }

    suspend fun ipOpenSurprise(): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.ipOpenSurprise",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun collectReplicaAsset(): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.collectReplicaAsset",
            "[{\"replicaCode\":\"avatar\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\"" + uniqueId +
                    "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    /**
     * 收取任务奖励
     */
    suspend fun receiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        // [{"ignoreLimit":false,"requestType":"RPC","sceneCode":"ANTOCEAN_TASK","source":"ANTFOCEAN","taskType":"BUSINESS_LIGHTS01","uniqueId":"17264942231524266039953950229"}]
        val json = mapOf(
            "ignoreLimit" to false,
            "requestType" to "RPC",
            "sceneCode" to sceneCode,
            "source" to "ANTFOCEAN",
            "taskType" to taskType,
            "uniqueId" to uniqueId
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antiep.receiveTaskAward", json)
    }

    suspend fun finishTask(sceneCode: String, taskType: String): Result<JSONObject> {
        // [{"outBizNo":"BUSINESS_LIGHTS01_1726493917755_7c654fc8","requestType":"RPC","sceneCode":"ANTOCEAN_TASK","source":"ADBASICLIB","taskType":"BUSINESS_LIGHTS01"}]
        val outBizNo = taskType + "_" + System.currentTimeMillis() + "_" + RandomUtils.getRandom(8)
        val json = mapOf(
            "outBizNo" to outBizNo,
            "requestType" to "RPC",
            "sceneCode" to sceneCode,
            "source" to "ADBASICLIB",
            "taskType" to taskType
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antiep.finishTask", json)
    }

    suspend fun queryTaskList(): Result<JSONObject> {
        // [{"extend":{},"fromAct":"dynamic_task","sceneCode":"ANTOCEAN_TASK","source":"ANT_FOREST","uniqueId":"17264938968047338119898098765","version":"20240115"}]
        val json = mapOf(
            "fromAct" to "dynamic_task",
            "sceneCode" to "ANTOCEAN_TASK",
            "source" to "ANT_FOREST",
            "uniqueId" to uniqueId,
            "version" to VERSION
        ).toListJson()

        return RpcUtil.requestV2("alipay.antocean.ocean.h5.queryTaskList", json)
    }

    suspend fun unLockReplicaPhase(replicaCode: String, replicaPhaseCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.unLockReplicaPhase",
            "[{\"replicaCode\":\"" + replicaCode + "\",\"replicaPhaseCode\":\"" + replicaPhaseCode +
                    "\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\"" + uniqueId + "\",\"version\":\"20220707\"}]"
        )
    }

    suspend fun queryReplicaHome(): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.queryReplicaHome",
            "[{\"replicaCode\":\"avatar\",\"source\":\"senlinzuoshangjiao\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun repairSeaArea(): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.repairSeaArea",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    /**
     * 查询海洋道具
     */
    suspend fun queryOceanPropList(): Result<JSONObject> {
        // [{"propTypeList":"UNIVERSAL_PIECE","skipPropId":false,"source":"ANT_FOREST","uniqueId":"17264902009516642196143100647"}]
        val json = mapOf(
            "propTypeList" to "UNIVERSAL_PIECE",
            "skipPropId" to false,
            "source" to "ANT_FOREST",
            "uniqueId" to uniqueId
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.queryOceanPropList", json)
    }

    /**
     * 海洋区域
     */
    suspend fun querySeaAreaDetailList(): Result<JSONObject> {
        // [{"seaAreaCode":"","source":"ANT_FOREST","targetUserId":"","uniqueId":"17264943980856074660920098944"}]
        val json = mapOf(
            "seaAreaCode" to "",
            "source" to "ANT_FOREST",
            "targetUserId" to "",
            "uniqueId" to uniqueId
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.querySeaAreaDetailList", json)
    }

    suspend fun queryOceanChapterList(): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.queryOceanChapterList",
            "[{\"source\":\"chInfo_ch_url-https://2021003115672468.h5app.alipay.com/www/atlasOcean.html\",\"uniqueId\":\""
                    + uniqueId + "\"}]"
        )
    }

    suspend fun switchOceanChapter(chapterCode: String): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.switchOceanChapter",
            "[{\"chapterCode\":\"" + chapterCode
                    + "\",\"source\":\"chInfo_ch_url-https://2021003115672468.h5app.alipay.com/www/atlasOcean.html\",\"uniqueId\":\""
                    + uniqueId + "\"}]"
        )
    }

    suspend fun queryMiscInfo(): Result<JSONObject> {
        // [{"extInfo":{"EMERGENCY":272025},"queryBizTypes":["KNOWLEDGE_TIPS","EMERGENCY","HOME_TIPS_REFRESH"],"source":"ANT_FOREST","uniqueId":"17264899738094426413747888198"}]
        val json = mapOf(
            "extInfo" to mapOf("EMERGENCY" to 272025),
            "queryBizTypes" to listOf("KNOWLEDGE_TIPS", "EMERGENCY", "HOME_TIPS_REFRESH"),
            "source" to "ANT_FOREST",
            "uniqueId" to uniqueId
        ).toListJson()
        return requestV2("alipay.antocean.ocean.h5.queryMiscInfo", json)
    }

    /**
     * 合并
     */
    suspend fun combineFish(fishId: String): Result<JSONObject> {
        val json = mapOf(
            "fishId" to fishId,
            "source" to "ANT_FOREST",
            "uniqueId" to uniqueId
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.combineFish", json)
    }

    suspend fun querySeaAreaDetailList(bubbleId: String, userId: String): Result<JSONObject> {
        return requestV2(
            "alipay.antmember.forest.h5.collectEnergy",
            "[{\"bubbleIds\":[" + bubbleId + "],\"channel\":\"ocean\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" +
                    uniqueId + "\",\"userId\":\"" + userId + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun cleanFriendOcean(userId: String): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.cleanFriendOcean",
            "[{\"cleanedUserId\":\"" + userId + "\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun queryFriendPage(userId: String): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.queryFriendPage",
            "[{\"friendUserId\":\"" + userId + "\",\"interactFlags\":\"T\",\"source\":\"ANT_FOREST\",\"uniqueId\":\"" +
                    uniqueId + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun queryUserRanking(): Result<JSONObject> {
        return requestV2(
            "alipay.antocean.ocean.h5.queryUserRanking",
            "[{\"source\":\"ANT_FOREST\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    /**
     * 兑换道具
     */
    suspend fun exchangeProp(exchangeNum: Int): Result<JSONObject> {
        // [{"bizNo":1726494500930,"exchangeNum":1,"propCode":"UNIVERSAL_PIECE","propType":"UNIVERSAL_PIECE","source":"ANT_FOREST","uniqueId":"172649450093018685668241346476"}]
        val json = mapOf(
            "bizNo" to System.currentTimeMillis(),
            "exchangeNum" to exchangeNum,
            "propCode" to "UNIVERSAL_PIECE",
            "propType" to "UNIVERSAL_PIECE",
            "source" to "ANT_FOREST",
            "uniqueId" to uniqueId
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.exchangeProp", json)
    }

    /**
     * 鱼类列表
     * @param chapterCode chapterOne,chapterTwo 分类
     * @param combineStatus UNOBTAINED 未迎回 OBTAINED 以迎回
     */
    suspend fun queryFishList(chapterCode: String, combineStatus: String): Result<JSONObject> {
        //未迎回 [{"combineStatus":"UNOBTAINED","needSummary":"Y","pageNum":1,"source":"ANT_FOREST","targetUserId":"","uniqueId":"17264946081564705276187737133"}]
        //全部 [{"chapterCode":"chapterOne","needSummary":"Y","pageNum":1,"source":"ANT_FOREST","targetUserId":"","uniqueId":"1726494724971055808429254236014"}]
        val json = mapOf(
            "chapterOne" to chapterCode,
            "combineStatus" to combineStatus,
            "needSummary" to "Y",
            "pageNum" to 1,
            "source" to "ANT_FOREST",
            "targetUserId" to "",
            "uniqueId" to uniqueId
        ).toListJson()
        return RpcUtil.requestV2("alipay.antocean.ocean.h5.queryFishList", json)
    }
}
