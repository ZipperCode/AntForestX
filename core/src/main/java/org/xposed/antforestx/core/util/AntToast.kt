package org.xposed.antforestx.core.util

import android.widget.Toast
import kotlinx.coroutines.launch
import org.xposed.antforestx.core.XposedHookEntry
import org.xposed.antforestx.core.manager.ConfigManager

object AntToast {


    private var toast: Toast? = null

    fun showShort(msg: String) {
        showToast(msg, Toast.LENGTH_SHORT)
    }

    fun showLong(msg: String) {
        showToast(msg, Toast.LENGTH_LONG)
    }

    private fun showToast(msg: String, duration: Int) {
        if (!ConfigManager.enableToast) {
            return
        }
        CoroutineHelper.loopCoroutineScope.launch {
            toast?.cancel()
            toast = Toast.makeText(XposedHookEntry.application, msg, duration)
            toast?.show()
        }
    }

    private fun cancelToast() {
        toast?.cancel()
    }
}