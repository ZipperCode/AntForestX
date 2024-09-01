package org.xposed.antforestx.core.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.bean.record.AntEnergyRecord
import org.xposed.antforestx.core.hooker.UserIndependentCacheHooker
import org.xposed.antforestx.core.util.DataFileHelper
import java.util.concurrent.ConcurrentHashMap

object UserManager {

    private var currentUid: String? = null

    private var friendIdMap: Map<String, String> = emptyMap()

    /**
     * 收取能量记录
     * key: userId
     * value: record
     */
    private val collectEnergyMap: MutableMap<String, AntEnergyRecord> = ConcurrentHashMap()

    /**
     * 获取所有好友信息，并保存
     */
    suspend fun hookLoadAllFriends() = withContext(Dispatchers.IO) {
        var condition = true
        while (condition) {
            val result = UserIndependentCacheHooker.getAllFriends()
            condition = result.isSuccess
            if (result.isFailure) {
                delay(2000)
            }
            if (result.isSuccess) {
                val dataList = result.getOrThrow()
                friendIdMap = dataList.associate { it.userId to "${it.remarkName}(${it.account})" }
                DataFileHelper.saveFriends(dataList)
            }
        }
    }

    suspend fun loadCollectEnergyRecord(): Map<String, AntEnergyRecord> {

    }

}