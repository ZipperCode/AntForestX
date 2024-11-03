package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable

/**
 * 蚂蚁记录
 *
 * @author zipper
 *
 * @param energyStatistics 能量记录
 * @param forestDayRecord 当日森林记录
 * @param manorRecord 庄园记录
 *
 * @param consumeGold 消耗金币
 * @param creditPoints 信用积分
 * @param totalVitalityAmount 总共活力值
 */
@Serializable
data class AntRecord(
    val userId: String = "",
    val energyStatistics: AntEnergyStatistics = AntEnergyStatistics(),
    val forestDayRecord: AntForestDayRecord = AntForestDayRecord(),
    val manorRecord: AntManorDayRecord = AntManorDayRecord(),
    val consumeGold: Long = 0,
    val creditPoints: Int = 0,
    val totalVitalityAmount: Int = 0
)