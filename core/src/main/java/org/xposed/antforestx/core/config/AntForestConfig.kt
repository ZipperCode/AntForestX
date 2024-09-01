package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

/**
 * 森林配置
 * @author zipper
 *
 * @param isCollectEnergy 是否收集能量
 * @param collectEnergyInterval 收集能量间隔
 * @param isCollectWateringBubble 是否收集浇水气泡
 * @param isBatchRobEnergy 是否批量抢能量
 * @param isCollectProp 是否收集道具
 * @param collectInterval 收集间隔
 * @param collectTimeout 收集超时时间
 * @param isForceCollect 是否强制收集
 * @param enableCollectLimit 是否限制收集数量
 * @param limitCountInMinute 限制收集数量
 * @param useDoubleProp 是否使用双倍道具
 * @param useDoublePropTime 使用双倍道具时间
 * @param useDoublePropLimit 使用双倍道具限制
 * @param noCollectUserList 不收集用户列表
 * @param isHelpFriendCollect 是否帮助好友收集
 * @param noHelpFriendList 不帮助好友列表
 * @param isReceiveForestTaskAward 是否领取森林任务奖励
 * @param waterFriendList 浇水好友列表
 * @param energyWaterEnum 能量浇水枚举
 * @param energyWaterFriendList 能量浇水好友列表
 * @param isCooperateWater 是否合种浇水
 * @param cooperateWaterList 合种浇水好友列表
 * @param cooperateWaterLimit 合种浇水限制
 * @param isProtectAncientTree 是否收集古树
 * @param ancientTreeCityCodeList 古树城市列表
 * @param ancientTreeOnlyWeek 是否只收集本周古树
 * @param isCollectEnergyRain 是否收集能量雨
 * @param energyFriendRainList 能量雨好友列表
 * @param isExchangeEnergyDoubleClick 是否活力值兑换双击卡
 * @param isExchangeEnergyShield 是否兑换能量盾
 * @param giveFriendPropList 赠送好友道具列表
 * @param isExchangeProtectLand 是否兑换保护地
 * @param protectLandList 保护地列表
 * @param isMagicalSpeciesCard 是否开启神奇物种
 * @param sendFriendCard 赠送好友卡片列表
 * @param enableMagicalOcean 是否开启神奇海洋
 * @param enableProtectOcean 是否开启保护海洋
 * @param protectOceanList 保护海洋列表
 * @param enablePatrolForest 是否开启巡护森林
 * @param enableDispatchAnim 是否开启派遣动物
 * @param enablerReceiveGiftBox 是否开启领取礼盒
 */
@Serializable
data class AntForestConfig(
    val isCollectEnergy: Boolean = true,
    val collectEnergyInterval: Int = 10,
    val isCollectWateringBubble: Boolean = true,
    val isBatchRobEnergy: Boolean = true,
    val isCollectProp: Boolean = true,
    val collectInterval: Long = 1000L,
    val collectTimeout: Int = 2,
    val isForceCollect: Boolean = false,
    val enableCollectLimit: Boolean = true,
    val limitCountInMinute: Int = 50,
    val useDoubleProp: Boolean = false,
    val useDoublePropTime: String = "0700-0730",
    val useDoublePropLimit: Int = 5,
    val noCollectUserList: List<String> = emptyList(),
    val isHelpFriendCollect: Boolean = true,
    val noHelpFriendList: List<String> = emptyList(),
    val isReceiveForestTaskAward: Boolean = true,
    val waterFriendList: List<String> = emptyList(),
    val energyWaterEnum: EnergyWaterEnum = EnergyWaterEnum.None,
    val energyWaterFriendList: List<String> = emptyList(),
    val isCooperateWater: Boolean = false,
    val cooperateWaterList: List<String> = emptyList(),
    val cooperateWaterLimit: Int = 0,
    val isProtectAncientTree: Boolean = false,
    val ancientTreeCityCodeList: List<String> = emptyList(),
    val ancientTreeOnlyWeek: Boolean = true,
    val isCollectEnergyRain: Boolean = true,
    val energyFriendRainList: List<String> = emptyList(),
    val isExchangeEnergyDoubleClick: Boolean = false,
    val exchangeDoubleClickLimit: Int = 6,
    val isExchangeEnergyShield: Boolean = false,
    val giveFriendPropList: List<String> = emptyList(),
    val isExchangeProtectLand: Boolean = false,
    val protectLandList: List<String> = emptyList(),
    val isMagicalSpeciesCard: Boolean = false,
    val sendFriendCard: List<String> = emptyList(),
    val enableMagicalOcean: Boolean = true,
    val enableProtectOcean: Boolean = true,
    val protectOceanList: List<String> = emptyList(),
    val enablePatrolForest: Boolean = true,
    val enableDispatchAnim: Boolean = true,
    val enablerReceiveGiftBox: Boolean = true

)