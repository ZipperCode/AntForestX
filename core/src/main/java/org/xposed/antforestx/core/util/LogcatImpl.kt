package org.xposed.antforestx.core.util

import android.util.Log
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

private const val TAG = "AntForestX"

internal class LogcatImpl : ILog {

    override fun d(msg: String, vararg args: Any?) {
        Log.d(TAG, String.format(msg, *args))
    }

    override fun i(msg: String, vararg args: Any?) {
        Log.i(TAG, String.format(msg, *args))
    }

    override fun w(msg: String, vararg args: Any?) {
        Log.w(TAG, String.format(msg, *args))
    }

    override fun e(msg: String, vararg args: Any?) {
        Log.e(TAG, String.format(msg, *args))
    }

    override fun e(msg: String, e: Throwable) {
        Log.e(TAG, msg, e)
    }

    override fun printStackTrace(e: Throwable) {
        Log.e(TAG, Log.getStackTraceString(e))
    }
}