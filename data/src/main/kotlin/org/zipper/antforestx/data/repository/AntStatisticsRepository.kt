package org.zipper.antforestx.data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.zipper.antforestx.data.DataStoreType
import org.zipper.antforestx.data.record.AntForestStatisticsDay
import org.zipper.antforestx.data.utils.DateUtils
import java.util.Calendar

internal class AntStatisticsRepository : IAntStatisticsRepository, KoinComponent {

    private val forestDataStore: DataStore<AntForestStatisticsDay> by inject<DataStore<AntForestStatisticsDay>>(
        DataStoreType.ForestStatistics
    )

    override val antForestStatisticsDayFlow: Flow<AntForestStatisticsDay>
        get() = forestDataStore.data

    override suspend fun updateForest(block: (AntForestStatisticsDay) -> AntForestStatisticsDay) {
        forestDataStore.updateData {
            val result = block(it)
            result.copy(
                dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
                date = DateUtils.getYearMonthDay()
            )
        }
    }
}