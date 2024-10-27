package org.zipper.antforestx.data.config

import kotlinx.serialization.Serializable

/**
 * 其他
 * @author zipper
 */
@Serializable
data class AntOtherConfig(
    /**
     * 会员签到
     */
    val enableSign: Boolean = true,
    /**
     * 积分任务
     */
    val enableIntegralTask: Boolean = true,
    /**
     * 收取积分
     */
    val enableCollectIntegral: Boolean = true,
    /**
     * 修改步数
     */
    val enableStep: Boolean = true,
    /**
     * 自定义步数
     */
    val customStepNum: Int = 50000,
    /**
     * 商家服务
     */
    val enableMerchant: Boolean = true,
    /**
     * 商家签到
     */
    val enableMerchantSign: Boolean = true,
    /**
     * 商家签到任务
     */
    val enableMerchantTask: Boolean = true,
)