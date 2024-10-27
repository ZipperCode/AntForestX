package org.zipper.antforestx.data.serializer

import androidx.datastore.core.Serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.zipper.antforestx.data.config.AntConfig
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

class AntConfigSerializer : Serializer<AntConfig> {
    override val defaultValue: AntConfig
        get() = AntConfig()

    override suspend fun readFrom(input: InputStream): AntConfig {
        try {
            val jsonString = input.bufferedReader().use {
                it.readText()
            }
            if (jsonString.isNotEmpty()) {
                return Json.decodeFromString(serializer(), jsonString)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return AntConfig()
    }

    override suspend fun writeTo(t: AntConfig, output: OutputStream) {
        try {
            val dataString = Json.encodeToString(t)
            output.bufferedWriter().use {
                it.write(dataString)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}