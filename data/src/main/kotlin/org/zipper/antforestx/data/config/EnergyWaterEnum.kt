package org.zipper.antforestx.data.config

import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

@Serializable(with = EnergyWaterEnum.Serializer::class)
enum class EnergyWaterEnum(
    val id: Int,
    val desc: String
) {
    None(0, "不浇水"),
    Level1(39, "10g"),
    Level2(40, "18g"),
    Level3(41, "33g"),
    Level4(42, "66g"),
    ;

    object Serializer : kotlinx.serialization.KSerializer<EnergyWaterEnum> {
        override val descriptor = PrimitiveSerialDescriptor("EnergyWaterEnum", kotlinx.serialization.descriptors.PrimitiveKind.INT)
        override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): EnergyWaterEnum {
            return entries.first { it.id == decoder.decodeInt() }
        }

        override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: EnergyWaterEnum) {
            encoder.encodeInt(value.id)
        }
    }
}