package org.zipper.antforestx.data.config

import kotlinx.serialization.Serializable
import org.zipper.antforestx.data.enums.RecallChicksType
import org.zipper.antforestx.data.enums.RepatriateType

/**
 * 蚂蚁庄园
 * @author zipper
 */
@Serializable
data class AntManorConfig(
    /**
     * 是否启用
     */
    val enable: Boolean = true,
    /**
     * 是否打赏好友奖励
     */
    val isRewardFriend: Boolean = true,
    /**
     * 是否遣返小鸡
     */
    val isRepatriateChicks: Boolean = true,
    /**
     * 遣返小鸡类型
     */
    val repatriateType: RepatriateType = RepatriateType.Normal,
    /**
     * 不遣返好友列表
     */
    val unRepatriateFriendList: List<String> = emptyList(),
    /**
     * 是否召回小鸡
     */
    val isRecallChicks: Boolean = true,
    /**
     * 召回小鸡类型
     */
    val recallChicksType: RecallChicksType = RecallChicksType.Always,
    /**
     * 收集道具奖励
     */
    val isCollectPropReward: Boolean = true,
    /**
     * 启用小鸡厨房
     */
    val enableChicksKitchen: Boolean = true,
    /**
     * 是否使用特殊食物
     */
    val isUseSpecialFoods: Boolean = true,
    /**
     * 启用收取爱心鸡蛋
     */
    val enableCollectEgg: Boolean = true,
    /**
     * 是否捐蛋
     */
    val enableDonateEgg: Boolean = false,
    /**
     * 捐蛋数量
     */
    val donateEggNumLimit: Int = 1,
    /**
     * 启用庄园小课堂
     */
    val enableAnswerQuestion: Boolean = true,
    /**
     * 是否收取任务奖励
     */
    val isCollectFeedReward: Boolean = true,
    /**
     * 启用喂鸡
     */
    val enableFeedChicks: Boolean = true,
    /**
     * 是否使用加速卡
     */
    val isUseSpeedCard: Boolean = true,
    /**
     * 是否帮好友喂鸡
     */
    val isFeedFriendChicks: Boolean = false,
    /**
     * 帮好友喂鸡列表
     */
    val feedFriendChicksList: List<String> = emptyList(),
    /**
     * 通知好友小鸡
     */
    val isNotifyFriendChicks: Boolean = false,
    /**
     * 不通知好友小鸡列表
     */
    val notNotifyFriendChicksList: List<String> = emptyList(),
    /**
     * 启用收麦子
     */
    val enableCollectWheat: Boolean = true,
    /**
     * 发送麦子好友列表
     */
    val sendWheatFriendList: List<String> = emptyList(),
    /**
     * 启用小鸡日记
     */
    val enableChicksDiary: Boolean = true,
    /**
     * 是否做任务
     */
    val enableChicksTask: Boolean = true,
    /**
     * 启用农场
     */
    val enableFarm: Boolean = true,
    /**
     * 收农场奖励
     */
    val collectFarmReward: Boolean = true,
    /**
     * 农场施肥次数
     */
    val farmFertilizeCount: Int = 5,
    /**
     * 是否捉鸡
     */
    val isCatchChicks: Boolean = true,
    /**
     * 是否做农场任务
     */
    val doFarmTask: Boolean = true
)