package org.xposed.forestx.core.utils

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


object AppCoroutine {
    val globalCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("CoroutineExceptionHandler %s", Log.getStackTraceString(throwable))
    }

    private val subLoopThread = HandlerThread("SubLoopThread")

    /**
     * 整个应用使用的协程
     */
    private val appCoroutine: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Default + SupervisorJob() + globalCoroutineExceptionHandler + CoroutineName("MainCoroutine"))
    }

    private val loopCoroutineScope: CoroutineScope by lazy {
        subLoopThread.start()
        val handler = Handler(subLoopThread.looper)
        CoroutineScope(handler.asCoroutineDispatcher() + globalCoroutineExceptionHandler)
    }

    fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit): Job {
        return loopCoroutineScope.launch(context, block = block)
    }

    fun launchSingle(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {
        loopCoroutineScope.launch(context, block = block)
    }

    fun createCoroutine(context: CoroutineContext): CoroutineScope {
        return CoroutineScope(
            context + globalCoroutineExceptionHandler
        )
    }

    fun cancelAllTask() {
        appCoroutine.cancel()
    }
}