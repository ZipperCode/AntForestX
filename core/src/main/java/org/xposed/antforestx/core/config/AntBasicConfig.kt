package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

/**
 * 基础配置
 * @author zipper
 *
 */
@Serializable
data class AntBasicConfig(
    /**
     * 是否记录日志
     */
    val enableLogcat: Boolean = false,
    /**
     * 是否显示Toast
     */
    val showToast: Boolean = false,
    /**
     * 是否保持唤醒
     */
    val keepAlive: Boolean = false,
    /**
     * 是否并行执行任务
     */
    val isParallel: Boolean = true
)