package org.zipper.antforestx.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    // 获取年月日如20240921
    fun getYearMonthDay(): String {
        return SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date())
    }
}