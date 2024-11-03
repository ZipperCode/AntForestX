package org.zipper.antforestx.data.serializer

import androidx.datastore.core.Serializer
import com.squareup.moshi.JsonAdapter
import org.xposed.forestx.core.utils.JsonContext
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream
import java.lang.reflect.Type

abstract class BaseMoshiDataStoreSerializer<T>(
    private val adapter: JsonAdapter<T>
) : Serializer<T> {
    override suspend fun readFrom(input: InputStream): T {
        try {
            val jsonString = input.bufferedReader().use {
                it.readText()
            }
            if (jsonString.isNotEmpty()) {
                val value = adapter.fromJson(jsonString)
                if (value != null) {
                    return value
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return defaultValue
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        try {
            val dataString = adapter.toJson(t)
            Timber.d("data = %s dataString = %s", t, dataString)
            output.bufferedWriter().use {
                Timber.d("写入文件数据 = %s", dataString)
                it.write(dataString)
            }
        } catch (e: Exception) {
            Timber.e(e)
            throw e
        }
    }
}