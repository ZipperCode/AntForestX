package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.ant.AntEcologyRpcCall
import org.xposed.antforestx.core.util.onSuccessCatching
import org.xposed.antforestx.core.util.success
import timber.log.Timber

/**
 * 生态保护
 * TODO 不玩这个暂时不实现了
 */
class AntEcologyTask : ITask {

    private val logger get() = Timber.tag("生态保护")
    override suspend fun start(): Unit = withContext(Dispatchers.IO) {

    }
    private suspend fun queryTreeItemsForExchange() {
        AntEcologyRpcCall.queryTreeItemsForExchange().onSuccessCatching {
            if (!it.success) {
                logger.e("生态保护数列表查询失败 %s", it)
                return@onSuccessCatching
            }
            val tabItems = it.getJSONArray("tabItems")
        }
    }
}