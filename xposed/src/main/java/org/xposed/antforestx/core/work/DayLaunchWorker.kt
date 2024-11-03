package org.xposed.antforestx.core.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DayLaunchWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return Result.success()
    }
}