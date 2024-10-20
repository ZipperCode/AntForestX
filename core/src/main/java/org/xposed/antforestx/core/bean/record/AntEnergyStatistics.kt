package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable

/**
 * 收取记录
 * @author zipper
 *
 * @param allCollected 总共收取
 * @param weekCollected 本周收取
 * @param startTime 开始时间
 */
@Serializable
data class AntEnergyStatistics(
    val allCollected: Int = 0,
    val yearCollected: TimeStatistics = TimeStatistics(CollectTimeType.YEAR),
    val monthCollected: TimeStatistics = TimeStatistics(CollectTimeType.MONTH),
    val weekCollected: TimeStatistics = TimeStatistics(CollectTimeType.WEEK),
    val startTime: Long = 0
)

@Serializable
enum class CollectTimeType {
    YEAR, MONTH,  WEEK, TODAY
}

/**
 * 收取时间数据
 * @param timeType 时间类型
 * @param collectEnergy 收集能量
 * @param collectTimes 收集次数
 * @param helpEnergy 助力能量
 * @param helpTimes 助力次数
 * @param waterValue 浇水值
 * @param waterTimes 浇水次数
 */
@Serializable
data class TimeStatistics(
    val timeType: CollectTimeType = CollectTimeType.TODAY,
    val collectEnergy: Int = 0,
    val collectTimes: Int = 0,
    val helpEnergy: Int = 0,
    val helpTimes: Int = 0,
    val waterValue: Int = 0,
    val waterTimes: Int = 0
)
