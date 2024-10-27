package org.zipper.antforestx.data.serializer

import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

abstract class BaseDataStoreSerializer<T>(
    private val jsonSerializer: KSerializer<T>
) : Serializer<T> {
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
            output.bufferedWriter().use {
                it.write(dataString)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}