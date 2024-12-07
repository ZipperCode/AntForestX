package org.xposed.antforestx.core.util

import android.os.Environment
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.xposed.antforestx.core.manager.UserManager
import timber.log.Timber
import java.io.File

object FileDataProvider {

    private const val MAIN_DIR = "AntForestX"

    private val logger get() = Timber.tag("数据存储")

    val mainDirectory: File by lazy {
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val file = File(storageDir, MAIN_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
        return@lazy file
    }


    private suspend fun requireUserDataFileDir(): File {
        return File(mainDirectory, "data/${UserManager.waitGetCurrentUid()}").validParentDir()
    }

    private suspend fun requireUserRecordFileDir(): File {
        return File(mainDirectory, "record/${UserManager.waitGetCurrentUid()}").validParentDir()
    }

    private inline fun <reified T> saveJsonToFile(fileName: String, data: T) {
        val file = File(mainDirectory, fileName).validParentDir()
        val jsonData = Json.encodeToString(serializer(), data)
        file.writeText(jsonData)
    }

    private inline fun <reified T> saveJsonToFile(file: File, data: T) {
        val jsonData = Json.encodeToString(serializer(), data)
        file.writeText(jsonData)
    }

    private inline fun <reified T> loadJsonFromFile(fileName: String): T? {
        val file = File(mainDirectory, fileName)
        if (!file.exists()) {
            return null
        }
        val jsonData = file.readText()
        return Json.decodeFromString(serializer(), jsonData)
    }

    private inline fun <reified T> loadJsonFromFile(file: File): T? {
        if (!file.exists()) {
            return null
        }
        val jsonData = file.readText()
        return Json.decodeFromString(serializer(), jsonData)
    }

    fun File.validParentDir(): File {
        runCatching {
            if (parentFile?.exists() == false) {
                parentFile?.mkdirs()
            }
        }
        return this
    }
}