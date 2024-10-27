package org.zipper.antforestx.data.repository

import kotlinx.coroutines.flow.Flow
import org.zipper.antforestx.data.record.AntForestStatisticsDay

interface IAntStatisticsRepository {
    /**
     * 当天的统计
     */
    val antForestStatisticsDayFlow: Flow<AntForestStatisticsDay>

    suspend fun updateForest(block: (AntForestStatisticsDay) -> AntForestStatisticsDay)
}