package org.xposed.antforestx.core.manager

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.xposed.antforestx.core.bean.CooperateInfoBean
import org.xposed.antforestx.core.util.FileDataProvider
import org.zipper.antforestx.data.bean.AntForestPropData
import org.zipper.antforestx.data.bean.QuestionData
import org.zipper.antforestx.data.bean.VitalityExchangedPropData
import org.zipper.antforestx.data.repository.IAntDataRepository

/**
 * 获取到的一些信息管理
 * 如：好友信息
 * 如：合种树信息、
 */
object DataInfoManager : KoinComponent {

    private val dataRepository: IAntDataRepository by inject<IAntDataRepository>()

    /**
     * 根据id获取用户名
     */
    fun getFriendById(userId: String): String? {
        return ""
    }

    suspend fun mergeSaveCooperateInfo(cooperateInfo: List<CooperateInfoBean>) {
        FileDataProvider.saveCooperate(cooperateInfo)
    }

    suspend fun updateVitalityInfo(vitalityPropData: VitalityExchangedPropData) {
        dataRepository.updateVitalityData {
            vitalityPropData
        }
    }

    suspend fun getQuestionById(questionId: String): QuestionData? {
        return dataRepository.getQuestionData(questionId)
    }

    suspend fun updateQuestionData(questionData: QuestionData) {
        dataRepository.addQuestionData(questionData)
    }

    suspend fun updateForestPropData(forestPropData: AntForestPropData) {
        dataRepository.updateForestPropData {
            forestPropData
        }
    }
}