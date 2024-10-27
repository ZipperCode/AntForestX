package org.xposed.antforestx.core.manager

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.xposed.antforestx.core.util.DateUtils
import org.zipper.antforestx.data.repository.IAntConfigRepository
import org.zipper.antforestx.data.repository.IAntStatisticsRepository

/**
 * 记录管理
 */
object RecordManager : KoinComponent {

    private val statisticsRepository: IAntStatisticsRepository by inject<IAntStatisticsRepository>()

    private val configRepository: IAntConfigRepository by inject<IAntConfigRepository>()

    suspend fun addEnergy(energy: Int) {
        statisticsRepository.updateForest {
            it.copy(
                collectedEnergy = it.collectedEnergy + energy,
                collectedEnergyCount = it.collectedEnergyCount + 1,
            )
        }
    }

    suspend fun updateUseDoubleCard() {
        statisticsRepository.updateForest {
            it.copy(useDoublePropCount = it.useDoublePropCount + 1)
        }
    }

    /**
     * 是否可以使用双击卡
     */
    suspend fun canUseDoublePropToday(): Boolean {
        val completableDeferred = CompletableDeferred(false)
        combine(
            statisticsRepository.antForestStatisticsDayFlow,
            configRepository.configFlow
        ) { statistics, config ->
            if (!config.forestConfig.useDoubleProp) {
                return@combine false
            }
            if (!DateUtils.checkInTime(config.forestConfig.useDoublePropTime)) {
                return@combine false
            }
            return@combine statistics.useDoublePropCount < config.forestConfig.useDoublePropLimit
        }.collectLatest {
            if (completableDeferred.isCompleted) {
                return@collectLatest
            }
            completableDeferred.complete(it)
        }
        return completableDeferred.await()
    }
}