package org.xposed.antforestx.core.util

import java.util.Calendar

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
}