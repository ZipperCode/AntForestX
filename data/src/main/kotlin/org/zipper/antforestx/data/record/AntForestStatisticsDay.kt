package org.zipper.antforestx.data.record

import kotlinx.serialization.Serializable
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer

@Serializable
data class AntForestStatisticsDay(
    val dayOfYear: Int = 0,
    /**
     * 今天收集能量
     */
    val collectedEnergy: Int = 0,
    /**
     * 收能量次数
     */
    val collectedEnergyCount: Int = 0,
    /**
     * 获得活力值
     */
    val vitality: Int = 0,
    /**
     * 今天浇水数量
     */
    val watering: Int = 0,
    /**
     * 浇水次数
     */
    val wateringCount: Int = 0,
    /**
     * 合种浇水数量
     */
    val cooperateWatering: Int = 0,
    /**
     * 今天帮收能量
     */
    val helpEnergy: Int = 0,
    /**
     * 帮收次数
     */
    val helpEnergyCount: Int = 0,
    /**
     * 今天使用双击卡数量
     */
    val useDoublePropCount: Int = 0,
    /**
     * 今天兑换双击卡数量
     */
    val exchangedDoublePropCount: Int = 0,
    /**
     * 当天兑换能量罩数量
     */
    val exchangedShieldPropCount: Int = 0,
    /**
     * 是否送过好友道具
     */
    val sendFriendProp: Boolean = false,
    /**
     * 日期
     */
    val date: String = "yyy-MM-dd"
) {
    companion object {
        val dsSerializer: BaseDataStoreSerializer<AntForestStatisticsDay> by lazy {
            object : BaseDataStoreSerializer<AntForestStatisticsDay>(serializer()) {
                override val defaultValue: AntForestStatisticsDay
                    get() = AntForestStatisticsDay()
            }
        }
    }
}