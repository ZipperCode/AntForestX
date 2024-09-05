package org.xposed.antforestx.core.bean.record

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

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
 */
@Serializable
data class AntRecord(
    val userId: String = "",
    val energyStatistics: AntEnergyStatistics = AntEnergyStatistics(),
    val forestDayRecord: AntForestDayRecord = AntForestDayRecord(),
    val manorRecord: AntManorRecord = AntManorRecord(),
    val consumeGold: Long = 0
)