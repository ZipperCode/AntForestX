package org.xposed.forestx.core.utils.param

sealed class PreferenceKey<T>(val name: String, val defaultValue: T) {

    class StringKey(name: String, defaultValue: String) : PreferenceKey<String>(name, defaultValue)

    class TypeOfKey<T>(name: String, defaultValue: T) : PreferenceKey<T>(name, defaultValue)
}
