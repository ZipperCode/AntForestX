package org.xposed.antforestx.core.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.json.JSONArray
import org.json.JSONObject


fun Map<*, *>.toListJson(): String {
    return Json.encodeToString(serializer(), listOf(this))
}

suspend inline fun <reified T> Result<T>.onSuccessCatching(crossinline block: suspend (T) -> Unit): Result<T> {
    try {
        onSuccess {
            block(it)
        }
    } catch (e: Exception) {
        return Result.failure(e)
    }
    return this
}

inline fun JSONArray.forEach(crossinline block: (JSONObject) -> Unit) {
    for (i in 0 until length()) {
        block(getJSONObject(i))
    }
}