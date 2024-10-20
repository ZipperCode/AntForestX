package org.xposed.antforestx.core.ant

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.xposed.antforestx.core.XposedHookEntry

object Starter {

    /**
     * 重启到登录页面
     */
    fun restartLogin() {

    }

    suspend fun restartByAlarm(delayTime: Long) {
        runCatching {
            val alarmManager = XposedHookEntry.application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        }
    }
}