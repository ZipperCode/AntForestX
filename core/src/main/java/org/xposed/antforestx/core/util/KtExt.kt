package org.xposed.antforestx.core.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


fun Map<*, *>.toListJson(): String {
    return Json.encodeToString(serializer(), listOf(this))
}