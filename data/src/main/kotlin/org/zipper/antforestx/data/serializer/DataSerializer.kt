package org.zipper.antforestx.data.serializer

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

class DataSerializer<T>(private val jsonSerializer: KSerializer<T>, override val defaultValue: T) : Serializer<T> {

    override suspend fun readFrom(input: InputStream): T {
        try {
            val jsonString = input.bufferedReader().use {
                it.readText()
            }
            if (jsonString.isNotEmpty()) {
                return Json.decodeFromString(jsonSerializer, jsonString)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return defaultValue
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        try {
            val dataString = Json.encodeToString(jsonSerializer, t)
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