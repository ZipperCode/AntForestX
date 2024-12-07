package org.zipper.antforestx.data.repository

import kotlinx.coroutines.flow.Flow
import org.zipper.antforestx.data.bean.AlipayUserData
import org.zipper.antforestx.data.bean.AntForestPropData
import org.zipper.antforestx.data.bean.CooperateData
import org.zipper.antforestx.data.bean.CooperateInfoBean
import org.zipper.antforestx.data.bean.QuestionData
import org.zipper.antforestx.data.bean.QuestionMap
import org.zipper.antforestx.data.bean.VitalityExchangedPropData

interface IAntDataRepository {

    val vitalityDataFlow: Flow<VitalityExchangedPropData>

    val questionDataFlow: Flow<QuestionMap>

    val alipayUserDataFlow: Flow<AlipayUserData>

    val forestPropDataFlow: Flow<AntForestPropData>

    val cooperateInfoBeanFlow: Flow<CooperateData>

    suspend fun getQuestionData(questionId: String): QuestionData?

    suspend fun addQuestionData(questionData: QuestionData)

    suspend fun updateVitalityData(block: (VitalityExchangedPropData) -> VitalityExchangedPropData)

    suspend fun updateAlipayUserData(block: (AlipayUserData) -> AlipayUserData)

    suspend fun updateForestPropData(block: (AntForestPropData) -> AntForestPropData)
}