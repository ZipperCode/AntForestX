package org.xposed.antforestx.core.util.log

import org.xposed.antforestx.core.util.DateUtils
import org.xposed.antforestx.core.util.FileDataProvider
import org.xposed.antforestx.core.util.FileDataProvider.validParentDir
import org.zipper.antforestx.data.provider.StoreFileProvider
import java.io.File


object FileLogcatProvider {

    val logcatDir: File by lazy {
        val date = DateUtils.getYearMonthDay()
        File(StoreFileProvider.logcatRootDir, date)
    }

    val forestLogcatFile: File by lazy {
        File(logcatDir, "森林日志.log").validParentDir()
    }

    val manorLogcatFile : File by lazy {
        File(logcatDir, "庄园日志.log").validParentDir()
    }

    val memberLogcatFile : File by lazy {
        File(logcatDir, "会员中心日志.log").validParentDir()
    }

    val errorLogcatFile: File by lazy {
        File(logcatDir, "错误日志.log").validParentDir()
    }

    val fullLogcatFile : File by lazy {
        File(logcatDir, "完整日志.log").validParentDir()
    }

}