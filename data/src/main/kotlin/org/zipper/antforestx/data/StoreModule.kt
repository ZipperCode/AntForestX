package org.zipper.antforestx.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import kotlinx.serialization.serializer
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.dsl.bind
import org.koin.dsl.module
import org.zipper.antforestx.data.bean.AntForestPropData
import org.zipper.antforestx.data.bean.CooperateInfoBean
import org.zipper.antforestx.data.bean.QuestionMap
import org.zipper.antforestx.data.bean.VitalityExchangedPropData
import org.zipper.antforestx.data.config.AntConfig
import org.zipper.antforestx.data.provider.StoreFileProvider
import org.zipper.antforestx.data.record.AntForestStatisticsDay
import org.zipper.antforestx.data.repository.AntConfigRepository
import org.zipper.antforestx.data.repository.AntDataRepository
import org.zipper.antforestx.data.repository.AntStatisticsRepository
import org.zipper.antforestx.data.repository.IAntConfigRepository
import org.zipper.antforestx.data.repository.IAntDataRepository
import org.zipper.antforestx.data.repository.IAntStatisticsRepository
import org.zipper.antforestx.data.repository.ILogcatRepository
import org.zipper.antforestx.data.repository.LogcatRepository
import org.zipper.antforestx.data.serializer.AlipayUserDataSerializer
import org.zipper.antforestx.data.serializer.DataSerializer
import java.io.File


private inline fun <reified T> createDataStore(
    dsSerializer: Serializer<T>,
    noinline produceFile: () -> File
): DataStore<T> {
    return DataStoreFactory.create(
        serializer = dsSerializer,
        produceFile = produceFile
    )
}

enum class DataStoreType : Qualifier {
    Config,
    Question,
    VitalityProp,
    AlipayUser,
    ForestProp,
    ForestStatistics,

    // 合种数信息
    CooperateData,
    ;

    override val value: QualifierValue get() = name
}

val antDataModule = module {
    single(DataStoreType.Config) {
        createDataStore(DataSerializer(serializer(), AntConfig())) {
            StoreFileProvider.requireAntConfigFile()
        }
    }

    single(DataStoreType.Question) {
        createDataStore(QuestionMap.dsSerializer) {
            StoreFileProvider.requireQuestionDataFile()
        }
    }
    single(DataStoreType.VitalityProp) {
        createDataStore(VitalityExchangedPropData.dsSerializer) {
            StoreFileProvider.requireVitalityPropDataFile()
        }
    }
    single(DataStoreType.AlipayUser) {
        createDataStore(AlipayUserDataSerializer()) {
            StoreFileProvider.requireAlipayUserDataFile()
        }
    }

    single(DataStoreType.ForestProp) {
        createDataStore(DataSerializer(serializer(), AntForestPropData())) {
            StoreFileProvider.requireForestPropDataFile()
        }
    }

    single(DataStoreType.ForestStatistics) {
        createDataStore(AntForestStatisticsDay.dsSerializer) {
            StoreFileProvider.requireForestStatisticsDayFile()
        }
    }

    single(DataStoreType.CooperateData) {
        createDataStore(DataSerializer(serializer(), CooperateInfoBean())) {
            StoreFileProvider.requireCooperateDataFile()
        }
    }

    single { AntConfigRepository() } bind IAntConfigRepository::class

    single { AntDataRepository() } bind IAntDataRepository::class

    single { AntStatisticsRepository() } bind IAntStatisticsRepository::class

    single { LogcatRepository() } bind ILogcatRepository::class
}