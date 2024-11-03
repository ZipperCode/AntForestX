package org.xposed.forestx.core.utils.toast

import android.content.Context
import android.widget.Toast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.xposed.forestx.core.utils.AppCoroutine

object AppToast : IToast, KoinComponent {

    private val androidContext: Context by inject<Context>()

    private var toast: Toast? = null

    fun forceShow(msg: String) {
        AppCoroutine.launch {
            showInternal(msg, Toast.LENGTH_SHORT)
        }
    }

    fun showShort(msg: String) {
        showToast(msg, Toast.LENGTH_SHORT)
    }

    fun showLong(msg: String) {
        showToast(msg, Toast.LENGTH_LONG)
    }

    override fun showToast(msg: String, duration: Int) {
        AppCoroutine.launchSingle {
            showInternal(msg, duration)
        }
    }

    private fun showInternal(msg: String, duration: Int) {
        toast?.cancel()
        toast = Toast.makeText(androidContext, msg, duration)
        toast?.show()
    }
}