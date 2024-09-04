package org.xposed.antforestx.core.util

import android.os.Handler
import android.os.HandlerThread
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


object CoroutineHelper {
    val globalCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.e("CoroutineExceptionHandler", throwable)
    }

    private val subLoopThread = HandlerThread("SubLoopThread")

    /**
     * 整个应用使用的协程
     */
    val mainCoroutine: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + SupervisorJob() + globalCoroutineExceptionHandler + CoroutineName("MainCoroutine"))
    }

    val loopCoroutineScope: CoroutineScope by lazy {
        subLoopThread.start()
        val handler = Handler(subLoopThread.looper)
        CoroutineScope(handler.asCoroutineDispatcher() + globalCoroutineExceptionHandler)
    }

    fun createCoroutine(context: CoroutineContext): CoroutineScope {
        return CoroutineScope(
            context + globalCoroutineExceptionHandler
        )
    }

    fun cancelAllTask() {
        mainCoroutine.cancel()
    }
}