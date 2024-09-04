package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable

/**
 * 收取记录
 * @author zipper
 *
 * @param allCollected 总共收取
 * @param weekCollected 本周收取
 * @param dayCollected 今天收取
 * @param startTime 开始时间
 */
@Serializable
data class AntEnergyStatistics(
    val allCollected: Int = 0,
    val yearCollected: TimeStatistics = TimeStatistics(),
    val weekCollected: TimeStatistics = TimeStatistics(),
    val dayCollected: TimeStatistics = TimeStatistics(),
    val startTime: Long = 0
)

@Serializable
enum class CollectTimeType {
    YEAR, WEEK, TODAY
}

/**
 * 数据类型 次数、收能量、助力、浇水
 */
@Serializable
enum class CollectDataType {
    Collected, Helped, Watered
}

/**
 * 收取时间数据
 * @param timeType 时间类型
 * @param collect 收集能量
 * @param help 助力
 * @param water 浇水
 */
@Serializable
data class TimeStatistics(
    val timeType: CollectTimeType = CollectTimeType.TODAY,
    val collect: Detail = Detail(CollectDataType.Collected),
    val help: Detail = Detail(CollectDataType.Helped),
    val water: Detail = Detail(CollectDataType.Watered)
)

/**
 * 详情
 */
@Serializable
data class Detail(
    val dataType: CollectDataType,
    val times: Int = 0,
    val value: Int = 0
)