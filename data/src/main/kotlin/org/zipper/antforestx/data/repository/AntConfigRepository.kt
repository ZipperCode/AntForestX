package org.zipper.antforestx.data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.zipper.antforestx.data.DataStoreType
import org.zipper.antforestx.data.config.AntConfig

internal class AntConfigRepository(

) : IAntConfigRepository , KoinComponent{

    private val antConfigDataStore: DataStore<AntConfig> by inject<DataStore<AntConfig>>(DataStoreType.Config)

    override val configFlow: Flow<AntConfig> get() = antConfigDataStore.data

    override val defaultConfig: AntConfig get() = AntConfig()
    override suspend fun updateConfig(block: (AntConfig) -> AntConfig) {
        antConfigDataStore.updateData(block)
    }
}