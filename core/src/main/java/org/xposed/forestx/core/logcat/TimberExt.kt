package org.xposed.forestx.core.logcat

import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

const val APP_TAG = "AntForestX"

private val loggerTreeMap = ConcurrentHashMap<String, Timber.Tree>()

fun Timber.Forest.plantTree(tree: Timber.Tree) {
    if (loggerTreeMap.containsKey(tree.toString())) {
        return
    }
    loggerTreeMap[tree.toString()] = tree
    plant(tree)
}