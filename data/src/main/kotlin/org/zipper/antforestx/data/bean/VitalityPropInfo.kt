package org.zipper.antforestx.data.bean

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Encoder
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer


@Serializable
class VitalityExchangedPropData : ArrayList<VitalityPropInfo>() {
    companion object {
        val dsSerializer: BaseDataStoreSerializer<VitalityExchangedPropData> by lazy {
            object : BaseDataStoreSerializer<VitalityExchangedPropData>(serializer()) {
                override val defaultValue: VitalityExchangedPropData
                    get() = VitalityExchangedPropData()
            }
        }
    }
}

/**
 * 活力值兑换道具信息
 * @param labelType 道具分类
 * @param skuId 道具id
 * @param spuId 道具spuId
 * @param rightsConfigId 道具配置id
 * @param skuName 道具名称
 * @param priceCent 道具价格(分/活力值)
 */
@Serializable
data class VitalityPropInfo(
    val labelType: LabelType,
    val skuId: String,
    val spuId: String,
    val rightsConfigId: String,
    val skuName: String,
    val price: Double,
    val priceCent: Long
)

/**
 * 活力值兑换道具分类
 */
@Serializable(with = LabelType.Serializer::class)
enum class LabelType(val label: String) {
    // 推荐
    Recommend(""),

    // 道具
    Prop("SC_ASSETS"),

    // 皮肤
    Skin("SKIN"),

    // 挂件
    Jewelry("JEWELRY"),

    // 其他
    Other("OTHER");

    object Serializer : KSerializer<LabelType> {
        override val descriptor = PrimitiveSerialDescriptor(
            "LabelType",
            kotlinx.serialization.descriptors.PrimitiveKind.STRING
        )

        override fun deserialize(decoder: kotlinx.serialization.encoding.Decoder): LabelType {
            val string = decoder.decodeString()
            return when (string) {
                "SC_ASSETS" -> Prop
                "SKIN" -> Skin
                "JEWELRY" -> Jewelry
                "OTHER" -> Other
                else -> Recommend
            }
        }


        override fun serialize(encoder: Encoder, value: LabelType) {
            encoder.encodeString(value.label)
        }
    }
}

