package org.zipper.antforestx.data.config

import kotlinx.serialization.Serializable

/**
 * 配置
 */
@Serializable
data class AntConfig(
    /**
     * 基础配置
     */
    val basicConfig: AntBasicConfig = AntBasicConfig(),
    /**
     * 蚂蚁森林配置
     */
    val forestConfig: AntForestConfig = AntForestConfig(),
    /**
     * 蚂蚁庄园配置
     */
    val manorConfig: AntManorConfig = AntManorConfig(),
    /**
     * 其他配置
     */
    val otherConfig: AntOtherConfig = AntOtherConfig()
)