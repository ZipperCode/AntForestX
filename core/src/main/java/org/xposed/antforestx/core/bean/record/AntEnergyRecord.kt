package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable

/**
 * 收取记录
 * @author zipper
 *
 * @param name 名称
 * @param allCollect 总共收取
 * @param weekCollect 本周收取
 * @param dayCollect 今天收取
 * @param startTime 开始时间
 */
@Serializable
data class AntEnergyRecord(
    val name: String,
    val allCollect: Int,
    val yearCollect: TimeStatistics,
    val weekCollect: TimeStatistics,
    val dayCollect: TimeStatistics,
    val startTime: Long
)

@Serializable
enum class CollectTimeType {
    YEAR, WEEK, TODAY
}

@Serializable
enum class CollectDataType {
    ALL, YEAR, WEEK, TODAY
}

/**
 * 收取时间数据
 * @param timeType 时间类型
 * @param dataType 数据类型
 * @param collectNum 收取数量
 * @param helpNum 帮助数量
 * @param waterNum 浇水数量
 * @param times 次数
 */
@Serializable
data class TimeStatistics(
    val timeType: CollectTimeType,
    val dataType: CollectDataType,
    val collectNum: Int,
    val helpNum: Int,
    val waterNum: Int,
    val times: Int
)