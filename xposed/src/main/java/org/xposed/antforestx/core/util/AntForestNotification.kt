package org.xposed.antforestx.core.util

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

object AntForestNotification {

    private const val NOTIFICATION_ID: Int = 111

    private const val CHANNEL_ID: String = "org.zipper.antforestx.repair.ANT_FOREST_X_NOTIFY_CHANNEL"

    private var _notifyManager: NotificationManager? = null
    private lateinit var notificationBuilder: Notification.Builder
    private var isStart: Boolean = false

    private var contentText: CharSequence = ""

    fun start(context: Context) {
        initNotification(context)
        if (!isStart) {
            _notifyManager = getNotificationManager(context)
            if (context is Service) {
                context.startForeground(NOTIFICATION_ID, notificationBuilder.build())
            } else {
                _notifyManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
            isStart = true
        }
    }

    fun stop(context: Context) {
        if (isStart) {
            if (context is Service) {
                context.stopForeground(Service.STOP_FOREGROUND_REMOVE)
            } else {
                getNotificationManager(context).cancel(NOTIFICATION_ID)
            }
            isStart = false
        }
    }

    fun setContentText(text: CharSequence) {
        if (contentText != text) {
            contentText = text
            notificationBuilder.setContentText(text)
            if (isStart) {
                _notifyManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    private fun initNotification(context: Context) {
        if (!this::notificationBuilder.isInitialized) {
            val it = Intent(Intent.ACTION_VIEW)
            it.setData(Uri.parse("alipays://platformapi/startapp?appId="))
            val pi = PendingIntent.getActivity(
                context, 0, it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    CHANNEL_ID, "芝麻粒能量提醒",
                    NotificationManager.IMPORTANCE_LOW
                )
                notificationChannel.enableLights(false)
                notificationChannel.enableVibration(false)
                notificationChannel.setShowBadge(false)
                getNotificationManager(context).createNotificationChannel(notificationChannel)
                Notification.Builder(context, CHANNEL_ID)
            } else {
                getNotificationManager(context)
                Notification.Builder(context)
                    .setPriority(Notification.PRIORITY_LOW)
            }

            notificationBuilder
                .setSmallIcon(R.drawable.sym_def_app_icon)
                .setContentTitle("芝麻粒X")
                .setAutoCancel(false)
                .setContentIntent(pi)
        }
    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}