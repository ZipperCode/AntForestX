package org.xposed.antforestx.core.work

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.xposed.antforestx.core.constant.ClassMember
import timber.log.Timber
import java.util.Calendar

object AntWorkScheduler {

    fun setAlarm7(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val it = Intent()
            it.setClassName(ClassMember.PACKAGE_NAME, ClassMember.LAUNCH_SERVICE)
            val piFlag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val launchIntent = PendingIntent.getService(context, 888, it, piFlag)
            val calendar = Calendar.getInstance().apply {
                if (get(Calendar.HOUR_OF_DAY) >= 7) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
                set(Calendar.HOUR_OF_DAY, 7)
                set(Calendar.MINUTE, 10)
                set(Calendar.SECOND, 0)
            }
            alarmManager.setAlarmClock(AlarmClockInfo(calendar.timeInMillis, null), launchIntent)
        } catch (e:Exception) {
            Timber.e(e)
        }
    }
}