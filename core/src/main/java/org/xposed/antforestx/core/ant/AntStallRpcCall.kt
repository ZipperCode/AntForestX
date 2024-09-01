package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.json.JSONArray
import org.xposed.antforestx.core.ant.RpcUtil.request
import java.util.UUID

/**
 * @author Constanline
 * @since 2023/08/22
 */
object AntStallRpcCall {
    private const val VERSION = "0.1.2312271038.27"

    suspend fun home(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.self.home",
            "[{\"arouseAppParams\":{},\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun settle(assetId: String, settleCoin: Int): Result<JsonObject> {
        return request(
            "com.alipay.antstall.self.settle",
            "[{\"assetId\":\"" + assetId + "\",\"coinType\":\"MASTER\",\"settleCoin\":" + settleCoin +
                    ",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun shopList(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.shop.list",
            "[{\"freeTop\":false,\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun preOneKeyClose(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.shop.close.preOneKey",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun oneKeyClose(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.shop.oneKeyClose",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun preShopClose(shopId: String, billNo: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.shop.close.pre",
            "[{\"billNo\":\"" + billNo + "\",\"shopId\":\"" + shopId +
                    "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun shopClose(shopId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.shop.close",
            "[{\"shopId\":\"" + shopId + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun oneKeyOpen(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.shop.oneKeyOpen",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun shopOpen(friendSeatId: String, friendUserId: String, shopId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.shop.open",
            "[{\"friendSeatId\":\"" + friendSeatId + "\",\"friendUserId\":\"" + friendUserId + "\",\"shopId\":\"" +
                    shopId + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun rankCoinDonate(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.rank.coin.donate",
            "[{\"source\":\"ANTFARM\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun friendHome(userId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.friend.home",
            "[{\"arouseAppParams\":{},\"friendUserId\":\"" + userId +
                    "\",\"source\":\"ANTFARM\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }


    suspend fun taskList(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.task.list",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun signToday(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.sign.today",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun finishTask(outBizNo: String, taskType: String): Result<JsonObject> {
        return request(
            "com.alipay.antiep.finishTask",
            "[{\"outBizNo\":\"" + outBizNo +
                    "\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_TASK\",\"source\":\"AST\",\"systemType\":\"android\",\"taskType\":\"" +
                    taskType + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun receiveTaskAward(taskType: String): Result<JsonObject> {
        return request(
            "com.alipay.antiep.receiveTaskAward",
            "[{\"ignoreLimit\":true,\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_TASK\",\"source\":\"AST\",\"systemType\":\"android\",\"taskType\":\"" +
                    taskType + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun taskFinish(taskType: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.task.finish",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"taskType\":\"" + taskType + "\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun taskAward(amount: String, prizeId: String, taskType: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.task.award",
            "[{\"amount\":" + amount + ",\"prizeId\":\"" + prizeId +
                    "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"taskType\":\""
                    + taskType + "\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun taskBenefit(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.task.benefit",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun collectManure(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.manure.collectManure",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun queryManureInfo(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.manure.queryManureInfo",
            "[{\"queryManureType\":\"ANTSTALL\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" +
                    VERSION + "\"}]"
        )
    }

    suspend fun projectList(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.project.list",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun projectDetail(projectId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.project.detail",
            "[{\"projectId\":\"" + projectId +
                    "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun projectDonate(projectId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.project.donate",
            "[{\"bizNo\":\"" + UUID.randomUUID().toString() + "\",\"projectId\":\"" + projectId +
                    "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun roadmap(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.village.roadmap",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun nextVillage(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.ast.next.village",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun rankInviteRegister(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.rank.invite.register",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun friendInviteRegister(friendUserId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.friend.invite.register",
            "[{\"friendUserId\":\"" + friendUserId
                    + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    /* 助力好友 */
    suspend fun shareP2P(): Result<JsonObject> {
        return request(
            "com.alipay.antiep.shareP2P",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_P2P_SHARER\",\"source\":\"ANTSTALL\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun achieveBeShareP2P(shareId: String): Result<JsonObject> {
        return request(
            "com.alipay.antiep.achieveBeShareP2P",
            "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_P2P_SHARER\",\"shareId\":\""
                    + shareId
                    + "\",\"source\":\"ANTSTALL\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun shopSendBackPre(billNo: String, seatId: String, shopId: String, shopUserId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.friend.shop.sendback.pre",
            "[{\"billNo\":\"" + billNo + "\",\"seatId\":\"" + seatId + "\",\"shopId\":\"" + shopId
                    + "\",\"shopUserId\":\"" + shopUserId
                    + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun shopSendBack(seatId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.friend.shop.sendback",
            "[{\"seatId\":\"" + seatId
                    + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun rankInviteOpen(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.rank.invite.open",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun oneKeyInviteOpenShop(friendUserId: String, mySeatId: String): Result<JsonObject> {
        return request(
            "com.alipay.antstall.user.shop.oneKeyInviteOpenShop",
            "[{\"friendUserId\":\"" + friendUserId + "\",\"mySeatId\":\"" + mySeatId
                    + "\",\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun dynamicLoss(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.dynamic.loss",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"" + VERSION + "\"}]"
        )
    }

    suspend fun throwManure(dynamicList: JSONArray): Result<JsonObject> {
        return request(
            "com.alipay.antstall.manure.throwManure",
            "[{\"dynamicList\":" + dynamicList
                    + ",\"sendMsg\":false,\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }

    suspend fun settleReceivable(): Result<JsonObject> {
        return request(
            "com.alipay.antstall.self.settle.receivable",
            "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\""
                    + VERSION + "\"}]"
        )
    }
}
