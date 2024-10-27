package org.xposed.antforestx.core.util

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.xposed.antforestx.core.bean.AlipayFriendBean
import org.xposed.antforestx.core.bean.CooperateInfoBean
import org.xposed.antforestx.core.bean.record.AntEnergyStatistics
import org.xposed.antforestx.core.bean.record.AntRecord
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

    /**
     * 保存能量收取记录
     */
    suspend fun saveEnergyRecord(record: Map<String, AntEnergyStatistics>) = withContext(Dispatchers.IO) {
        val file = File(mainDirectory, "data/energy_record.json").validParentDir()
        val jsonData = Json.encodeToString(serializer(), record)
        file.writeText(jsonData)
    }

    /**
     * 保存记录
     */
    suspend fun saveAntRecord(userId: String, antRecord: AntRecord) = withContext(Dispatchers.IO) {
        val file = File(mainDirectory, "record/record_$userId.json").validParentDir()
        val jsonData = Json.encodeToString(serializer(), antRecord)
        file.writeText(jsonData)
    }

    suspend fun loadAntRecord(userId: String): AntRecord? = withContext(Dispatchers.IO) {
        logger.d("读取记录文件")
        val file = File(mainDirectory, "record/record_$userId.json")
        if (!file.exists()) {
            return@withContext null
        }
        val jsonData = file.readText()
        return@withContext Json.decodeFromString(serializer(), jsonData)
    }

    suspend fun saveCooperate(list: List<CooperateInfoBean>) = withContext(Dispatchers.IO) {
        val file = File(requireUserDataFileDir(), "cooperate.json")
        saveJsonToFile(file, list)
    }

    suspend fun loadCooperate(userId: String): List<CooperateInfoBean> = withContext(Dispatchers.IO) {
        val file = File(requireUserDataFileDir(), "cooperate.json")
        return@withContext loadJsonFromFile(file) ?: emptyList<CooperateInfoBean>()
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