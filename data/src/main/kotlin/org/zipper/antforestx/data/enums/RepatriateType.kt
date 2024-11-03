package org.zipper.antforestx.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class RepatriateType(
    val title:String
) {
    Attack("攻击"),
    Normal("普通")
}