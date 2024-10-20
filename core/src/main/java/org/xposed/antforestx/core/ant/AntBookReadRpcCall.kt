package org.xposed.antforestx.core.ant

import org.json.JSONObject
import org.xposed.antforestx.core.util.RandomUtils


/**
 * @author cwj851
 * @since 2024/01/18
 */
object AntBookReadRpcCall {
    private const val VERSION = "1.0.1397"

    /* 读书 */
    suspend fun queryTaskCenterPage(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookpromo.taskcenter.queryTaskCenterPage",
            "[{\"bannerId\":\"\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"hasAddHome\":false,\"miniClientVersion\":\"1.0.0\",\"supportFeatures\":[\"prize_task_20230831\"],\"yuyanVersion\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun queryMiniTaskCenterInfo(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookpromo.minitaskcenter.queryMiniTaskCenterInfo",
            "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"hasAddHome\":false,\"isFromSync\":false,\"miniClientVersion\":\"1.0.0\",\"needInfos\":\"\",\"yuyanVersion\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun syncUserReadInfo(bookId: String, chapterId: String): Result<JSONObject> {
        val readCount: Int = RandomUtils.nextInt(40, 200)
        val readTime: Int = RandomUtils.nextInt(160, 170) * 10000
        return RpcUtil.requestV2(
            "com.alipay.antbookread.biz.mgw.syncUserReadInfo",
            "[{\"bookId\":\"" + bookId
                    + "\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"chapterId\":\""
                    + chapterId
                    + "\",\"miniClientVersion\":\"1.0.0\",\"readCount\":" + readCount
                    + ",\"readTime\":" + readTime
                    + ",\"timeStamp\":" + System.currentTimeMillis()
                    + ",\"volumeId\":\"\",\"yuyanVersion\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun queryReaderForestEnergyInfo(bookId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookread.biz.mgw.queryReaderForestEnergyInfo",
            "[{\"bookId\":\"" + bookId
                    + "\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"miniClientVersion\":\"1.0.0\",\"yuyanVersion\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun queryHomePage(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookread.biz.mgw.queryHomePage",
            "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"miniClientVersion\":\"1.0.0\",\"yuyanVersion\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun queryBookCatalogueInfo(bookId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookread.biz.mgw.queryBookCatalogueInfo",
            "[{\"bookId\":\"" + bookId
                    + "\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"isInit\":true,\"miniClientVersion\":\"1.0.0\",\"order\":1,\"yuyanVersion\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun queryReaderContent(bookId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookread.biz.mgw.queryReaderContent",
            "[{\"bookId\":\"" + bookId
                    + "\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"isInit\":true,\"miniClientVersion\":\"1.0.0\",\"queryRecommend\":false,\"yuyanVersion\":\""
                    + VERSION + "\"}]"
        )
    }

    /* 任务 */
    suspend fun queryTreasureBox(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookpromo.taskcenter.queryTreasureBox",
            "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"miniClientVersion\":\"1.0.0\",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    suspend fun taskFinish(taskId: String, taskType: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookpromo.taskcenter.taskFinish",
            "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"miniClientVersion\":\"1.0.0\",\"taskId\":\""
                    + taskId
                    + "\",\"taskType\":\"" + taskType
                    + "\",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    suspend fun collectTaskPrize(taskId: String, taskType: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookpromo.taskcenter.collectTaskPrize",
            "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"miniClientVersion\":\"1.0.0\",\"taskId\":\""
                    + taskId
                    + "\",\"taskType\":\"" + taskType
                    + "\",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    suspend fun queryApplayer(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.adtask.biz.mobilegw.service.applayer.query",
            "[{\"spaceCode\":\"adPosId#2023112024200071171##sceneCode#null##mediaScene#42##rewardNum#1##spaceCode#READ_LISTEN_BOOK_TREASURE_FEEDS_FUSION##expCode#\"}]"
        )
    }

    suspend fun serviceTaskFinish(bizId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.adtask.biz.mobilegw.service.task.finish",
            "[{\"bizId\":\"$bizId\"}]"
        )
    }

    suspend fun serviceTaskQuery(bizId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.adtask.biz.mobilegw.service.task.query",
            "[{\"bizId\":\"$bizId\"}]"
        )
    }

    suspend fun openTreasureBox(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbookpromo.taskcenter.openTreasureBox",
            "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"miniClientVersion\":\"1.0.0\",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    /* 听书 */
    suspend fun queryEveningForestMainPage(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbooks.biz.mgw.queryEveningForestMainPage",
            "[{\"chInfo\":\"sy_wanansenlin_shouye\",\"miniClientVersion\":\"1.0.0\",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    suspend fun queryAlbumDetailPage(albumId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbooks.biz.mgw.queryAlbumDetailPage",
            "[{\"albumId\":" + albumId
                    + ",\"chInfo\":\"sy_wanansenlin_shouye\",\"miniClientVersion\":\"1.0.0\",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    suspend fun querySoundUrl(albumId: String, soundId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbooks.biz.mgw.querySoundUrl",
            "[{\"albumId\":" + albumId
                    + ",\"chInfo\":\"sy_wanansenlin_shouye\",\"miniClientVersion\":\"1.0.0\",\"sceneId\":\"EVENING_FOREST\",\"soundId\":"
                    + soundId + ",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    suspend fun syncUserPlayData(albumId: String, soundId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbooks.biz.mgw.syncUserPlayData",
            "[{\"chInfo\":\"sy_wanansenlin_shouye\",\"miniClientVersion\":\"1.0.0\",\"syncingPlayRecordRequestList\":[{\"albumId\":"
                    + albumId + ",\"position\":720,\"soundId\":" + soundId
                    + ",\"timestamp\":"
                    + System.currentTimeMillis() + "}],\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }

    suspend fun queryPlayPage(albumId: String, soundId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antbooks.biz.mgw.queryPlayPage",
            "[{\"albumId\":" + albumId
                    + ",\"chInfo\":\"sy_wanansenlin_shouye\",\"miniClientVersion\":\"1.0.0\",\"sceneId\":\"EVENING_FOREST\",\"soundId\":"
                    + soundId + ",\"yuyanVersion\":\"1.0.1397\"}]"
        )
    }
}