package org.xposed.antforestx.core.manager

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.xposed.antforestx.core.ant.RpcUtil
import org.xposed.antforestx.core.bean.record.AntEnergyStatistics
import org.xposed.antforestx.core.bean.record.AntForestDayRecord
import org.xposed.antforestx.core.bean.record.AntManorDayRecord
import org.xposed.antforestx.core.bean.record.AntRecord
import org.xposed.antforestx.core.hooker.UserIndependentCacheHooker
import org.xposed.forestx.core.utils.AppCoroutine
import org.xposed.forestx.core.utils.param.AppGlobal
import org.xposed.forestx.core.utils.param.currentUserIdKey
import org.zipper.antforestx.data.bean.AlipayUser
import org.zipper.antforestx.data.record.AntForestStatisticsDay
import org.zipper.antforestx.data.repository.IAntConfigRepository
import org.zipper.antforestx.data.repository.IAntDataRepository
import org.zipper.antforestx.data.repository.IAntStatisticsRepository
import timber.log.Timber
import java.util.Calendar

object UserManager : KoinComponent {

    private val dataRepository: IAntDataRepository by inject<IAntDataRepository>()

    private val statisticsRepository: IAntStatisticsRepository by inject<IAntStatisticsRepository>()

    private val configRepository: IAntConfigRepository by inject<IAntConfigRepository>()

    private var alipayUserMap: Map<String, AlipayUser> = emptyMap()

    private val antRecordFlow = MutableStateFlow(AntRecord())

    var forestStatistics: AntForestStatisticsDay = AntForestStatisticsDay()
        private set

    val antRecord: AntRecord get() = antRecordFlow.value

    val forestDayRecord: AntForestDayRecord get() = antRecordFlow.value.forestDayRecord
    val manorRecord: AntManorDayRecord get() = antRecordFlow.value.manorRecord

    suspend fun init() {
        initFriendsData()
        AppCoroutine.launch {
            statisticsRepository.antForestStatisticsDayFlow.collectLatest {
                forestStatistics = it
            }
        }
    }

    /**
     * 好有信息
     */
    private suspend fun initFriendsData() {
        val userId = waitGetCurrentUid()
        AppGlobal[currentUserIdKey] = userId
        val result = UserIndependentCacheHooker.getFriendsMap()
        if (result.isSuccess) {
            val dataMap = result.getOrThrow()
            alipayUserMap = HashMap(dataMap)
            dataRepository.updateAlipayUserData {
                dataMap
            }
        }
    }

    fun getAlipayUserName(userId: String): String {
        return alipayUserMap[userId]?.friendNameInfo ?: ""
    }

    /**
     * 更新步数
     */
    fun updateNewStep(step: Int) {
        AppCoroutine.launch {
            statisticsRepository.updateForest {
                it.copy(stepNum = step)
            }
        }
    }

    /**
     * 双击卡使用次数+1
     */
    suspend fun updateDoubleClickUse() {
        statisticsRepository.updateForest {
            it.copy(useDoublePropCount = it.useDoublePropCount + 1)
        }
    }

    suspend fun updateEnergy(energy: Int) {
        statisticsRepository.updateForest {
            it.copy(
                collectedEnergy = it.collectedEnergy + energy,
                collectedEnergyCount = it.collectedEnergyCount + 1
            )
        }
    }

    suspend fun updateHelpEnergy(energy: Int) {
        statisticsRepository.updateForest {
            it.copy(
                helpEnergy = it.helpEnergy + energy,
                helpEnergyCount = it.helpEnergyCount + 1
            )
        }
    }

    suspend fun updateVitality(vitality: Int) {
        statisticsRepository.updateForest {
            it.copy(
                vitality = it.vitality + vitality,
            )
        }
    }

    suspend fun updateWater(water: Int) {
        statisticsRepository.updateForest {
            it.copy(
                watering = it.watering + water,
                wateringCount = it.wateringCount + 1
            )
        }
    }

    suspend fun updateCooperateWater(water: Int) {
        statisticsRepository.updateForest {
            it.copy(
                cooperateWatering = it.cooperateWatering + water,
            )
        }
    }


    private fun validateTimeRecord() {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        if (dayOfYear != antRecord.forestDayRecord.dayOfYear) {
            // 重置
            antRecordFlow.value = antRecord.copy(
                forestDayRecord = AntForestDayRecord(dayOfYear = dayOfYear)
            )
        }
    }

    fun validateUser(): Boolean {
        val userId = RpcUtil.getUserId()
        return !userId.isNullOrEmpty()
    }

    /**
     * 获取当前用户
     */
    suspend fun waitGetCurrentUid(): String {
        var count = 0
        do {
            val userId = RpcUtil.getUserId()
            if (!userId.isNullOrEmpty()) {
                return userId
            }
            count++
            delay(3000)
        } while (count < 3)
        throw IllegalArgumentException("waitGetCurrentUid isnull")
    }


    fun canAncientTreeToday(cityCode: String): Boolean {
        return forestDayRecord.protectedAncientTreeList.contains(cityCode)
    }

    /**
     * 更新当前用户拥有的芝麻粒
     */
    fun updateUserCreditPoints(points: Int) {
        antRecordFlow.value = antRecordFlow.value.copy(
            creditPoints = points
        )
    }

}