package org.xposed.antforestx.core.util.log

import org.xposed.antforestx.core.util.DateUtils
import org.xposed.antforestx.core.util.FileDataProvider
import org.xposed.antforestx.core.util.FileDataProvider.validParentDir
import java.io.File


object FileLogcatProvider {

    private val logcatDir: File by lazy {
        val date = DateUtils.getYearMonthDay()
        File(FileDataProvider.mainDirectory, "log/${date}")
    }

    val forestLogcatFile: File by lazy {
        File(logcatDir, "forest.log").validParentDir()
    }

    val errorLogcatFile: File by lazy {
        File(logcatDir, "error.log").validParentDir()
    }

    val fullLogcatFile : File by lazy {
        File(logcatDir, "full.log").validParentDir()
    }

}