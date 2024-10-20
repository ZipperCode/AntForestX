package org.xposed.antforestx.core.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.bean.CooperateInfoBean
import org.xposed.antforestx.core.bean.QuestionData
import org.xposed.antforestx.core.bean.QuestionMap
import org.xposed.antforestx.core.bean.VitalityExchangeInfo
import org.xposed.antforestx.core.util.FileDataProvider

/**
 * 获取到的一些信息管理
 * 如：好友信息
 * 如：合种树信息、
 */
object DataInfoManager {

    private val questionDataFlow = MutableStateFlow(QuestionMap())

    suspend fun init() = withContext(Dispatchers.IO) {
        val result = FileDataProvider.loadQuestionCache()
        if (result != null) {
            questionDataFlow.value = result
        }
        questionDataFlow.distinctUntilChangedBy { it.size }
            .flowOn(Dispatchers.IO)
            .collectLatest {
                runCatching {
                    // 配置改变，同步保存本地
                    FileDataProvider.saveQuestionCache(it)
                }
            }
    }

    /**
     * 根据id获取用户名
     */
    fun getFriendById(userId: String): String? {
        return ""
    }

    suspend fun mergeSaveCooperateInfo(cooperateInfo: List<CooperateInfoBean>) {
        FileDataProvider.saveCooperate(cooperateInfo)
    }

    suspend fun updateVitalityInfo(villageInfo: List<VitalityExchangeInfo>) {

    }

    fun getQuestionById(questionId: String): QuestionData? {
        return questionDataFlow.value[questionId]
    }

    fun updateQuestionData(questionData: QuestionData) {
        val newQuestionData = questionDataFlow.value
        newQuestionData[questionData.questionId.toString()] = questionData
        questionDataFlow.value = newQuestionData
    }
}