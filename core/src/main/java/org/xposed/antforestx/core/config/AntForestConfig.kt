package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

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
     * 收能量间隔
     */
    val collectInterval: Long = 1000L,
    /**
     * 是否收集浇水气泡
     */
    val isCollectWateringBubble: Boolean = true,
    /**
     * 是否批量抢能量
     */
    val isBatchRobEnergy: Boolean = true,
    /**
     * 是否收集道具
     */
    val isCollectProp: Boolean = true,
    /**
     * 是否收能量限制
     */
    val isCollectLimit: Boolean = true,
    /**
     * 每分钟限制收取
     */
    val limitCountInMinute: Int = 50,
    /**
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
     * 不收取用户列表
     */
    val noCollectUserList: List<String> = emptyList(),
    /**
     * 是否帮助好友收集
     */
    val isHelpFriendCollect: Boolean = true,
    /**
     * 不帮助好友列表
     */
    val noHelpFriendList: List<String> = emptyList(),
    /**
     * 是否启用森林任务
     */
    val isEnableForestTask: Boolean = true,
    /**
     * 给好友浇水列表
     */
    val waterFriendList: List<String> = emptyList(),
    /**
     * 浇水配置枚举
     */
    val energyWaterEnum: EnergyWaterEnum = EnergyWaterEnum.None,
    /**
     * 是否启用合种浇水
     */
    val isEnableCooperateWater: Boolean = false,
    /**
     * 合种浇水好友列表
     */
    val cooperateWaterList: List<String> = emptyList(),
    /**
     * 合种浇水限制
     */
    val cooperateWaterLimit: Int = 0,
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
    val isAncientTreeOnlyWeek: Boolean = true,
    /**
     * 是否收集能量雨
     */
    val isCollectEnergyRain: Boolean = true,
    /**
     * 能量雨好友列表
     */
    val energyFriendRainList: List<String> = emptyList(),

    /**
     * 是否兑换双击卡
     */
    val isExchangeDoubleClickProp: Boolean = false,
    /**
     * 双击卡兑换限制
     */
    val exchangeDoubleClickLimit: Int = 6,
    /**
     * 是否兑换能量盾
     */
    val isExchangeEnergyShieldProp: Boolean = false,
    /**
     * 赠送好友道具列表
     */
    val giveFriendPropList: List<String> = emptyList(),
    /**
     * 是否开启保护海洋
     */
    val enableProtectOcean: Boolean = true,
    /**
     * 保护海洋列表
     */
    val protectOceanList: List<String> = emptyList(),
    val enablerReceiveGiftBox: Boolean = true

)