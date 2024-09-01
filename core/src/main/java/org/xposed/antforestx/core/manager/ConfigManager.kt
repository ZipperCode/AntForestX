package org.xposed.antforestx.core.manager

import kotlinx.coroutines.flow.MutableStateFlow
import org.xposed.antforestx.core.config.AntConfig

object ConfigManager {

    private val configFlow = MutableStateFlow(AntConfig())

    val enableToast: Boolean get() = configFlow.value.basicConfig.showToast

    val timeoutRestart: Boolean get() = configFlow.value.basicConfig.timeoutRestart

    val isLimitForestCollect: Boolean get() = configFlow.value.forestConfig.enableCollectLimit

    val limitCountInMinute: Int get() = configFlow.value.forestConfig.limitCountInMinute

    fun getConfig(): AntConfig {
        return configFlow.value
    }

}