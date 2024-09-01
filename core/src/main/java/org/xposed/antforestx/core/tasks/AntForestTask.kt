package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.manager.ConfigManager
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.TimeUnit

/**
 * 蚂蚁森林
 */
class AntForestTask : ITask {

    private val collectTimestamp: Queue<Long> = LinkedList<Long>()

    private val collectTimeLock = Mutex()

    override fun start() {

    }

    suspend fun checkEnergyRanking() {

    }

    /**
     * 收取自身能量
     */
    private suspend fun collectSelfEnergy() {

    }

    private suspend fun checkCollectLimited(): Boolean = withContext(Dispatchers.Unconfined) {
        if (ConfigManager.isLimitForestCollect) {
            collectTimeLock.withLock {
                val limitCount = ConfigManager.limitCountInMinute
                val dropTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1)
                val firstTime = collectTimestamp.peek() ?: 0
                if (firstTime < dropTime) {
                    collectTimestamp.poll()
                }
                return@withContext collectTimestamp.size >= ConfigManager.limitCountInMinute
            }
        }
        return@withContext false
    }

    private suspend fun withCollectTime(block: suspend () -> Unit) {
        if (ConfigManager.isLimitForestCollect) {
            collectTimeLock.withLock {
                block()
            }
        }
    }
}