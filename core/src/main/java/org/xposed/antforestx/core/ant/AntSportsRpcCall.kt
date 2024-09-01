package org.xposed.antforestx.core.ant

import kotlinx.serialization.json.JsonObject
import org.xposed.antforestx.core.ant.RpcUtil.request

object AntSportsRpcCall {
    private const val chInfo = "ch_appcenter__chsub_9patch"
    private const val timeZone = "Asia\\/Shanghai"
    private const val version = "3.0.1.2"
    private const val alipayAppVersion = "0.0.852"
    private const val cityCode = "330100"
    private const val appId = "2021002116659397"

    suspend fun queryCoinBubbleModule(): Result<JsonObject> {
        return request(
            "com.alipay.sportshealth.biz.rpc.sportsHealthHomeRpc.queryCoinBubbleModule",
            "[{\"bubbleId\":\"\",\"canAddHome\":false,\"chInfo\":\"" + chInfo
                    + "\",\"clientAuthStatus\":\"not_support\",\"clientOS\":\"android\",\"distributionChannel\":\"\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"]}]"
        )
    }

    suspend fun receiveCoinAsset(assetId: String, coinAmount: Int): Result<JsonObject> {
        return request(
            "com.alipay.sportshealth.biz.rpc.SportsHealthCoinCenterRpc.receiveCoinAsset",
            "[{\"assetId\":\"" + assetId
                    + "\",\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"coinAmount\":"
                    + coinAmount
                    + ",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"tracertPos\":\"首页金币收集\"}]"
        )
    }

    suspend fun queryMyHomePage(): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.map.queryMyHomePage", "[{\"alipayAppVersion\":\""
                    + alipayAppVersion + "\",\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"pathListUsePage\":true,\"timeZone\":\""
                    + timeZone + "\"}]"
        )
    }

    suspend fun join(pathId: String): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.map.join", "[{\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"pathId\":\""
                    + pathId + "\"}]"
        )
    }

    suspend fun openAndJoinFirst(): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.user.openAndJoinFirst", "[{\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"]}]"
        )
    }

    suspend fun go(day: String, rankCacheKey: String, stepCount: Int): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.map.go", "[{\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"day\":\"" + day
                    + "\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"needAllBox\":true,\"rankCacheKey\":\""
                    + rankCacheKey + "\",\"timeZone\":\"" + timeZone + "\",\"useStepCount\":" + stepCount
                    + "}]"
        )
    }

    suspend fun openTreasureBox(boxNo: String, userId: String): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.treasureBox.openTreasureBox", "[{\"boxNo\":\"" + boxNo
                    + "\",\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"userId\":\""
                    + userId + "\"}]"
        )
    }

    suspend fun queryBaseList(): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.path.queryBaseList", "[{\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"]}]"
        )
    }

    suspend fun queryProjectList(index: Int): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.charity.queryProjectList", "[{\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"index\":"
                    + index + ",\"projectListUseVertical\":true}]"
        )
    }

    suspend fun donate(donateCharityCoin: Int, projectId: String): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.charity.donate", "[{\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"donateCharityCoin\":" + donateCharityCoin
                    + ",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"projectId\":\""
                    + projectId + "\"}]"
        )
    }

    suspend fun queryWalkStep(): Result<JsonObject> {
        return request(
            "alipay.antsports.walk.user.queryWalkStep", "[{\"chInfo\":\"" + chInfo
                    + "\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"timeZone\":\""
                    + timeZone + "\"}]"
        )
    }

    suspend fun walkDonateSignInfo(count: Int): Result<JsonObject> {
        return request(
            "alipay.charity.mobile.donate.walk.walkDonateSignInfo",
            "[{\"needDonateAction\":false,\"source\":\"walkDonateHome\",\"steps\":" + count
                    + ",\"timezoneId\":\""
                    + timeZone + "\"}]"
        )
    }

    suspend fun donateWalkHome(count: Int): Result<JsonObject> {
        return request(
            "alipay.charity.mobile.donate.walk.home",
            "[{\"module\":\"3\",\"steps\":" + count + ",\"timezoneId\":\"" + timeZone + "\"}]"
        )
    }

    suspend fun exchange(actId: String, count: Int, donateToken: String): Result<JsonObject> {
        return request(
            "alipay.charity.mobile.donate.walk.exchange",
            "[{\"actId\":\"" + actId + "\",\"count\":"
                    + count + ",\"donateToken\":\"" + donateToken + "\",\"timezoneId\":\""
                    + timeZone + "\",\"ver\":0}]"
        )
    }

    /* 这个好像没用 */
    suspend fun exchangeSuccess(exchangeId: String): Result<JsonObject> {
        val args1 = ("[{\"exchangeId\":\"" + exchangeId
                + "\",\"timezone\":\"GMT+08:00\",\"version\":\"" + version + "\"}]")
        return request("alipay.charity.mobile.donate.exchange.success", args1)
    }

    /* 文体中心 */
    suspend fun userTaskGroupQuery(groupId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.sports.userTaskGroup.query",
            "[{\"cityCode\":\"" + cityCode + "\",\"groupId\":\"" + groupId + "\"}]"
        )
    }

    suspend fun userTaskComplete(bizType: String, taskId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.sports.userTask.complete",
            "[{\"bizType\":\"" + bizType + "\",\"cityCode\":\"" + cityCode + "\",\"completedTime\":"
                    + System.currentTimeMillis() + ",\"taskId\":\"" + taskId + "\"}]"
        )
    }

    suspend fun userTaskRightsReceive(taskId: String, userTaskId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.sports.userTaskRights.receive",
            "[{\"taskId\":\"$taskId\",\"userTaskId\":\"$userTaskId\"}]"
        )
    }

    suspend fun queryAccount(): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.user.asset.query.account",
            "[{\"accountType\":\"TIYU_SEED\"}]"
        )
    }

    suspend fun queryRoundList(): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.wenti.walk.queryRoundList",
            "[{}]"
        )
    }

    suspend fun participate(bettingPoints: Int, InstanceId: String, ResultId: String, roundId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.wenti.walk.participate",
            "[{\"bettingPoints\":" + bettingPoints + ",\"guessInstanceId\":\"" + InstanceId
                    + "\",\"guessResultId\":\"" + ResultId
                    + "\",\"newParticipant\":false,\"roundId\":\"" + roundId
                    + "\",\"stepTimeZone\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun pathFeatureQuery(): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.path.feature.query",
            "[{\"appId\":\"" + appId
                    + "\",\"features\":[\"USER_CURRENT_PATH_SIMPLE\"],\"sceneCode\":\"wenti_shijiebei\"}]"
        )
    }

    suspend fun pathMapJoin(pathId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.path.map.join",
            "[{\"appId\":\"" + appId + "\",\"pathId\":\"" + pathId + "\"}]"
        )
    }

    suspend fun pathMapHomepage(pathId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.path.map.homepage",
            "[{\"appId\":\"" + appId + "\",\"pathId\":\"" + pathId + "\"}]"
        )
    }

    suspend fun stepQuery(countDate: String, pathId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.path.map.step.query",
            "[{\"appId\":\"" + appId + "\",\"countDate\":\"" + countDate
                    + "\",\"pathId\":\""
                    + pathId + "\",\"timeZone\":\"Asia/Shanghai\"}]"
        )
    }

    suspend fun tiyubizGo(countDate: String, goStepCount: Int, pathId: String, userPathRecordId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.path.map.go",
            "[{\"appId\":\"" + appId + "\",\"countDate\":\"" + countDate
                    + "\",\"goStepCount\":"
                    + goStepCount + ",\"pathId\":\"" + pathId
                    + "\",\"timeZone\":\"Asia/Shanghai\",\"userPathRecordId\":\""
                    + userPathRecordId + "\"}]"
        )
    }

    suspend fun rewardReceive(pathId: String, userPathRewardId: String): Result<JsonObject> {
        return request(
            "alipay.tiyubiz.path.map.reward.receive",
            "[{\"appId\":\"" + appId + "\",\"pathId\":\"" + pathId + "\",\"userPathRewardId\":\""
                    + userPathRewardId + "\"}]"
        )
    }
}
