package org.xposed.antforestx.core.manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.xposed.antforestx.core.config.AntConfig
import org.xposed.antforestx.core.config.AntForestConfig
import org.xposed.antforestx.core.util.DateUtils
import org.xposed.antforestx.core.util.FileDataProvider

object ConfigManager {

    private val configFlow = MutableStateFlow(AntConfig())

    val forestConfig: AntForestConfig get() = configFlow.value.forestConfig

    val enableToast: Boolean get() = configFlow.value.basicConfig.showToast

    val timeoutRestart: Boolean get() = configFlow.value.basicConfig.timeoutRestart

    val isLimitForestCollect: Boolean get() = configFlow.value.forestConfig.enableCollectLimit

    val limitCountInMinute: Int get() = configFlow.value.forestConfig.limitCountInMinute

    /**
     * 保护古树
     *
     */
    val enableAncientTree: Boolean get() = forestConfig.enableProtectAncientTree && isAncientTreeWeek

    fun getConfig(): AntConfig {
        return configFlow.value
    }

    /**
     * 是否保护本周古树
     */
    private val isAncientTreeWeek: Boolean
        get() {
            if (!forestConfig.ancientTreeOnlyWeek) {
                return true
            }
            val dayOfWeek = DateUtils.getDayOfWeek()
            return dayOfWeek.isMonday || dayOfWeek.isWednesday || dayOfWeek.isFriday
        }

    suspend fun init(): Result<Unit> {
        return runCatching {
            val config = FileDataProvider.loadAntConfig()
            if (config != null) {
                configFlow.value = config
            }
            configFlow
                .distinctUntilChanged { old, new -> old == new }
                .collectLatest {
                    runCatching {
                        FileDataProvider.saveAntConfig(it)
                    }
                }
        }
    }
}