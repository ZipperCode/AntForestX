package org.xposed.antforestx.core.manager

import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.xposed.antforestx.core.util.DateUtils
import org.xposed.forestx.core.utils.AppCoroutine
import org.zipper.antforestx.data.config.AntBasicConfig
import org.zipper.antforestx.data.config.AntConfig
import org.zipper.antforestx.data.config.AntForestConfig
import org.zipper.antforestx.data.config.AntManorConfig
import org.zipper.antforestx.data.config.AntOtherConfig
import org.zipper.antforestx.data.repository.IAntConfigRepository
import timber.log.Timber

object ConfigManager : KoinComponent {

    private val iAntConfigRepository: IAntConfigRepository by inject<IAntConfigRepository>()

    private var config: AntConfig = AntConfig()

    val basicConfig: AntBasicConfig get() = config.basicConfig
    val forestConfig: AntForestConfig get() = config.forestConfig
    val manorConfig: AntManorConfig get() = config.manorConfig
    val otherConfig: AntOtherConfig get() = config.otherConfig

    val enableToast: Boolean get() = config.basicConfig.showToast

    val isLimitForestCollect: Boolean get() = config.forestConfig.isCollectLimit

    val limitCountInMinute: Int get() = config.forestConfig.limitCountInMinute

    /**
     * 保护古树
     *
     */
    val enableAncientTree: Boolean get() = forestConfig.isProtectAncientTree && isAncientTreeWeek

    /**
     * 是否可以使用双击卡
     */
    val canUseDoubleProp get() = forestConfig.useDoubleProp && DateUtils.checkInTime(forestConfig.useDoublePropTime)

    fun getConfig(): AntConfig {
        return config
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

    suspend fun init() {
        AppCoroutine.launch {
            iAntConfigRepository.configFlow.collectLatest {
                config = it
            }
        }
    }
}