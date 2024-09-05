package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.ant.AntBookReadRpcCall
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.Logger
import org.xposed.antforestx.core.util.log.ITagLog
import org.xposed.antforestx.core.util.onSuccessCatching
import java.util.concurrent.TimeUnit

/**
 * 阅读书籍
 */
class AntBookReadTask(override val subTag: String = "阅读任务📖") : ITagLog {

    suspend fun start() = withContext(Dispatchers.IO + CoroutineName("AntBookRead")) {
        log("阅读书籍任务开始执行")
        if (!ConfigManager.enableBookRead) {
            li("阅读书籍未开启")
            return@withContext
        }
        val lastConsumeTime = UserManager.antRecord.consumeGold
        if (System.currentTimeMillis() - lastConsumeTime < TimeUnit.HOURS.toMillis(6)) {
            li("距离上次阅读书籍不足6小时，跳过")
            return@withContext
        }
        UserManager.updateNewRecord(UserManager.antRecord.copy(consumeGold = System.currentTimeMillis()))
        runCatching {
            queryTask()
            queryTreasureBox()
        }
    }


    private suspend fun queryTask() {
        log("查询书籍任务")
        var taskReceive = false
        AntBookReadRpcCall.queryTaskCenterPage().onSuccessCatching { jsonObject ->
            if (!jsonObject.getBoolean("success")) {
                lw("查询书籍任务失败", jsonObject)
                return@onSuccessCatching
            }
            val userTaskGroupList = jsonObject.getJSONObject("data")
                .getJSONObject("userTaskListModuleVO")
                .getJSONArray("userTaskGroupList")

            for (i in 0 until userTaskGroupList.length()) {
                val userTaskList = userTaskGroupList.getJSONObject(i).getJSONArray("userTaskList")
                for (j in 0 until userTaskList.length()) {
                    val taskInfo = userTaskList.getJSONObject(j)
                    val taskStatus = taskInfo.getString("taskStatus")
                    val taskType = taskInfo.getString("taskType")
                    val title = taskInfo.getString("title")
                    if ("TO_RECEIVE" == taskStatus) {
                        if ("READ_MULTISTAGE" == taskType) {
                            val subTaskList = taskInfo.getJSONArray("multiSubTaskList")
                            for (k in 0 until subTaskList.length()) {
                                val subTask = subTaskList.getJSONObject(k)
                                if (subTask.getString("taskStatus") == "TO_RECEIVE") {
                                    collectTaskPrize(subTask.getString("taskId"), taskType, title)
                                }
                            }
                        } else {
                            val taskId = taskInfo.getString("taskId")
                            collectTaskPrize(taskId, taskType, title)
                        }
                    } else if ("NOT_DONE" == taskStatus) {
                        if ("AD_VIDEO_TASK" == taskType) {
                            val taskId = taskInfo.getString("taskId")
                            for (k in 0 until 5) {
                                taskFinish(taskId, taskType)
                                delay(2000)
                                collectTaskPrize(taskId, taskType, title)
                                delay(2000)
                            }
                        } else if ("FOLLOW_UP" == taskType || "JUMP" == taskType) {
                            val taskId = taskInfo.getString("taskId")
                            taskFinish(taskId, taskType)
                            taskReceive = true
                        }
                    }
                }
            }

            if (taskReceive) {
                delay(5000)
                queryTask()
            }
        }.onFailure {
            le("查询书籍任务失败", it)
        }
    }

    private suspend fun collectTaskPrize(taskId: String, taskType: String, title: String) {
        log("领取书籍任务奖励", taskId, taskType, title)
        AntBookReadRpcCall.collectTaskPrize(taskId, taskType).onSuccessCatching { jsonObject ->
            if (jsonObject.optBoolean("success")) {
                log(
                    "领取书籍任务奖励成功, %s-%s#获得 %s",
                    taskId, title, jsonObject.optJSONObject("data")?.getInt("coinNum")
                )
            }
        }.onFailure {
            le("领取书籍任务奖励失败", it)
        }
    }

    private suspend fun taskFinish(taskId: String, taskType: String) {
        AntBookReadRpcCall.taskFinish(taskId, taskType).onSuccessCatching { jsonObject ->
            if (jsonObject.optBoolean("success")) {
                log("完成任务成功 id:%s, type:%s", taskId, taskType)
            }
        }.onFailure {
            le("完成任务失败", it)
        }
    }

    private suspend fun queryTreasureBox() {
        log("查询宝箱任务")
        AntBookReadRpcCall.queryTreasureBox().onSuccessCatching { jsonObject ->
            if (!jsonObject.optBoolean("success")) {
                lw("查询宝箱任务失败", jsonObject)
                return@onSuccessCatching
            }
            val treasureBoxVo = jsonObject.getJSONObject("data").getJSONObject("treasureBoxVo")
            if (treasureBoxVo.has("countdown")) {
                return@onSuccessCatching
            }
            if (treasureBoxVo.getString("status") == "CAN_OPEN") {
                AntBookReadRpcCall.openTreasureBox().onSuccess {
                    if (!it.optBoolean("success")) {
                        Logger.w("宝箱任务开启失败", it)
                        return@onSuccess
                    }
                    val coinNum: Int = it.getJSONObject("data").getInt("coinNum")
                    log("[打开宝箱]#%s", coinNum);
                }
            }

        }.onFailure {
            le("查询宝箱任务失败", it)
        }
    }
}