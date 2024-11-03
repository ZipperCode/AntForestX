package org.xposed.antforestx.core.util

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 *
 * @author  zhangzhipeng
 * @date    2024/9/4
 */
object DateUtils {

    @JvmInline
    value class DayOfWeek(private val value: Int) {
        val isSunday: Boolean get() = value == 1
        val isMonday: Boolean get() = value == 2
        val isTuesday: Boolean get() = value == 3
        val isWednesday: Boolean get() = value == 4
        val isThursday: Boolean get() = value == 5
        val isFriday: Boolean get() = value == 6
        val isSaturday: Boolean get() = value == 7
    }

    fun getDayOfWeek(): DayOfWeek {
        return DayOfWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
    }

    /**
     * 如 0700-0730 表示 7点到7点半
     */
    fun checkInTime(str: String): Boolean {
        try {
            val splits = str.split("-")
            val start = splits[0].toInt() // 700
            val end = splits[1].toInt() // 730
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val currentValue = String.format(Locale.CHINA, "%02d%02d", hour, minute).toInt()
            return currentValue in start..end
        } catch (ignored: Exception) {
            return false
        }
    }

    // 获取年月日如20240921
    fun getYearMonthDay(): String {
        return SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date())
    }
}