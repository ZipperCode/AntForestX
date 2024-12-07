package org.xposed.antforestx.core.util.log

import android.util.Log
import androidx.annotation.WorkerThread
import org.xposed.antforestx.core.BuildConfig
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.forestx.core.logcat.DebugTimberTree
import org.xposed.forestx.core.logcat.FilePrintTimberTree
import org.xposed.forestx.core.logcat.plantTree
import timber.log.Timber
import java.io.File

data class Marker(val value: String, val enable: Boolean = false)

@WorkerThread
fun Timber.Forest.init() {
    if (BuildConfig.DEBUG) {
        plantTree(DebugTimberTree(true))
    } else {
        enableFullTree()
        plantTree(ErrorTimberTree())
    }
}

fun Timber.Forest.enableFullTree() {
    Timber.Forest.plantTree(FullTimberTree())
}

fun Timber.Forest.enableForest() {
    Timber.Forest.plantTree(MarkerTimberTree(AntForestTag, FileLogcatProvider.forestLogcatFile))
}

fun Timber.Forest.enableManor() {
    Timber.Forest.plantTree(MarkerTimberTree(AntManorTag, FileLogcatProvider.manorLogcatFile))
}

fun Timber.Forest.enableMember() {
    Timber.Forest.plantTree(MarkerTimberTree(AntMemberTag, FileLogcatProvider.memberLogcatFile))
}

/**
 * 蚂蚁森林日志
 */
class MarkerTimberTree(private val marker: Marker, file: File) : FilePrintTimberTree(file) {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return super.isLoggable(tag, priority) && marker.enable && marker.value == tag
    }

    override fun toString(): String {
        return "MarkerTimberTree(marker=$marker)"
    }

}

/**
 * 错误日志
 */
class ErrorTimberTree : FilePrintTimberTree(FileLogcatProvider.errorLogcatFile) {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority == Log.ERROR
    }
}

/**
 * 全部日志
 */
class FullTimberTree : FilePrintTimberTree(FileLogcatProvider.fullLogcatFile) {
    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return ConfigManager.basicConfig.enableLogcat
    }
}