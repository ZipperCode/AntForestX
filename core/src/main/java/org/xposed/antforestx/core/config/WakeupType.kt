package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

/**
 * 唤醒方式
 * @author zipper
 */
@Serializable
enum class WakeupType {
    None,

    /// 广播
    Broadcast,

    /// 闹钟
    Alarm,

    /// 任务
    Work,
}