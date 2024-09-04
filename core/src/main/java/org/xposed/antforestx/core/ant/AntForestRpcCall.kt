package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import org.json.JSONObject
import org.xposed.antforestx.core.util.RandomUtils
import org.xposed.antforestx.core.util.toListJson
import java.util.UUID

object AntForestRpcCall {

    private const val VERSION = "20231208"

    private fun getUniqueId(): String {
        return System.currentTimeMillis().toString() + RandomUtils.nextLong()
    }

    suspend fun fillUserRobFlag(userIdList: List<String>): Result<JSONObject> {
        val idsJson = Json.encodeToString(serializer(), userIdList)
        return RpcUtil.request("alipay.antforest.forest.h5.fillUserRobFlag", """[{"userIdList": $idsJson }]""")
    }

    suspend fun queryEnergyRanking(): Result<JSONObject> {
        val json = mapOf(
            "periodType" to "day",
            "rankType" to "energyRank",
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to VERSION,
        ).toListJson()
        return RpcUtil.request("alipay.antmember.forest.h5.queryEnergyRanking", json)
    }

    suspend fun queryHomePage(): Result<JSONObject> {
        val json = mapOf(
            "configVersionMap" to mapOf(
                "wateringBubbleConfig" to "10"
            ),
            "skipWhackMole" to false,
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "version" to VERSION,
        ).toListJson()
        return RpcUtil.request("alipay.antforest.forest.h5.queryHomePage", json)
    }

    suspend fun queryFriendHomePage(userId: String): Result<JSONObject> {
        val json = mapOf(
            "canRobFlags" to "F,F,F,F,F",
            "configVersionMap" to mapOf(
                "wateringBubbleConfig" to "0",
                "wateringBubbleConfig" to "10"
            ),
            "source" to "chInfo_ch_appcenter__chsub_9patch",
            "userId" to userId,
            "version" to VERSION
        ).toListJson()
        return RpcUtil.request("alipay.antforest.forest.h5.queryFriendHomePage", json)
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
        return RpcUtil.request("alipay.antmember.forest.h5.collectEnergy", args1)
    }

    suspend fun batchRobEnergy(userId: String, bubbleId: List<String?>?): Result<JSONObject> {
        val args1 = ("[{\"bizType\":\"\",\"bubbleIds\":[" + java.lang.String.join(",", bubbleId)
                + "],\"fromAct\":\"BATCH_ROB_ENERGY\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"userId\":\"" + userId + "\",\"version\":\""
                + VERSION + "\"}]")
        return RpcUtil.request("alipay.antmember.forest.h5.collectEnergy", args1)
    }

    suspend fun collectRebornEnergy(): Result<JSONObject> {
        return RpcUtil.request("alipay.antforest.forest.h5.collectRebornEnergy", "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]")
    }

    suspend fun transferEnergy(targetUser: String, bizNo: String, energyId: Int): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antmember.forest.h5.transferEnergy", "[{\"bizNo\":\"" +
                    bizNo + UUID.randomUUID().toString() + "\",\"energyId\":" + energyId +
                    ",\"extInfo\":{\"sendChat\":\"N\"},\"from\":\"friendIndex\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetUser\":\""
                    + targetUser + "\",\"transferType\":\"WATERING\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun forFriendCollectEnergy(targetUserId: String, bubbleId: Long): Result<JSONObject> {
        val args1 = "[{\"bubbleIds\":[$bubbleId],\"targetUserId\":\"$targetUserId\"}]"
        return RpcUtil.request("alipay.antmember.forest.h5.forFriendCollectEnergy", args1)
    }

    suspend fun vitalitySign(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.vitalitySign",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    suspend fun queryTaskList(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.queryTaskList",
            "[{\"extend\":{},\"fromAct\":\"home_task_list\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun queryEnergyRainHome(): Result<JSONObject> {
        return RpcUtil.request("alipay.antforest.forest.h5.queryEnergyRainHome", "[{\"version\":\"$VERSION\"}]")
    }

    suspend fun queryEnergyRainCanGrantList(): Result<JSONObject> {
        return RpcUtil.request("alipay.antforest.forest.h5.queryEnergyRainCanGrantList", "[{}]")
    }

    suspend fun grantEnergyRainChance(targetUserId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.grantEnergyRainChance",
            "[{\"targetUserId\":$targetUserId}]"
        )
    }

    suspend fun startEnergyRain(): Result<JSONObject> {
        return RpcUtil.request("alipay.antforest.forest.h5.startEnergyRain", "[{\"version\":\"$VERSION\"}]")
    }

