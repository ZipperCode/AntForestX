package org.xposed.antforestx.core.util

interface ILog {

    fun d(msg: String, vararg args: Any?)

    fun i(msg: String, vararg args: Any?)

    fun w(msg: String, vararg args: Any?)

    fun e(msg: String, vararg args: Any?)

    fun e(msg: String, e: Throwable)

    fun printStackTrace(e: Throwable)
}