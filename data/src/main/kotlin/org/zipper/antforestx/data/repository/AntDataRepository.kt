package org.zipper.antforestx.data.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.zipper.antforestx.data.DataStoreType
import org.zipper.antforestx.data.bean.AlipayUser
import org.zipper.antforestx.data.bean.AlipayUserData
import org.zipper.antforestx.data.bean.AntForestPropData
import org.zipper.antforestx.data.bean.QuestionData
import org.zipper.antforestx.data.bean.QuestionMap
import org.zipper.antforestx.data.bean.VitalityExchangedPropData
import timber.log.Timber

internal class AntDataRepository() : IAntDataRepository, KoinComponent {

    private val questionDataStore: DataStore<QuestionMap> by inject<DataStore<QuestionMap>>(
        DataStoreType.Question
    )

    override val questionDataFlow: Flow<QuestionMap>
        get() = questionDataStore.data

    private val vitalityDataStore: DataStore<VitalityExchangedPropData> by inject<DataStore<VitalityExchangedPropData>>(
        DataStoreType.VitalityProp
    )

    override val vitalityDataFlow: Flow<VitalityExchangedPropData>
        get() = vitalityDataStore.data

    private val alipayUserDataStore: DataStore<AlipayUserData> by inject<DataStore<AlipayUserData>>(
        DataStoreType.AlipayUser
    )

    override val alipayUserDataFlow: Flow<AlipayUserData>
        get() = alipayUserDataStore.data

    private val forestPropDataStore: DataStore<AntForestPropData> by inject<DataStore<AntForestPropData>>(
        DataStoreType.ForestProp
    )

    override val forestPropDataFlow: Flow<AntForestPropData>
        get() = forestPropDataStore.data


    override suspend fun getQuestionData(questionId: String): QuestionData? {
        val deferred = CompletableDeferred<QuestionData?>()
        questionDataFlow.collectLatest {
            if (deferred.isCompleted) {
                return@collectLatest
            }
            deferred.complete(it[questionId])
        }
        return deferred.await()
    }

    override suspend fun addQuestionData(questionData: QuestionData) {
        questionDataStore.updateData {
            it[questionData.questionId.toString()] = questionData
            return@updateData it
        }
    }

    override suspend fun updateVitalityData(block: (VitalityExchangedPropData) -> VitalityExchangedPropData) {
        vitalityDataStore.updateData {
            block(it)
        }
    }

    override suspend fun updateAlipayUserData(block: (AlipayUserData) -> AlipayUserData) {
        try {
            val data = AlipayUserData()
            data.put("aaaaa", AlipayUser("11111", "", "", "", "", false))
            data.put("bbbb", AlipayUser("222", "", "", "", "", false))
            data.put("cccc", AlipayUser("111333311", "", "", "", "", false))
            Timber.d("写入数据开始")
            alipayUserDataStore.updateData {
                data
            }
            Timber.d("写入数据完成")
        } catch (e: Exception) {
            Timber.e(e)
            Timber.e("写入数据错误")
        }
    }

    override suspend fun updateForestPropData(block: (AntForestPropData) -> AntForestPropData) {
        forestPropDataStore.updateData {
            block(it)
        }
    }
}