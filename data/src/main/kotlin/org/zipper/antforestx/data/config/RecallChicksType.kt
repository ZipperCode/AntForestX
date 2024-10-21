package org.zipper.antforestx.data.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RecallChicksType.Serializer::class)
enum class RecallChicksType(
    val desc: String
) {
    Always("始终召回"),
    InEat("在吃东西时召回"),
    InHunger("在饥饿时召回"),
    Never("从不召回")

    ;

    object Serializer : kotlinx.serialization.KSerializer<RecallChicksType> {
        override val descriptor = PrimitiveSerialDescriptor(
            "RecallChicksType",
            kotlinx.serialization.descriptors.PrimitiveKind.INT
        )

        override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): RecallChicksType =
            entries[decoder.decodeInt()]

        override fun serialize(encoder: Encoder, value: RecallChicksType) {
            encoder.encodeInt(value.ordinal)
        }
    }
}