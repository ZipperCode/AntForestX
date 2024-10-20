package org.xposed.antforestx.core.bean

import kotlinx.serialization.Serializable

/**
 * 合种信息
 */
@Serializable
data class CooperateInfoBean(
    val cooperateId: String,
    val name: String
)