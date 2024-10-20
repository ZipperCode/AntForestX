package org.xposed.antforestx.core.util.log

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.xposed.antforestx.core.BuildConfig
import timber.log.Timber
import java.util.concurrent.Executors

private const val APP_TAG = "AntForestX"
private const val MAX_LOG_LENGTH = 3000

class DebugTimberTree : Timber.DebugTree() {

    private val logCoroutine = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return BuildConfig.DEBUG
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logCoroutine.launch {
            val newMessage = "${requireTag(tag)}: $message"
            if (newMessage.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(APP_TAG, newMessage)
                } else {
                    Log.println(priority, APP_TAG, newMessage)
                }
                return@launch
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            var i = 0
            val length = newMessage.length
            while (i < length) {
                var newline = newMessage.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = Math.min(newline, i + MAX_LOG_LENGTH)
                    val part = newMessage.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Log.wtf(APP_TAG, part)
                    } else {
                        Log.println(priority, APP_TAG, part)
                    }
                    i = end
                } while (i < newline)
                i++
            }

            // super.log(priority, APP_TAG, "${requireTag(tag)}: $message", t)
        }
    }

    private fun requireTag(tag: String?): String {
        return if (tag.isNullOrEmpty()) "" else "【$tag】"
    }

    override fun toString(): String {
        return "DebugTimberTree"
    }


}