package org.zipper.antforestx.data.record

import kotlinx.serialization.Serializable

/**
 * 森林用户信息
 */
@Serializable
data class AntForestUserInfo(
    val userId: String = "",
    /**
     * 总能量
     */
    val totalEnergy: Int = 0,
    /**
     * 总活力值
     */
    val totalVitality: Int = 0,
    /**
     * 证书数量
     */
    val certCount: Int = 0,
    /**
     * 拥有道具
     */
    val props: List<AntForestProp> = emptyList()
)

/**
 * 道具
 */
@Serializable
data class AntForestProp(
    val propId: String,
    val name: String,
    val count: Int
)