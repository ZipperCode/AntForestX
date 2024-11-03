package org.xposed.antforestx.core.tasks

interface ITask {
    /**
     * 启动任务
     */
    suspend fun start()
}