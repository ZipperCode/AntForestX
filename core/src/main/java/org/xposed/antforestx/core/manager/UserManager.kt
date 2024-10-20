package org.xposed.antforestx.core.manager

import android.graphics.Bitmap.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.ant.RpcUtil
import org.xposed.antforestx.core.bean.record.AntEnergyStatistics
import org.xposed.antforestx.core.bean.record.AntForestDayRecord
import org.xposed.antforestx.core.bean.record.AntManorDayRecord
import org.xposed.antforestx.core.bean.record.AntRecord
import org.xposed.antforestx.core.hooker.UserIndependentCacheHooker
import org.xposed.antforestx.core.util.FileDataProvider
import java.util.Calendar

object UserManager {

    private var friendIdMap: Map<String, String> = emptyMap()

    private val antRecordFlow = MutableStateFlow(AntRecord())

    val antRecord: AntRecord get() = antRecordFlow.value

    val energyStatistics: AntEnergyStatistics get() = antRecordFlow.value.energyStatistics
    val forestDayRecord: AntForestDayRecord get() = antRecordFlow.value.forestDayRecord
    val manorRecord: AntManorDayRecord get() = antRecordFlow.value.manorRecord

    val canDoubleToday: Boolean get() = forestDayRecord.useDoubleClickCount < ConfigManager.forestConfig.useDoublePropLimit

    suspend fun init(): Result<Unit> {
        return runCatching {
            val userId = waitGetCurrentUid()
            val result = FileDataProvider.loadAntRecord(userId)
            if (result != null) {
                antRecordFlow.value = result
            }
            antRecordFlow
                .distinctUntilChanged { old, new -> old == new }
                .flowOn(Dispatchers.IO)
                .collectLatest {
                    runCatching {
                        // 配置改变，同步保存本地
                        FileDataProvider.saveAntRecord(waitGetCurrentUid(), it)
                    }
                }
        }
    }

    fun updateNewRecord(newRecord: AntRecord) {
        antRecordFlow.value = newRecord
    }

    fun updateNewStep(step: Int) {
        val newRecord = antRecord.copy(
            forestDayRecord = forestDayRecord.copy(todayStepNum = step)
        )
        antRecordFlow.value = newRecord
    }

    /**
     * 双击卡使用次数+1
     */
    fun updateDoubleClickUse() {
        antRecordFlow.value = antRecord.copy(
            forestDayRecord = forestDayRecord.copy(
                useDoubleClickCount = forestDayRecord.useDoubleClickCount + 1
            )
        )
    }

    /**
     * 增加能量
     */
    fun addNewEnergy(energy: Int) {
        validateTimeRecord()
        val yearStatistics = energyStatistics.yearCollected
        val newYearStatistics =
            yearStatistics.copy(collectEnergy = yearStatistics.collectEnergy + energy, collectTimes = yearStatistics.collectTimes + 1)
        val monthStatistics = energyStatistics.yearCollected
        val newMonthStatistics =
            monthStatistics.copy(collectEnergy = monthStatistics.collectEnergy + energy, collectTimes = monthStatistics.collectTimes + 1)
        val weekStatistics = energyStatistics.weekCollected
        val newWeekStatistics =
            weekStatistics.copy(collectEnergy = weekStatistics.collectEnergy + energy, collectTimes = weekStatistics.collectTimes + 1)
        val dayStatistics = forestDayRecord.collectStatistics
        val newStatistics =
            dayStatistics.copy(collectEnergy = dayStatistics.collectEnergy + energy, collectTimes = dayStatistics.collectTimes + 1)

        val newEnergyStatistics = energyStatistics.copy(
            allCollected = energyStatistics.allCollected + energy,
            yearCollected = newYearStatistics,
            monthCollected = newMonthStatistics,
            weekCollected = newWeekStatistics,
        )
        antRecordFlow.value = antRecordFlow.value.copy(
            energyStatistics = newEnergyStatistics,
            forestDayRecord = forestDayRecord.copy(
                collectStatistics = newStatistics
            )
        )
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

    /**
     * 获取所有好友信息，并保存
     */
    suspend fun hookLoadAllFriends() = withContext(Dispatchers.IO) {
        var count = 0
        val userId = waitGetCurrentUid()
        do {
            val result = UserIndependentCacheHooker.getAllFriends()
            if (result.isSuccess) {
                val dataList = result.getOrThrow()
                friendIdMap = dataList.associate { it.userId to "${it.remarkName}(${it.account})" }
                FileDataProvider.saveFriends(userId, dataList)
                return@withContext
            }
            delay(2000)
            count++
        } while (count < 3)
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