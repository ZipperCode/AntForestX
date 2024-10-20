package org.xposed.antforestx.core.util.log

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.WorkerThread
import org.xposed.antforestx.core.BuildConfig
import org.xposed.antforestx.core.manager.ConfigManager
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

private val enableLogcat = BuildConfig.DEBUG

fun String.toMarker(enable: Boolean = enableLogcat): Marker = Marker(this, enable)

data class Marker(val value: String, val enable: Boolean = false)

private val loggerTreeMap = ConcurrentHashMap<String, Timber.Tree>()

@SuppressLint("LogNotTimber")
fun Timber.Forest.plantTree(tree: Timber.Tree) {
    Log.d("Timber", "AntForestX plant tree: $tree")
    if (loggerTreeMap.containsKey(tree.toString())) {
        return
    }
    loggerTreeMap[tree.toString()] = tree
    plant(tree)
}

@WorkerThread
fun Timber.Forest.init() {
    if (BuildConfig.DEBUG) {
        plantTree(DebugTimberTree())
    } else {
        enableFullTree()
        plantTree(ErrorTimberTree())
    }
}

fun Timber.Forest.enableFullTree() {
    Timber.Forest.plantTree(FullTimberTree())
}

fun Timber.Forest.enableForest() {
    Timber.Forest.plantTree(MarkerTimberTree(AntForestTag))
}

fun Timber.Forest.enableManor() {
    Timber.Forest.plantTree(MarkerTimberTree(AntManorTag))
}

fun Timber.Forest.enableMember() {
    Timber.Forest.plantTree(MarkerTimberTree(AntMemberTag))
}

/**
 * 蚂蚁森林日志
 */
class MarkerTimberTree(private val marker: Marker) : FilePrintTimberTree(FileLogcatProvider.forestLogcatFile) {
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