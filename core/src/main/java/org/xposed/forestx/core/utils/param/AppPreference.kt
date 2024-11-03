package org.xposed.forestx.core.utils.param

import java.util.concurrent.ConcurrentHashMap

object AppPreference {

    private val global = ConcurrentHashMap<PreferenceKey<*>, Any?>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: PreferenceKey<T>): T {
        return global[key] as? T ?: key.defaultValue
    }

    operator fun <T> set(key: PreferenceKey<T>, value: T) {
        setUnsafe(key, value)
    }

    private fun setUnsafe(key: PreferenceKey<*>, value: Any?) {
        global[key] = value
    }
}