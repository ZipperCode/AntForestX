package org.zipper.antforestx.data.bean

import kotlinx.serialization.Serializable

typealias CooperateData = List<CooperateInfoBean>

/**
 * 合种信息
 */
@Serializable
data class CooperateInfoBean(
    val cooperateId: String = "",
    val name: String = ""
)