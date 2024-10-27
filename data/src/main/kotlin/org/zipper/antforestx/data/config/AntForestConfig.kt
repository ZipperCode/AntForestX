package org.zipper.antforestx.data.config

import kotlinx.serialization.Serializable
import org.zipper.antforestx.data.enums.EnergyWaterEnum

/**
 * 森林配置
 * @author zipper
 */
@Serializable
data class AntForestConfig(
    /**
     * 是否收能量
     */
    val isCollectEnergy: Boolean = true,
    /**
     * 是否批量抢能量
     */
    val isBatchRobEnergy: Boolean = true,
    /**
     * 收能量间隔
     */
    val collectInterval: Long = 1000L,
    /**
     * 是否收能量限制
     */
    val isCollectLimit: Boolean = true,
    /**
     * 每分钟限制收取
     */
    val limitCountInMinute: Int = 50,
    /**
     * 是否收取好友能量
     */
    val enableCollectFriends: Boolean = true,
    /**
     * 不收取用户列表
     */
    val unCollectFriendList: List<String> = emptyList(),

    /**
     * 是否帮助好友收集
     */
    val isHelpFriendCollect: Boolean = true,

    /**
     * 道具配置
     * =========
     *
     * 是否使用双倍道具
     */
    val useDoubleProp: Boolean = false,
    /**
     * 是否使用双倍道具时间
     */
    val useDoublePropTime: String = "0700-0730",
    /**
     * 使用双击道具限制
     */
    val useDoublePropLimit: Int = 1,
    /**
     * 使用能量盾
     */
    val enableEnergyShieldProp: Boolean = false,
    /**
     * 是否送好友道具
     */
    val enableSendFriendProp: Boolean = false,
    /**
     * 赠送好友道具列表
     */
    val sendFriendPropList: List<String> = emptyList(),
    /**
     * 送好友列表
     */
    val sendPropFriends: List<String> = emptyList(),
    /**
     * 是否兑换双击卡
     */
    val enableExchangeDoubleProp: Boolean = false,
    /**
     * 双击卡兑换限制
     */
    val exchangeDoubleLimit: Int = 6,
    /**
     * 是否兑换能量盾
     */
    val enableExchangeShieldProp: Boolean = false,
    /**
     * 兑换能量盾限制
     */
    val exchangeShieldLimit: Int = 1,

    /**
     * 是否启用森林任务
     * =======
     */
    val enableForestTask: Boolean = true,
    /**
     * 是否收集能量雨
     */
    val isCollectEnergyRain: Boolean = true,
    /**
     * 能量雨好友列表
     */
    val energyFriendRainList: List<String> = emptyList(),

    /**
     * 是否给好友浇水
     * ============
     */
    val enableWateringFriend: Boolean = false,
    /**
     * 给好友浇水列表
     */
    val waterFriendList: List<String> = emptyList(),
    /**
     * 浇水配置枚举
     */
    val wateringEnum: EnergyWaterEnum = EnergyWaterEnum.None,
    /**
     * 是否启用合种浇水
     */
    val enableCooperateWater: Boolean = false,
    /**
     * 可浇水的合种数列表
     */
    val cooperateTreeList: List<String> = emptyList(),
    /**
     * 合种浇水限制
     */
    val cooperateWaterLimit: Int = 0,
    /**
     * 是否开启保护海洋
     * =============
     */
    val enableProtectOcean: Boolean = true,
    /**
     * 是否做海洋任务
     */
    val enableOceanTask: Boolean = true,
    /**
     * 捡垃圾
     */
    val enableCollectGarbage: Boolean = false,

    /**
     * 是否保护古树
     */
    val isProtectAncientTree: Boolean = false,
    /**
     * 古树城市列表
     */
    val ancientTreeCityCodeList: List<String> = emptyList(),
    /**
     * 是否只收集本周古树
     */
    val isAncientTreeOnlyWeek: Boolean = true


)