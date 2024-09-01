package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable

/**
 * 庄园记录
 * @author zipper
 */
@Serializable
data class AntManorRecord(
    val hasAnswer: Boolean,
    val feedFriendList: List<FeedFriend>,
    val sendWheatList: List<SendWheat>,
    val stallShareList: List<StallShare>,
    val stallHelpList: List<StallHelp>,
    val dailyAnswerList:Set<String>,
    val donationEggList: List<String>,
    val spreadManureList: List<String>,
)

/**
 * 好有喂饲料
 * @param userId 用户id
 * @param feedTimes 喂食次数
 */
@Serializable
data class FeedFriend(
    val userId: String,
    val feedTimes: Int
)

/**
 * 送麦子
 * @param userId 用户id
 * @param wheatCount 麦子数量
 */
@Serializable
data class SendWheat(
    val userId: String,
    val wheatCount: Int
)

/**
 * 新村分享助力
 * @param userId 用户id
 * @param shareId 分享id
 */
@Serializable
data class StallShare(
    val userId: String,
    val shareId: String
)

/**
 * 新村助力
 * @param userId 用户id
 * @param helpedCount 已助力次数
 * @param beHelpedCount 被助力次数
 */
@Serializable
data class StallHelp(
    val userId: String,
    val helpedCount: Int,
    val beHelpedCount: Int
)