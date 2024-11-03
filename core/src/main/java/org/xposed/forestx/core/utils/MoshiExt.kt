package org.xposed.forestx.core.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.xposed.forestx.core.utils.JsonContext.getMoshi
import java.lang.reflect.Type

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

object JsonContext {

    fun getMoshi(): Moshi = moshi

    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        return getMoshi().adapter<T>(clazz).fromJson(json)
    }

    fun <T> fromJson(json: String, clazz: Type): T? {
        return getMoshi().adapter<T>(clazz).fromJson(json)
    }


    fun toJson(data: Any): String {
        return getMoshi().adapter<Any>(data::class.java).toJson(data)
    }
}

inline fun <reified T> createType(): Type {
    return Types.getRawType(T::class.java)
}

inline fun <reified T> createList(): Type {
    return Types.newParameterizedType(List::class.java, createType<T>())
}

inline fun <reified T> createMapType(): Type {
    return Types.newParameterizedType(Map::class.java, String::class.java, T::class.java)
}

inline fun <reified T> Moshi.fromJson(json: String): T? {
    return getMoshi().adapter<T>(T::class.java).fromJson(json)
}

inline fun <reified T> Moshi.listAdapter(): JsonAdapter<List<T>> {
    val type = Types.newParameterizedType(List::class.java, T::class.java)
    return adapter(type)
}

inline fun <reified T> Moshi.mapAdapter(): JsonAdapter<Map<String, T>> {
    val type = Types.newParameterizedType(Map::class.java, String::class.java, T::class.java)
    return adapter(type)
}