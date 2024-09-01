package org.xposed.antforestx.core.util

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.xposed.antforestx.core.bean.AlipayFriendBean
import org.xposed.antforestx.core.bean.record.AntEnergyRecord
import java.io.File

object DataFileHelper {

    private const val MAIN_DIR = "AntForestX"

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
     * 获取好友信息，保存到 data/friends.json
     */
    suspend fun saveFriends(friends: List<AlipayFriendBean>) = withContext(Dispatchers.IO) {
        val file = File(mainDirectory, "data/friends.json")
        if (file.parentFile?.exists() == false) {
            file.parentFile?.mkdirs()
        }

        val jsonData = Json.encodeToString(serializer(), friends)
        file.writeText(jsonData)
    }

    /**
     * 保存能量收取记录
     */
    suspend fun saveEnergyRecord(record: Map<String, AntEnergyRecord>) = withContext(Dispatchers.IO) {
        val file = File(mainDirectory, "data/energy_record.json")
        if (file.parentFile?.exists() == false) {
            file.parentFile?.mkdirs()
        }
        val jsonData = Json.encodeToString(serializer(), record)
        file.writeText(jsonData)
    }

    /**
     * 读取收取记录
     */
    suspend fun loadEnergyRecord(): Map<String, AntEnergyRecord> = withContext(Dispatchers.IO) {
        val file = File(mainDirectory, "data/energy_record.json")
        if (!file.exists()) {
            return@withContext emptyMap()
        }
        val jsonData = file.readText()
        return@withContext Json.decodeFromString(serializer(), jsonData)
    }
}