package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

/**
 * 蚂蚁庄园
 * @author zipper
 *
 * @param enable 是否启用
 * @param enableRewardFriend 是否启用奖励好友
 * @param enableRepatriateChicks 是否启用遣返小鸡
 * @param unRepatriateFriendList 不遣返好友列表
 * @param repatriateType 遣返小鸡类型
 * @param recallChicksType 召回小鸡类型
 * @param enableCollectPropReward 是否收集道具奖励
 * @param enableFixGameScore 是否修复游戏分数
 * @param enableChicksKitchen 是否启用小鸡厨房
 * @param useSpecialFoods 是否启用特殊食物
 * @param useNewEgg 是否启用新蛋
 * @param enableCollectEgg 是否收集蛋
 * @param enableDonateEgg 是否捐赠蛋
 * @param donateEggNum 捐赠蛋数量
 * @param enableAnswerQuestion 是否回答问题
 * @param enableCollectFeedReward 是否收集奖励
 * @param enableFeedChicks 是否喂养小鸡
 * @param useSpeedCard 是否使用加速卡
 * @param enableFeedFriendChicks 是否喂养好友小鸡
 * @param feedFriendChicksList 喂养好友小鸡列表
 * @param enableNotifyFriendChicks 是否通知好友小鸡
 * @param notNotifyFriendChicksList 不通知好友小鸡列表
 * @param enableCollectWheat 是否收集麦子
 * @param sendWheatFriendList 发送麦子好友列表
 * @param enableChicksDiary 是否启用小鸡日记
 * @param enableFarm 是否启用农场
 * @param collectFarmReward 是否收集农场奖励
 * @param farmFertilizeCount 农场施肥次数
 * @param enableVillage 是否启用蚂蚁新村
 * @param enableAutoCollectStall 是否启用自动收集摊位
 * @param enableAutoPutStall 是否启用自动放入摊位
 * @param enableAutoTask 是否启用自动任务
 * @param enableAutoReward 是否启用自动领取奖励
 * @param stallList 摊位列表
 * @param banStallList 禁用摊位列表
 * @param notExpelList 不驱赶列表
 * @param allowFriendTime 允许好友时间
 * @param ownStallTime 自己摆摊时间
 * @param enableAutoDonate 是否启用自动捐赠
 * @param enableVillageFeed 是否启用蚂蚁新村饲料
 */
@Serializable
data class AntManorConfig(
    val enable: Boolean = true,
    val enableRewardFriend: Boolean = true,
    val enableRepatriateChicks: Boolean = true,
    val repatriateType: RepatriateType = RepatriateType.Normal,
    val unRepatriateFriendList: List<String> = emptyList(),
    val enableRecallChicks: Boolean = true,
    val recallChicksType: RecallChicksType = RecallChicksType.Always,
    val enableCollectPropReward: Boolean = true,
    val enableFixGameScore: Boolean = true,
    val enableChicksKitchen: Boolean = true,
    val useSpecialFoods: Boolean = true,
    val useNewEgg: Boolean = true,
    val enableCollectEgg: Boolean = true,
    val enableDonateEgg: Boolean = false,
    val donateEggNum: Int = 1,
    val enableAnswerQuestion: Boolean = true,
    val enableCollectFeedReward: Boolean = true,
    val enableFeedChicks: Boolean = true,
    val useSpeedCard: Boolean = true,
    val enableFeedFriendChicks: Boolean = false,
    val feedFriendChicksList: List<String> = emptyList(),
    val enableNotifyFriendChicks: Boolean = false,
    val notNotifyFriendChicksList: List<String> = emptyList(),
    val enableCollectWheat: Boolean = true,
    val sendWheatFriendList: List<String> = emptyList(),
    val enableChicksDiary: Boolean = true,
    val enableFarm: Boolean = true,
    val collectFarmReward: Boolean = true,
    val farmFertilizeCount: Int = 5,
    /**
     * 蚂蚁新村
     */
    val enableVillage: Boolean = false,
    val enableAutoCollectStall: Boolean = false,
    val enableAutoPutStall: Boolean = false,
    val enableAutoTask: Boolean = false,
    val enableAutoReward: Boolean = false,
    val stallList: List<String> = emptyList(),
    val banStallList: List<String> = emptyList(),
    val notExpelList: List<String> = emptyList(),
    val allowFriendTime: Int = 120,
    val ownStallTime: Int = 120,
    val enableAutoDonate: Boolean = false,
    val enableVillageFeed: Boolean = false
)