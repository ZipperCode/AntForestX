package org.xposed.antforestx.core.config

import kotlinx.serialization.Serializable

/**
 * 配置
 */
@Serializable
data class AntConfig(
    val basicConfig: AntBasicConfig = AntBasicConfig(),
    val forestConfig: AntForestConfig = AntForestConfig(),
    val manorConfig: AntManorConfig = AntManorConfig(),
    val otherConfig: AntOtherConfig = AntOtherConfig()
)