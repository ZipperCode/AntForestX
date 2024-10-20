package org.xposed.antforestx.core.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import org.xposed.antforestx.core.config.AntBasicConfig
import org.xposed.antforestx.core.config.AntConfig
import org.xposed.antforestx.core.config.AntForestConfig
import org.xposed.antforestx.core.config.AntManorConfig
import org.xposed.antforestx.core.config.AntOtherConfig
import org.xposed.antforestx.core.util.DateUtils
import org.xposed.antforestx.core.util.FileDataProvider

object ConfigManager {

    private val configFlow = MutableStateFlow(AntConfig())

    val basicConfig: AntBasicConfig get() = configFlow.value.basicConfig
    val forestConfig: AntForestConfig get() = configFlow.value.forestConfig
    val manorConfig: AntManorConfig get() = configFlow.value.manorConfig
    val otherConfig: AntOtherConfig get() = configFlow.value.otherConfig

    val enableToast: Boolean get() = configFlow.value.basicConfig.showToast

    val isLimitForestCollect: Boolean get() = configFlow.value.forestConfig.isCollectLimit

    val limitCountInMinute: Int get() = configFlow.value.forestConfig.limitCountInMinute

    /**
     * 保护古树
     *
     */
    val enableAncientTree: Boolean get() = forestConfig.isProtectAncientTree && isAncientTreeWeek

    val enableBookRead get() = otherConfig.enableReadListenBook

    /**
     * 是否可以使用双击卡
     */
    val canUseDoubleProp get() = forestConfig.useDoubleProp && DateUtils.checkInTime(forestConfig.useDoublePropTime)

    fun getConfig(): AntConfig {
        return configFlow.value
    }

    /**
     * 是否保护本周古树
     */
    private val isAncientTreeWeek: Boolean
        get() {
            if (!forestConfig.isAncientTreeOnlyWeek) {
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
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    runCatching {
                        FileDataProvider.saveAntConfig(it)
                    }
                }
        }
    }
}