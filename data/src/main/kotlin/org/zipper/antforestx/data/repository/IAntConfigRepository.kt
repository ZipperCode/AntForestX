package org.zipper.antforestx.data.repository

import kotlinx.coroutines.flow.Flow
import org.zipper.antforestx.data.config.AntConfig

interface IAntConfigRepository {

    val configFlow: Flow<AntConfig>

    val defaultConfig: AntConfig

    suspend fun updateConfig(block: (AntConfig) -> AntConfig)
}