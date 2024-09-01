package org.xposed.antforestx.core.bean.record

/**
 * 森林记录
 *
 * @author zipper
 *
 * @param energyRecord 能量记录
 * @param forestDayRecord 当日森林记录
 */
data class AntForestRecord(
    val energyRecord: AntEnergyRecord,
    val forestDayRecord: AntForestDayRecord,
    val manorRecord: AntManorRecord
) {
}