    suspend fun energyRainSettlement(saveEnergy: Int, token: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.energyRainSettlement",
            "[{\"activityPropNums\":0,\"saveEnergy\":" + saveEnergy + ",\"token\":\"" + token + "\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun receiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.antiep.receiveTaskAward",
            "[{\"ignoreLimit\":false,\"requestType\":\"H5\",\"sceneCode\":\"" + sceneCode +
                    "\",\"source\":\"ANTFOREST\",\"taskType\":\"" + taskType + "\"}]"
        )
    }

    suspend fun finishTask(sceneCode: String, taskType: String): Result<JSONObject> {
        val outBizNo = taskType + "_" + RandomUtils.nextDouble()
        return RpcUtil.request(
            "com.alipay.antiep.finishTask",
            "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"H5\",\"sceneCode\":\"" +
                    sceneCode + "\",\"source\":\"ANTFOREST\",\"taskType\":\"" + taskType + "\"}]"
        )
    }

    suspend fun popupTask(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.popupTask",
            "[{\"fromAct\":\"pop_task\",\"needInitSign\":false,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"statusList\":[\"TODO\",\"FINISHED\"],\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun antiepSign(entityId: String, userId: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.antiep.sign",
            "[{\"entityId\":\"" + entityId
                    + "\",\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_ENERGY_SIGN\",\"source\":\"ANTFOREST\",\"userId\":\""
                    + userId + "\"}]"
        )
    }

    suspend fun queryPropList(onlyGive: Boolean): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.queryPropList",
            "[{\"onlyGive\":\"" + (if (onlyGive) "Y" else "")
                    + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun giveProp(giveConfigId: String, propId: String, targetUserId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.giveProp",
            "[{\"giveConfigId\":\"" + giveConfigId + "\",\"propId\":\"" + propId
                    + "\",\"source\":\"self_corner\",\"targetUserId\":\"" + targetUserId + "\"}]"
        )
    }

    suspend fun collectProp(giveConfigId: String, giveId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.collectProp",
            "[{\"giveConfigId\":\"" + giveConfigId + "\",\"giveId\":\"" + giveId
                    + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    suspend fun consumeProp(propId: String, propType: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.consumeProp",
            "[{\"propId\":\"" + propId + "\",\"propType\":\"" + propType +
                    "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"timezoneId\":\"Asia/Shanghai\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun itemList(labelType: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.antiep.itemList",
            "[{\"extendInfo\":\"{}\",\"labelType\":\"" + labelType
                    + "\",\"pageSize\":20,\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"source\":\"afEntry\",\"startIndex\":0}]"
        )
    }

    suspend fun itemDetail(spuId: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.antiep.itemDetail",
            "[{\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"source\":\"afEntry\",\"spuId\":\""
                    + spuId + "\"}]"
        )
    }

    suspend fun queryVitalityStoreIndex(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.queryVitalityStoreIndex",
            "[{\"source\":\"afEntry\"}]"
        )
    }

    suspend fun exchangeBenefit(spuId: String, skuId: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.antcommonweal.exchange.h5.exchangeBenefit",
            ("[{\"sceneCode\":\"ANTFOREST_VITALITY\",\"requestId\":\"" + System.currentTimeMillis()
                    + "_" + RandomUtils.getRandom(17)).toString() + "\",\"spuId\":\"" +
                    spuId + "\",\"skuId\":\"" + skuId + "\",\"source\":\"GOOD_DETAIL\"}]"
        )
    }

    suspend fun testH5Rpc(operationTpye: String?, requestDate: String?): Result<JSONObject> {
        return RpcUtil.request(operationTpye!!, requestDate!!)
    }


    /* 神奇物种 */
    suspend fun queryAnimalStatus(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.queryAnimalStatus",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    suspend fun antdodoHomePage(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.homePage",
            "[{}]"
        )
    }

    suspend fun taskEntrance(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.taskEntrance",
            "[{\"statusList\":[\"TODO\",\"FINISHED\"]}]"
        )
    }

    suspend fun antdodoCollect(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.collect",
            "[{}]"
        )
    }

    suspend fun antdodoTaskList(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.taskList",
            "[{}]"
        )
    }

    suspend fun antdodoFinishTask(sceneCode: String, taskType: String): Result<JSONObject> {
        val uniqueId: String = AntForestRpcCall.getUniqueId()
        return RpcUtil.request(
            "com.alipay.antiep.finishTask",
            "[{\"outBizNo\":\"" + uniqueId + "\",\"requestType\":\"rpc\",\"sceneCode\":\""
                    + sceneCode + "\",\"source\":\"af-biodiversity\",\"taskType\":\""
                    + taskType + "\",\"uniqueId\":\"" + uniqueId + "\"}]"
        )
    }

    suspend fun antdodoReceiveTaskAward(sceneCode: String, taskType: String): Result<JSONObject> {
        return RpcUtil.request(
            "com.alipay.antiep.receiveTaskAward",
            "[{\"ignoreLimit\":0,\"requestType\":\"rpc\",\"sceneCode\":\"" + sceneCode
                    + "\",\"source\":\"af-biodiversity\",\"taskType\":\"" + taskType
                    + "\"}]"
        )
    }

    suspend fun antdodoPropList(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.propList",
            "[{}]"
        )
    }

    suspend fun antdodoConsumeProp(propId: String, propType: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.consumeProp",
            "[{\"propId\":\"$propId\",\"propType\":\"$propType\"}]"
        )
    }

    suspend fun queryBookInfo(bookId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.queryBookInfo",
            "[{\"bookId\":\"$bookId\"}]"
        )
    }

    // 送卡片给好友
    suspend fun antdodoSocial(targetAnimalId: String, targetUserId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antdodo.rpc.h5.social",
            "[{\"actionCode\":\"GIFT_TO_FRIEND\",\"source\":\"GIFT_TO_FRIEND_FROM_CC\",\"targetAnimalId\":\""
                    + targetAnimalId + "\",\"targetUserId\":\"" + targetUserId
                    + "\",\"triggerTime\":\"" + System.currentTimeMillis() + "\"}]"
        )
    }

    /* 巡护保护地 */
    suspend fun queryUserPatrol(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.queryUserPatrol",
            "[{\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun queryMyPatrolRecord(): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.queryMyPatrolRecord",
            "[{\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun switchUserPatrol(targetPatrolId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.switchUserPatrol",
            "[{\"source\":\"ant_forest\",\"targetPatrolId\":$targetPatrolId,\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun patrolGo(nodeIndex: Int, patrolId: Int): Result<JSONObject> {
        return RpcUtil.request(
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
        return RpcUtil.request("alipay.antforest.forest.h5.patrolKeepGoing", args)
    }

    suspend fun exchangePatrolChance(costStep: Int): Result<JSONObject> {
        return RpcUtil.request(
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
        return RpcUtil.request("alipay.antforest.forest.h5.queryAnimalAndPiece", args)
    }

    suspend fun combineAnimalPiece(animalId: Int, piecePropIds: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.combineAnimalPiece",
            "[{\"animalId\":" + animalId + ",\"piecePropIds\":" + piecePropIds
                    + ",\"timezoneId\":\"Asia/Shanghai\",\"source\":\"ant_forest\"}]"
        )
    }

    suspend fun AnimalConsumeProp(propGroup: String, propId: String, propType: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.consumeProp",
            "[{\"propGroup\":\"" + propGroup + "\",\"propId\":\"" + propId + "\",\"propType\":\"" + propType
                    + "\",\"source\":\"ant_forest\",\"timezoneId\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun collectAnimalRobEnergy(propId: String, propType: String, shortDay: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.collectAnimalRobEnergy",
            "[{\"propId\":\"" + propId + "\",\"propType\":\"" + propType + "\",\"shortDay\":\"" + shortDay
                    + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]"
        )
    }

    /* 复活能量 */
    suspend fun protectBubble(targetUserId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.protectBubble",
            "[{\"source\":\"ANT_FOREST_H5\",\"targetUserId\":\"" + targetUserId + "\",\"version\":\"" + VERSION
                    + "\"}]"
        )
    }

    /* 森林礼盒 */
    suspend fun collectFriendGiftBox(targetId: String, targetUserId: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.antforest.forest.h5.collectFriendGiftBox",
            "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"targetId\":\"" + targetId
                    + "\",\"targetUserId\":\"" + targetUserId + "\"}]"
        )
    }

    suspend fun consultForSendEnergyByAction(sourceType: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.bizfmcg.greenlife.consultForSendEnergyByAction",
            "[{\"sourceType\":\"$sourceType\"}]"
        )
    }

    suspend fun sendEnergyByAction(sourceType: String): Result<JSONObject> {
        return RpcUtil.request(
            "alipay.bizfmcg.greenlife.sendEnergyByAction",
            ("[{\"actionType\":\"GOODS_BROWSE\",\"requestId\":\"" + RandomUtils.getRandom(8)).toString() + "\",\"sourceType\":\"" + sourceType + "\"}]"
        )
    }
}