package org.xposed.antforestx.core.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.ant.RpcUtil
import org.xposed.antforestx.core.bean.record.AntEnergyStatistics
import org.xposed.antforestx.core.bean.record.AntForestDayRecord
import org.xposed.antforestx.core.bean.record.AntManorRecord
import org.xposed.antforestx.core.bean.record.AntRecord
import org.xposed.antforestx.core.hooker.UserIndependentCacheHooker
import org.xposed.antforestx.core.util.FileDataProvider

object UserManager {

    private var friendIdMap: Map<String, String> = emptyMap()

    private val antRecordFlow = MutableStateFlow(AntRecord())

    val energyStatistics: AntEnergyStatistics get() = antRecordFlow.value.energyStatistics
    val forestDayRecord: AntForestDayRecord get() = antRecordFlow.value.forestDayRecord
    val manorRecord: AntManorRecord get() = antRecordFlow.value.manorRecord

    suspend fun init(): Result<Unit> {
        return runCatching {
            val userId = waitGetCurrentUid()
            val result = FileDataProvider.loadAntRecord(userId)
            if (result != null) {
                antRecordFlow.value = result
            }
            antRecordFlow
                .distinctUntilChanged { old, new -> old == new }
                .collectLatest {
                    runCatching {
                        // 配置改变，同步保存本地
                        FileDataProvider.saveAntRecord(waitGetCurrentUid(), it)
                    }
                }
        }
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

}