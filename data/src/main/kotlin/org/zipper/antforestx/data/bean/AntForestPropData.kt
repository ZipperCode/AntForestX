package org.zipper.antforestx.data.bean

import kotlinx.serialization.Serializable

/**
 * 森林活力值道具数据
 */
typealias AntForestPropData = ArrayList<AntForestPropInfo>

@Serializable
data class AntForestPropInfo(
    val propId:String,
    val propType: String,
    val propName: String,
    val durationTime: Long,
    val holdsNum: Int,
    val propGroup: String,
    val recentExpireTime: Long
) {
    val isTimeDoubleProp: Boolean get() = propType == "TIME_ENERGY_DOUBLE_CLICK"
    val isDoubleProp: Boolean get() = propType == "ENERGY_DOUBLE_CLICK"
}