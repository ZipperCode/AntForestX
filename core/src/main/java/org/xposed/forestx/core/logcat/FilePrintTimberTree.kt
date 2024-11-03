package org.xposed.forestx.core.logcat

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class FilePrintTimberTree(
    private val logcatFile: File
) : Timber.Tree() {

    private val logcatCoroutine: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val fmt: SimpleDateFormat by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    }

    private val printStream: PrintStream? by lazy {
        kotlin.runCatching {
            if (!logcatFile.exists() && logcatFile.createNewFile()) {
                if (logcatFile.canWrite()) {
                    return@lazy PrintStream(FileOutputStream(logcatFile, true), true, "UTF-8")
                }
            }
        }
        return@lazy null
    }


    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return (priority == Log.INFO || priority == Log.WARN)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (printStream == null) {
            return
        }
        logcatCoroutine.launch {
            printStream?.println("${getFormatTime()}: $message")
        }
    }

    private fun getFormatTime(): String {
        val currentDate = Date()
        try {
            return fmt.format(currentDate)
        } catch (ignore: Exception) {
        }
        return "${currentDate.time}"
    }

    override fun equals(other: Any?): Boolean {
        return other?.javaClass == this.javaClass
    }

    override fun hashCode(): Int {
        var result = logcatFile.hashCode()
        result = 31 * result + logcatCoroutine.hashCode()
        result = 31 * result + fmt.hashCode()
        result = 31 * result + (printStream?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "FilePrintTimberTree(logcatFile=$logcatFile)"
    }
}