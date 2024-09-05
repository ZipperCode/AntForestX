package org.xposed.antforestx.core.util.log

import org.xposed.antforestx.core.util.Logger

interface ITagLog {

    val subTag: String

    fun log(msg: String, vararg args: Any?) {
        Logger.d("$subTag: $msg", *args)
    }

    fun li(msg: String, vararg args: Any?) {
        Logger.i("$subTag: $msg", *args)
    }

    fun lw(msg: String, vararg args: Any?) {
        Logger.w("$subTag: $msg", *args)
    }

    fun le(msg: String, vararg args: Any?) {
        Logger.e("$subTag: $msg", *args)
    }

    fun le(msg: String, e: Throwable) {
        Logger.e("$subTag: $msg", e)
    }

    fun printStackTrace(e: Throwable) {
        Logger.printStackTrace(e)
    }
}