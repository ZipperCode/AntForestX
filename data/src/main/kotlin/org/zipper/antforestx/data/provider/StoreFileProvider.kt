package org.zipper.antforestx.data.provider

import android.content.Context
import android.os.Environment
import android.provider.Settings.System.canWrite
import android.util.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.zipper.antforestx.data.utils.DateUtils
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object StoreFileProvider : KoinComponent {

    private val context: Context by inject<Context>()

    private const val MAIN_DIR = "AntForestX"
    private val mainDirectory: File by lazy {
        // 是否支持传统存储 Environment.isExternalStorageLegacy()
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val file = File(storageDir, MAIN_DIR)
        if (!file.exists()) {
            val result = file.mkdirs()
            Timber.tag("StoreFileProvider").e("Download存储文件夹创建：$result")
        }
        return@lazy file
    }

    private val mainMediaDirectory: File by lazy {
        val mediaFile = File(Environment.getExternalStorageDirectory(), "Android/media/com.eg.android.AlipayGphone")
        if (!mediaFile.exists()) {
            mediaFile.mkdirs()
        }
        val file = File(mediaFile, MAIN_DIR)
        if (!file.exists()) {
            val result = file.mkdirs()
            Timber.tag("StoreFileProvider").e("Android/media存储文件夹创建：$result")
        }
        return@lazy file
    }

    fun requireAntConfigFile(): File {
        return requireAvailableFile("config/config.json")
    }

    fun requireQuestionDataFile(): File {
        return requireAvailableFile("data/question_cache.json")
    }

    fun requireVitalityPropDataFile(): File {
        return requireAvailableFile("data/vitality_prop.json")
    }

    fun requireForestPropDataFile(): File {
        return requireAvailableFile("data/forest_prop.json")
    }

    fun requireAlipayUserDataFile(): File {
        return requireAvailableFile("data/alipay_user.json")
    }

    fun requireForestStatisticsDayFile(dateStr: String = DateUtils.getYearMonthDay()): File {
        return requireAvailableFile("record/forest_${dateStr}.json")
    }

    private fun requireAvailableFile(path: String): File {
        // 优先使用media文件夹存储，否则使用Downloads文件夹，最后兜底应用内存储
        var file = File(mainMediaDirectory, path).validParentDir()
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            return file
        } catch (e: Exception) {
            Timber.e(e)
            file = File(mainDirectory, path).validParentDir()
            try {
                if (!file.exists()) {
                    file.createNewFile()
                }
                return file
            } catch (ee: Exception) {
                Timber.e(ee)
            }
        }
        Timber.tag("StoreFileProvider").w("SD存储不可用，使用内置存储")
        return File(context.filesDir, "${MAIN_DIR}/${path}").validParentDir()
    }

    private fun File.validParentDir(): File {
        val parent = this.parentFile ?: return this
        if (!parent.exists()) {
            parent.mkdirs()
        }
        return this
    }
}