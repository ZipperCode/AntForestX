package org.xposed.antforestx.core.ant

import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.json.JSONObject
import org.xposed.antforestx.core.util.RandomUtils
import org.xposed.antforestx.core.util.toListJson
import java.util.UUID

object AntForestRpcCall {

    private const val VERSION = "20240909"

    private fun getUniqueId(): String {
        return System.currentTimeMillis().toString() + RandomUtils.nextLong()
    }

    suspend fun fillUserRobFlag(userIdList: List<String>): Result<JSONObject> {
        val idsJson = Json.encodeToString(serializer(), userIdList)
        return RpcUtil.requestV2("alipay.antforest.forest.h5.fillUserRobFlag", """[{"userIdList": $idsJson }]""")
    }

    suspend fun queryEnergyRanking(): Result<JSONObject> {
        val json = mapOf(
            "periodType" to "day",
            "rankType" to "energyRank",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to VERSION,
        ).toListJson()
        return RpcUtil.requestV2("alipay.antmember.forest.h5.queryEnergyRanking", json)
    }

    /**
     * 查询首页
     */
    suspend fun queryHomePage(): Result<JSONObject> {
        val json = mapOf(
            "configVersionMap" to mapOf(
                "wateringBubbleConfig" to "0"
            ),
            "skipWhackMole" to false,
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to VERSION,
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryHomePage", json)
    }

    /**
     * 查询好友森林首页
     */
    suspend fun queryFriendHomePage(userId: String): Result<JSONObject> {
        // [{"activityParam":{},"configVersionMap":{"wateringBubbleConfig":"0"},"currentEnergy":275873,"currentVitalityAmount":56393,"fromAct":"TAKE_LOOK","skipWhackMole":false,"source":"chInfo_ch_appcenter__chsub_9patch","userId":"2088912123322694","version":"20240909"}]
       val json = mapOf(
           "activityParam" to emptyMap<String, String>(),
           "configVersionMap" to mapOf(
               "wateringBubbleConfig" to "0"
           ),
           "currentEnergy" to 0,
           "currentVitalityAmount" to 0,
           "fromAct" to "TAKE_LOOK",
           "skipWhackMole" to false,
           "source" to "chInfo_ch_appcenter__chsub_9patch",
           "userId" to userId,
       )
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryFriendHomePage", json.toListJson())
    }

    suspend fun collectEnergy(bizType: String?, userId: String, bubbleId: Long): Result<JSONObject> {
        val args1 = if (bizType.isNullOrEmpty()) {
            ("[{\"bizType\":\"\",\"bubbleIds\":[" + bubbleId
                    + "],\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" + userId + "\",\"version\":\""
                    + VERSION + "\"}]")
        } else {
            ("[{\"bizType\":\"" + bizType + "\",\"bubbleIds\":[" + bubbleId
                    + "],\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" + userId + "\"}]")
        }
        return RpcUtil.requestV2("alipay.antmember.forest.h5.collectEnergy", args1)
    }

    suspend fun collectEnergy(bizType: String?, userId: String, bubbleIds: List<Long>, batchCollect: Boolean): Result<JSONObject> {
        // [{"bizType":"","bubbleIds":[4400961998],"source":"chInfo_ch_appcenter__chsub_9patch","userId":"2088112226168470","version":"20230501"}]
        val json = mutableMapOf(
            "bizType" to (bizType ?: ""),
            "bubbleIds" to bubbleIds,
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "userId" to userId,
            "version" to "20230501"
        )
        if (batchCollect) {
            json["fromAct"] = "BATCH_ROB_ENERGY"
        }

        return RpcUtil.requestV2("alipay.antmember.forest.h5.collectEnergy", json.toListJson())
    }

    /**
     * 批量收取
     */
    suspend fun batchCollectEnergy(userId: String, bubbleId: List<Long>): Result<JSONObject> {
        // [{"bizType":"","bubbleIds":[4204445652],"fromAct":"BATCH_ROB_ENERGY","source":"chInfo_ch_appcenter__chsub_9patch","userId":"2088422733380440","version":"20230501"}]
        val json = mapOf(
            "bizType" to "",
            "bubbleIds" to bubbleId,
            "fromAct" to "BATCH_ROB_ENERGY",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "userId" to userId,
            "version" to "20230501"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antmember.forest.h5.collectEnergy", json)
    }

    suspend fun collectRebornEnergy(): Result<JSONObject> {
        return RpcUtil.requestV2("alipay.antforest.forest.h5.collectRebornEnergy", "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]")
    }

    suspend fun transferEnergy(targetUser: String, bizNo: String, energyId: Int): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antmember.forest.h5.transferEnergy", "[{\"bizNo\":\"" +
                    bizNo + UUID.randomUUID().toString() + "\",\"energyId\":" + energyId +
                    ",\"extInfo\":{\"sendChat\":\"N\"},\"from\":\"friendIndex\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUser\":\""
                    + targetUser + "\",\"transferType\":\"WATERING\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun forFriendCollectEnergy(targetUserId: String, bubbleId: Long): Result<JSONObject> {
        val args1 = "[{\"bubbleIds\":[$bubbleId],\"targetUserId\":\"$targetUserId\"}]"
        return RpcUtil.requestV2("alipay.antmember.forest.h5.forFriendCollectEnergy", args1)
    }

    /**
     * 活力签到
     */
    suspend fun vitalitySign(): Result<JSONObject> {
        val json = mapOf(
            "source" to "chInfo_ch_appcenter__chsub_9patch"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.vitalitySign", json)
    }

    /**
     * 活力值任务
     */
    suspend fun queryTaskList(): Result<JSONObject> {
        val json = mapOf(
            "extend" to emptyMap<String, String>(),
            "fromAct" to "home_task_list",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to "20240105"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryTaskList", json)
    }

    /**
     * 查询能量雨
     */
    suspend fun queryEnergyRainHome(): Result<JSONObject> {
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryEnergyRainHome", """[{"source":"jgygplus","version":"20230501"}]""")
    }

    suspend fun queryEnergyRainCanGrantList(): Result<JSONObject> {
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryEnergyRainCanGrantList", "[{}]")
    }

    suspend fun grantEnergyRainChance(targetUserId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.grantEnergyRainChance",
            "[{\"targetUserId\":$targetUserId}]"
        )
    }

    suspend fun startEnergyRain(): Result<JSONObject> {
        return RpcUtil.requestV2("alipay.antforest.forest.h5.startEnergyRain", "[{\"version\":\"$VERSION\"}]")
    }

    suspend fun energyRainSettlement(saveEnergy: Int, token: String): Result<JSONObject> {
        // [{"activityPropNums":0,"saveEnergy":68,"token":"dfd55b2875c8a0533214fd24feb0594f","version":"20230501"}]
        val json = mapOf(
            "activityPropNums" to 0,
            "saveEnergy" to saveEnergy,
            "token" to token,
            "version" to "20230501"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.energyRainSettlement", json)
    }

    suspend fun receiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        // [{"ignoreLimit":false,"requestType":"H5","sceneCode":"ANTFOREST_VITALITY_TASK","source":"ANTFOREST","taskType":"ANTFOREST_DOUBLE_CLICK_ZHOUMO"}]
        val json = mapOf(
            "ignoreLimit" to false,
            "requestType" to "H5",
            "sceneCode" to sceneCode,
            "source" to "ANTFOREST",
            "taskType" to taskType
        ).toListJson()

        return RpcUtil.requestV2("com.alipay.antiep.receiveTaskAward", json)
    }

    suspend fun finishTask(sceneCode: String, taskType: String): Result<JSONObject> {
        // 31天能量卡 [{"outBizNo":"WT_ENERGY_DOUBLE_CLICK_31DAYS_ZDH_1726452602686_562a7399","requestType":"H5","sceneCode":"ANTFOREST_POPUP_WIN_TASK","source":"ANTFOREST","taskType":"WT_ENERGY_DOUBLE_CLICK_31DAYS_ZDH"}]
        // [{"outBizNo":"TEST_LEAF_COOLECT_TASK_1727532491444_163d843a","requestType":"H5","sceneCode":"ANTFOREST_VITALITY_TASK","source":"ANTFOREST","taskType":"TEST_LEAF_COOLECT_TASK"}]
        val outBizNo = taskType + "_" + System.currentTimeMillis() + "_" + RandomUtils.getRandom(8)
        val json = mapOf(
            "outBizNo" to outBizNo,
            "requestType" to "H5",
            "sceneCode" to sceneCode,
            "source" to "ANTFOREST",
            "taskType" to taskType
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antiep.finishTask", json)
    }

    suspend fun popupTask(): Result<JSONObject> {
        val json = mapOf(
            "fromAct" to "pop_task",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "statusList" to listOf("TODO", "FINISHED"),
            "version" to "20240704"
        ).toListJson()

        return RpcUtil.requestV2("alipay.antforest.forest.h5.popupTask", json)
    }

    suspend fun antiepSign(entityId: String, userId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antiep.sign",
            "[{\"entityId\":\"" + entityId
                    + "\",\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_ENERGY_SIGN\",\"source\":\"ANTFOREST\",\"userId\":\""
                    + userId + "\"}]"
        )
    }

    /**
     * 查询道具列表
     */
    suspend fun queryPropList(onlyGive: Boolean = false): Result<JSONObject> {
        // [{"onlyGive":"","source":"chInfo_ch_appcenter__chsub_9patch","version":"20240820"}]
        val json = mapOf(
            "onlyGive" to if (onlyGive) "Y" else "",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to "20240820"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryPropList", json)
    }

    suspend fun giveProp(giveConfigId: String, propId: String, targetUserId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.giveProp",
            "[{\"giveConfigId\":\"" + giveConfigId + "\",\"propId\":\"" + propId
                    + "\",\"source\":\"self_corner\",\"targetUserId\":\"" + targetUserId + "\"}]"
        )
    }

    suspend fun collectProp(giveConfigId: String, giveId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.collectProp",
            "[{\"giveConfigId\":\"" + giveConfigId + "\",\"giveId\":\"" + giveId
                    + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    /**
     * 消费道具
     */
    suspend fun consumeProp(propId: String, propType: String): Result<JSONObject> {
        // [{"propId":"00j1a5c1k6oqwf16ra0gp0ekq0ze8470","propType":"ENERGY_DOUBLE_CLICK","source":"chInfo_ch_appcenter__chsub_9patch","timezoneId":"Asia/Shanghai","version":"20230501"}]
        val json = mapOf(
            "propId" to propId,
            "propType" to propType,
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "timezoneId" to "Asia/Shanghai",
            "version" to "20230501"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.consumeProp", json)
    }

    /**
     * 活力值兑换列表
     */
    suspend fun itemList(labelType: String): Result<JSONObject> {
        // [{"extendInfo":"{}","fromSpuId":"","labelType":"SC_ASSETS","pageSize":10,"requestType":"rpc","sceneCode":"ANTFOREST_VITALITY","source":"afEntry","startIndex":0}]
        val json = mapOf(
            "extendInfo" to "{}",
            "fromSpuId" to "",
            "labelType" to labelType,
            "pageSize" to 10,
            "requestType" to "rpc",
            "sceneCode" to "ANTFOREST_VITALITY",
            "source" to "afEntry",
            "startIndex" to 0
        ).toListJson()
        return RpcUtil.requestV2("com.alipay.antiep.itemList", json)
    }

    suspend fun itemDetail(spuId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antiep.itemDetail",
            "[{\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"source\":\"afEntry\",\"spuId\":\""
                    + spuId + "\"}]"
        )
    }

    /**
     * 活力值首页
     */
    suspend fun queryVitalityStoreIndex(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.queryVitalityStoreIndex",
            """[{"source":"afEntry"}]"""
        )
    }

    suspend fun exchangeBenefit(spuId: String, skuId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antcommonweal.exchange.h5.exchangeBenefit",
            ("[{\"sceneCode\":\"ANTFOREST_VITALITY\",\"requestId\":\"" + System.currentTimeMillis()
                    + "_" + RandomUtils.getRandom(17)).toString() + "\",\"spuId\":\"" +
                    spuId + "\",\"skuId\":\"" + skuId + "\",\"source\":\"GOOD_DETAIL\"}]"
        )
    }

    suspend fun testH5Rpc(operationTpye: String?, requestDate: String?): Result<JSONObject> {
        return RpcUtil.requestV2(operationTpye!!, requestDate!!)
    }


    /* 神奇物种 */
    suspend fun queryAnimalStatus(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.queryAnimalStatus",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    suspend fun antdodoHomePage(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.homePage",
            "[{}]"
        )
    }

    suspend fun taskEntrance(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.taskEntrance",
            "[{\"statusList\":[\"TODO\",\"FINISHED\"]}]"
        )
    }

    suspend fun antdodoCollect(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.collect",
            "[{}]"
        )
    }

    suspend fun antdodoTaskList(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.taskList",
            "[{}]"
        )
    }

    suspend fun antdodoFinishTask(sceneCode: String, taskType: String): Result<JSONObject> {
        val uniqueId: String = AntForestRpcCall.getUniqueId()
        return RpcUtil.requestV2(
            "com.alipay.antiep.finishTask",
            "[{\"outBizNo\":\"" + uniqueId + "\",\"requestType\":\"rpc\",\"sceneCode\":\""
                    + sceneCode + "\",\"source\":\"af-biodiversity\",\"taskType\":\""
                    + taskType + "\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun antdodoReceiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "com.alipay.antiep.receiveTaskAward",
            "[{\"ignoreLimit\":0,\"requestType\":\"rpc\",\"sceneCode\":\"" + sceneCode
                    + "\",\"source\":\"af-biodiversity\",\"taskType\":\"" + taskType
                    + "\"}]"
        )
    }

    suspend fun antdodoPropList(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.propList",
            "[{}]"
        )
    }

    suspend fun antdodoConsumeProp(propId: String, propType: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.consumeProp",
            "[{\"propId\":\"$propId\",\"propType\":\"$propType\"}]"
        )
    }

    suspend fun queryBookInfo(bookId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.queryBookInfo",
            "[{\"bookId\":\"$bookId\"}]"
        )
    }

    // 送卡片给好友
    suspend fun antdodoSocial(targetAnimalId: String, targetUserId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antdodo.rpc.h5.social",
            "[{\"actionCode\":\"GIFT_TO_FRIEND\",\"source\":\"GIFT_TO_FRIEND_FROM_CC\",\"targetAnimalId\":\""
                    + targetAnimalId + "\",\"targetUserId\":\"" + targetUserId
                    + "\",\"triggerTime\":\"" + System.currentTimeMillis() + "\"}]"
        )
    }

    /* 巡护保护地 */
    suspend fun queryUserPatrol(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.queryUserPatrol",
            "[{\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun queryMyPatrolRecord(): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.queryMyPatrolRecord",
            "[{\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun switchUserPatrol(targetPatrolId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.switchUserPatrol",
            "[{\"source\":\"ant_forest\",\"targetPatrolId\":$targetPatrolId,\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun patrolGo(nodeIndex: Int, patrolId: Int): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.patrolGo",
            "[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                    + ",\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun patrolKeepGoing(nodeIndex: Int, patrolId: Int, eventType: String?): Result<JSONObject> {
        var args: String? = null
        args = when (eventType) {
            "video" -> ("[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                    + ",\"reactParam\":{\"viewed\":\"Y\"},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]")

            "chase" -> ("[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                    + ",\"reactParam\":{\"sendChat\":\"Y\"},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]")

            "quiz" -> ("[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                    + ",\"reactParam\":{\"answer\":\"correct\"},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]")

            else -> ("[{\"nodeIndex\":" + nodeIndex + ",\"patrolId\":" + patrolId
                    + ",\"reactParam\":{},\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]")
        }
        return RpcUtil.requestV2("alipay.antforest.forest.h5.patrolKeepGoing", args)
    }

    suspend fun exchangePatrolChance(costStep: Int): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.exchangePatrolChance",
            "[{\"costStep\":$costStep,\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun queryAnimalAndPiece(animalId: Int): Result<JSONObject> {
        var args: String? = null
        args = if (animalId != 0) {
            "[{\"animalId\":$animalId,\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        } else {
            "[{\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\",\"withDetail\":\"N\",\"withGift\":true}]"
        }
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryAnimalAndPiece", args)
    }

    suspend fun combineAnimalPiece(animalId: Int, piecePropIds: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.combineAnimalPiece",
            "[{\"animalId\":" + animalId + ",\"piecePropIds\":" + piecePropIds
                    + ",\"timezoneId\":\"Asia/Shanghai\",\"source\":\"ant_forest\"}]"
        )
    }

    suspend fun AnimalConsumeProp(propGroup: String, propId: String, propType: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.consumeProp",
            "[{\"propGroup\":\"" + propGroup + "\",\"propId\":\"" + propId + "\",\"propType\":\"" + propType
                    + "\",\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun collectAnimalRobEnergy(propId: String, propType: String, shortDay: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.collectAnimalRobEnergy",
            "[{\"propId\":\"" + propId + "\",\"propType\":\"" + propType + "\",\"shortDay\":\"" + shortDay
                    + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    /* 复活能量 */
    suspend fun protectBubble(targetUserId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.protectBubble",
            "[{\"source\":\"ANT_FOREST_H5\",\"targetUserId\":\"" + targetUserId + "\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }

    /* 森林礼盒 */
    suspend fun collectFriendGiftBox(targetId: String, targetUserId: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.antforest.forest.h5.collectFriendGiftBox",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetId\":\"" + targetId
                    + "\",\"targetUserId\":\"" + targetUserId + "\"}]"
        )
    }

    suspend fun consultForSendEnergyByAction(sourceType: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.bizfmcg.greenlife.consultForSendEnergyByAction",
            "[{\"sourceType\":\"$sourceType\"}]"
        )
    }

    suspend fun sendEnergyByAction(sourceType: String): Result<JSONObject> {
        return RpcUtil.requestV2(
            "alipay.bizfmcg.greenlife.sendEnergyByAction",
            ("[{\"actionType\":\"GOODS_BROWSE\",\"requestId\":\"" + RandomUtils.getRandom(8)).toString() + "\",\"sourceType\":\"" + sourceType + "\"}]"
        )
    }

    /**
     * 今日动态
     */
    suspend fun queryDynamicsIndex(): Result<JSONObject> {
        // [{"autoRefresh":false,"source":"chInfo_ch_appcenter__chsub_9patch","version":"20221222"}]
        val json = mapOf(
            "autoRefresh" to false,
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to "20221222"
        ).toListJson()
        return RpcUtil.requestV2("alipay.antforest.forest.h5.queryDynamicsIndex", json)
    }

    /**
     * 找能量
     */
    suspend fun takeLook(exposedUserId: String? = null) : Result<JSONObject> {
        // [{"contactsStatus":"N","skipUsers":{},"source":"chInfo_ch_appcenter__chsub_9patch","version":"20231208"}]
        val json = mutableMapOf(
            "contactsStatus" to "N",
            "skipUsers" to mapOf<String, String>(),
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to "20231208"
        )
        if (!exposedUserId.isNullOrEmpty()) {
            json["exposedUserId"] = exposedUserId
        }

        return RpcUtil.requestV2("alipay.antforest.forest.h5.takeLook", json.toListJson())
    }

    suspend fun takeLookEnd() : Result<JSONObject> {
        // [{"contactsStatus":"N","source":"chInfo_ch_appcenter__chsub_9patch","version":"20240909"}]
        val json = mapOf(
            "contactsStatus" to "N",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to "20240909"
        )
        return RpcUtil.requestV2("alipay.antforest.forest.h5.takeLookEnd", json.toListJson())
    }
}