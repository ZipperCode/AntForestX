package org.zipper.antforestx.data.bean

import kotlinx.serialization.serializer
import org.json.JSONArray
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer

/**
 * 森林活力值道具数据
 */
class AntForestPropData : ArrayList<AntForestPropInfo> {

    constructor() : super()
    constructor(size: Int) : super(size)

    companion object {
        val dsSerializer: BaseDataStoreSerializer<AntForestPropData> by lazy {
            object : BaseDataStoreSerializer<AntForestPropData>(serializer()) {
                override val defaultValue: AntForestPropData
                    get() = AntForestPropData()
            }
        }
    }
}

data class AntForestPropInfo(
    val propType: String,
    val propName: String,
    val durationTime: Long,
    val holdsNum: Int,
    val propGroup: String,
    val propIdList: JSONArray,
    val recentExpireTime: Long
) {
    val isTimeDoubleProp: Boolean get() = propType == "TIME_ENERGY_DOUBLE_CLICK"
    val isDoubleProp: Boolean get() = propType == "ENERGY_DOUBLE_CLICK"
}