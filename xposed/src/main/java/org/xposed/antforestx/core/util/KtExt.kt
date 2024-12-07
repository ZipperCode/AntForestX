package org.xposed.antforestx.core.util

import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber


fun Map<String, Any>.toListJson(): String {
    return moshi.adapter(List::class.java).toJson(listOf(this))
}

fun JSONArray.toStringList(): List<String> {
    val list = mutableListOf<String>()
    for (i in 0 until length()) {
        list.add(getString(i))
    }
    return list
}

suspend inline fun <reified T> Result<T>.onSuccessCatching(crossinline block: suspend (T) -> Unit): Result<T> {
    try {
        onSuccess {
            block(it)
        }
    } catch (e: Exception) {
        Timber.tag("Exception").e(e)
        return Result.failure(e)
    }
    return this
}

inline fun JSONArray.forEach(crossinline block: (JSONObject) -> Unit) {
    for (i in 0 until length()) {
        block(getJSONObject(i))
    }
}

fun JSONObject.isSuccess(): Boolean {
    return optString("resultCode") == "SUCCESS"
}

val JSONObject.success get() = optBoolean("success")

fun JSONArray?.isNullOrEmpty(): Boolean {
    return this == null || length() == 0
}

fun String.safeToInt(): Int {
    return try {
        toInt()
    } catch (e: Exception) {
        0
    }
}

fun JSONObject.obj(key: String): JSONObject {
    return this.optJSONObject(key) ?: JSONObject()
}

fun JSONObject.int(key: String): Int {
    return this.optInt(key)
}

fun JSONObject.str(key: String): String {
    return this.optString(key)
}

fun JSONObject.bool(key: String): Boolean {
    return this.optBoolean(key)
}

fun JSONObject.array(key: String): JSONArray {
    return this.optJSONArray(key) ?:JSONArray()
}