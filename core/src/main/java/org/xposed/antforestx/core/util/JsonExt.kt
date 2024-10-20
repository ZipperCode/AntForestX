package org.xposed.antforestx.core.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

internal val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()


class HashMapJsonAdapter<K, V>(moshi: Moshi, keyType: Type, valueType: Type) :
    JsonAdapter<HashMap<K?, V?>?>() {
    private val keyAdapter: JsonAdapter<K> = moshi.adapter(keyType)
    private val valueAdapter: JsonAdapter<V> = moshi.adapter(valueType)

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: HashMap<K?, V?>?) {
        writer.beginObject()
        for (entry: Map.Entry<K?, V?> in value!!.entries) {
            if (entry.key == null) {
                throw JsonDataException("Map key is null at " + writer.path)
            }
            writer.promoteValueToName()
            keyAdapter.toJson(writer, entry.key)
            valueAdapter.toJson(writer, entry.value)
        }
        writer.endObject()
    }

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): HashMap<K?, V?> {
        val result = HashMap<K?, V?>()
        reader.beginObject()
        while (reader.hasNext()) {
            reader.promoteNameToValue()
            val name = keyAdapter.fromJson(reader)
            val value = valueAdapter.fromJson(reader)
            val replaced = result.put(name, value)
            if (replaced != null) {
                throw JsonDataException(
                    "Map key '"
                            + name
                            + "' has multiple values at path "
                            + reader.path
                            + ": "
                            + replaced
                            + " and "
                            + value
                )
            }
        }
        reader.endObject()
        return result
    }

    override fun toString(): String {
        return "JsonAdapter($keyAdapter=$valueAdapter)"
    }

    companion object {
        val FACTORY: Factory = Factory { type, annotations, moshi ->
            val rawType = Types.getRawType(type)
            if (annotations.isNotEmpty()) return@Factory null
            if (rawType != java.util.Map::class.java) return@Factory null
            val keyAndValue = if (type === java.util.Properties::class.java) arrayOf<Type>(
                String::class.java,
                String::class.java
            ) else {
                arrayOf<Type>(Any::class.java, Any::class.java)
            }
            HashMapJsonAdapter<Any?, Any>(
                moshi,
                keyAndValue[0],
                keyAndValue[1]
            ).nullSafe()
        }
    }
}
