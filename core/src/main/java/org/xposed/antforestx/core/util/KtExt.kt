package org.xposed.antforestx.core.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber


fun Map<String, Any>.toListJson(): String {
    return moshi.adapter(List::class.java).toJson(listOf(this))
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

fun String.safeToInt():Int {
    return try {
        toInt()
    } catch (e:Exception) {
        0
    }
}