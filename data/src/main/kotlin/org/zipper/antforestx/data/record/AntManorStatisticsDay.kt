package org.zipper.antforestx.data.record

import kotlinx.serialization.Serializable
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer

@Serializable
data class AntManorStatisticsDay(
    val dayOfYear: Int = 0,
    /**
     * 喂食次数
     */
    val feedCount: Int = 0,
    /**
     * 收便便
     */
    val collectPoopCount: Int = 0,
    /**
     * 农场施肥次数
     */
    val fertilizeCount: Int = 0,
    /**
     * 日期
     */
    val date: String = "yyy-MM-dd"
) {
    companion object {
        val dsSerializer: BaseDataStoreSerializer<AntManorStatisticsDay> by lazy {
            object : BaseDataStoreSerializer<AntManorStatisticsDay>(serializer()) {
                override val defaultValue: AntManorStatisticsDay
                    get() = AntManorStatisticsDay()
            }
        }
    }
}


