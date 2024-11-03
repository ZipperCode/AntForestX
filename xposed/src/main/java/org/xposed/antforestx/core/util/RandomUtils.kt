package org.xposed.antforestx.core.util

import java.util.Random


object RandomUtils {
    private val random = Random()

    fun nextDouble() = random.nextDouble()

    fun nextInt(max: Int) = random.nextInt(max)
    fun nextInt(min: Int, max: Int): Int {
        if (min >= max) return min
        return random.nextInt(max - min) + min
    }

    fun nextLong() = random.nextLong()

    fun getRandom(len: Int): String {
        val rs = StringBuilder()
        for (i in 0 until len) {
            rs.append(random.nextInt(10))
        }
        return rs.toString()
    }
}