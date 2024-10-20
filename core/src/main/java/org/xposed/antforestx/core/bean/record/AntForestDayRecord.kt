package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable

/**
 * 当日森林记录
 *
 * @author zipper
 *
 * @param dayOfYear 日期
 * @param collectStatistics 当日收取记录
 * @param useDoubleClickCount 当天使用双击卡次数
 * @param friendWaterList 好友浇水记录
 * @param cooperateWaterList 合种浇水列表
 * @param protectedAreaList 保护地列表
 * @param protectedAncientTreeList 保护古数 value 为 cityCode
 * @param isProtectedEnergyBubble 是否保护能量气泡
 * @param isSyncStep 当日是否同步步数
 * @param todayStepNum 当日步数
 * @param isDonateStep 当日是否捐赠步数
 * @param isExchangedDoubleCard 是否兑换双卡
 * @param exchangedDoubleCardCount 已兑换双卡次数
 * @param isExchangedShield 是否兑换护盾
 * @param exchangedShieldCount 已兑换护盾次数
 */
@Serializable
data class AntForestDayRecord(
    val dayOfYear: Int = 1,
    val collectStatistics: TimeStatistics = TimeStatistics(CollectTimeType.TODAY),
    val friendWaterList: List<FriendWaterRecord> = emptyList(),
    val cooperateWaterList: List<String> = emptyList(),
    val protectedAreaList: List<ProtectedAreaRecord> = emptyList(),
    val protectedAncientTreeList: List<String> = emptyList(),
    val isProtectedEnergyBubble: Boolean = false,
    val isSyncStep: Boolean = false,
    val todayStepNum: Int = 0,
    val isDonateStep: Boolean = false,
    val isExchangedDoubleCard: Boolean = false,
    val exchangedDoubleCardCount: Int = 0,
    val useDoubleClickCount: Int = 0,
    val isExchangedShield: Boolean = false,
    val exchangedShieldCount: Int = 0,
)

/**
 * 好友浇水
 */
@Serializable
data class FriendWaterRecord(
    val userId: String,
    val friendName: String,
    val waterNum: Int
)

/**
 * 保护地
 */
@Serializable
data class ProtectedAreaRecord(
    val projectId: String,
    val applyCount: Int
)