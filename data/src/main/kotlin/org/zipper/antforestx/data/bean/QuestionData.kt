package org.zipper.antforestx.data.bean

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer


@Serializable(with = QuestionMap.Serializer::class)
class QuestionMap : HashMap<String, QuestionData>() {

    companion object {
        val dsSerializer: BaseDataStoreSerializer<QuestionMap> by lazy {
            object : BaseDataStoreSerializer<QuestionMap>(serializer()) {
                override val defaultValue: QuestionMap
                    get() = QuestionMap()
            }
        }
    }

    class Serializer() : KSerializer<QuestionMap> {

        private val delegateSerializer = MapSerializer(String.serializer(), QuestionData.serializer())


        @OptIn(ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor get() = SerialDescriptor("AlipayUserData", delegateSerializer.descriptor)

        override fun serialize(encoder: Encoder, value: QuestionMap) {
            encoder.encodeSerializableValue(delegateSerializer, value as Map<String, QuestionData>)
        }

        override fun deserialize(decoder: Decoder): QuestionMap {
            val value = decoder.decodeSerializableValue(delegateSerializer)
            return QuestionMap().apply { putAll(value) }
        }
    }
}

@Serializable
data class QuestionData(
    val questionId: Long,
    val title: String,
    val answer: String
)