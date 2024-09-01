package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

/**
 * 基础配置
 * @author zipper
 *
 * @param immediate 立即执行
 * @param recordLog 记录日志
 * @param showToast 显示Toast
 * @param keepAlive 保持唤醒
 * @param wakeupType 唤醒类型
 * @param enableOnGoing 通知禁删
 *
 */
@Serializable
data class AntBasicConfig(
    val immediate: Boolean = false,
    val recordLog: Boolean = false,
    val showToast: Boolean = false,
    val keepAlive: Boolean = false,
    val wakeupType: WakeupType = WakeupType.None,
    val enableOnGoing: Boolean = false,
    val timeoutRestart: Boolean = false
)