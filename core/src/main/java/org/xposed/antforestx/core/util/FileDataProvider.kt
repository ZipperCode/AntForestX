package org.xposed.antforestx.core.util

import android.os.Environment
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.xposed.antforestx.core.bean.AlipayFriendBean
import org.xposed.antforestx.core.bean.CooperateInfoBean
import org.xposed.antforestx.core.bean.QuestionData
import org.xposed.antforestx.core.bean.QuestionMap
import org.xposed.antforestx.core.bean.record.AntEnergyStatistics
import org.xposed.antforestx.core.bean.record.AntRecord
import org.xposed.antforestx.core.config.AntConfig
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
     * 保存配置
     */
    suspend fun saveAntConfig(antConfig: AntConfig) = withContext(Dispatchers.IO) {
        logger.d("保存配置文件")
        saveJsonToFile("config/config.json", antConfig)
    }

    /**
     * 读取配置
     */
    suspend fun loadAntConfig(): AntConfig? = withContext(Dispatchers.IO) {
        logger.d("读取配置文件")
        return@withContext loadJsonFromFile("config/config.json")
    }

    /**
     * 获取好友信息，保存到 data/friends.json
     */
    suspend fun saveFriends(userId: String, friends: List<AlipayFriendBean>) = withContext(Dispatchers.IO) {
        logger.d("保存好友信息")
        saveJsonToFile("data/friends_$userId.json", friends)
    }

    suspend fun loadFriends(userId: String): List<AlipayFriendBean>? = withContext(Dispatchers.IO) {
        logger.d("读取好友信息")
        return@withContext loadJsonFromFile("data/friends_$userId.json")
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

    suspend fun saveQuestionCache(questionMap: QuestionMap) {
        val file = File(requireUserRecordFileDir(), "question_cache.json")
        saveJsonToFile(file, questionMap)
    }

    suspend fun loadQuestionCache(): QuestionMap? {
        val file = File(requireUserRecordFileDir(), "question_cache.json")
        return loadJsonFromFile(file)
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