package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable

/**
 * 庄园记录
 * @author zipper
 *
 * @param hasAnswer 是否回答过问题
 * @param feedFriendList 喂饲料列表
 * @param sendWheatList 送麦子列表
 * @param stallShareList 新村分享助力列表
 * @param stallHelpList 新村助力列表
 * @param dailyAnswerList 每日答题列表
 * @param donationEggList 捐赠鸡蛋列表
 * @param spreadManureList 运动助力
 */
@Serializable
data class AntManorDayRecord(
    val hasAnswer: Boolean = false,
    val feedFriendList: List<FeedFriend> = emptyList(),
    val sendWheatList: List<SendWheat> = emptyList(),
    val stallShareList: List<StallShare> = emptyList(),
    val stallHelpList: List<StallHelp> = emptyList(),
    val dailyAnswerList: Set<String> = emptySet(),
    val donationEggList: List<String> = emptyList(),
    val spreadManureList: List<String> = emptyList(),
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