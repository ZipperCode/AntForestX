package org.xposed.antforestx.core.util

import android.widget.Toast
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.forestx.core.utils.toast.AppToast
import org.xposed.forestx.core.utils.toast.IToast

object AntToast : IToast by AppToast {

    fun forceShow(msg: String) {
        AppToast.showToast(msg, Toast.LENGTH_SHORT)
    }

    fun showShort(msg: String) {
        showToast(msg, Toast.LENGTH_SHORT)
    }

    fun showLong(msg: String) {
        showToast(msg, Toast.LENGTH_LONG)
    }

    override fun showToast(msg: String, duration: Int) {
        if (ConfigManager.enableToast) {
            AppToast.showToast(msg, duration)
        }
    }
}