package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

/**
 * 其他
 * @author zipper
 * @param enableCollectIntegral 是否收集积分
 * @param enableMotionGiftBox 是否开启运动礼包
 * @param enableCollectMotionCoin 是否收集运动币
 * @param enableDonateMotionCoin 是否捐赠运动币
 * @param minDonateStepNum 最小捐赠步数
 * @param minDonateStepTime 最小捐赠步数时间
 * @param customStepNum 自定义步数
 * @param enableKouBeiSign 是否开启口碑签到
 * @param enableActionClockIn 是否开启行为打卡
 * @param enableCsCenter 是否开启文体中心
 * @param enableCollectSesameSeeds 是否收集芝麻粒
 * @param enableZhaoCaiSign 是否开启招财签到
 * @param enableBusinessOpenDoorSign 是否开启商家开门签到
 * @param enableGreenOperate 是否开启绿色操作
 * @param enableReadListenBook 是否开启阅读听书
 * @param enableConsumeCoin 是否消费运动币
 * @param enableGoldTicketSign 是否开启金券签到
 */
@Serializable
data class AntOtherConfig(
    val enableCollectIntegral: Boolean = true,
    val enableMotionGiftBox: Boolean = true,
    val enableCollectMotionCoin: Boolean = true,
    val enableDonateMotionCoin: Boolean = false,
    val minDonateStepNum: Int = 0,
    val minDonateStepTime: Int = 21,
    val customStepNum: Int = 50000,
    val enableKouBeiSign: Boolean = true,
    val enableActionClockIn: Boolean = true,
    val enableCsCenter: Boolean = true,
    val enableCollectSesameSeeds: Boolean = true,
    val enableZhaoCaiSign: Boolean = true,
    val enableBusinessOpenDoorSign: Boolean = true,
    val enableGreenOperate: Boolean = true,
    val enableReadListenBook: Boolean = false,
    val enableConsumeCoin: Boolean = true,
    val enableGoldTicketSign:Boolean = false
)