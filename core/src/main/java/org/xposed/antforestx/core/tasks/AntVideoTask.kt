package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.xposed.antforestx.core.ant.AntInteractTaskRpcCall
import org.xposed.antforestx.core.util.isSuccess
import org.xposed.antforestx.core.util.onSuccessCatching
import timber.log.Timber

/**
 * 视频任务
 */
class AntVideoTask : ITask {

    private val logger get() = Timber.tag("视频任务")
    override suspend fun start(): Unit = withContext(Dispatchers.IO + CoroutineName("AntVideo")) {

    }

    private suspend fun query() {
        AntInteractTaskRpcCall.taskQuery().onSuccessCatching {
            if (!it.isSuccess()) {
                logger.e("查询视频任务失败: %s", it)
                return@onSuccessCatching
            }
            val newAudience = it.getBoolean("newAudience")
            val newUser = it.getBoolean("newUser")
            val taskList = it.getJSONArray("taskList")
            for (i in 0 until taskList.length()) {
                val task = taskList.getJSONObject(i)
                val completed = task.getBoolean("completed")
                val todayLimited = task.getBoolean("todayLimited")
                if (completed || todayLimited) {
                    continue
                }
                val taskType = task.getString("taskType")
                val rewardParams = task.getString("rewardParams")
                val taskActivityId = task.getString("taskActivityId")
                if (taskType == "signIn") {
                    // 签到
                    handleTodayTask(task.getJSONObject("taskData"), taskActivityId, rewardParams, taskType)
                } else if (taskType == "wfDayShare") {

                } else if (taskType == "radicalRed") {
                    // radicalRed

                }
            }
        }
    }

    private suspend fun handleTodayTask(taskData: JSONObject, taskActivityId: String, rewardParams: String, taskType: String) {
        val taskProgress = taskData.getJSONArray("taskProgress")
        for (i in 0 until taskProgress.length()) {
            val taskProgressItem = taskProgress.getJSONObject(i)
            val today = taskProgressItem.getBoolean("today")
            if (!today) {
                continue
            }
            val completed = taskProgressItem.getBoolean("completed")
            if (completed) {
                return
            }
            AntInteractTaskRpcCall.reward(taskActivityId, rewardParams, taskType)
        }
    }
}