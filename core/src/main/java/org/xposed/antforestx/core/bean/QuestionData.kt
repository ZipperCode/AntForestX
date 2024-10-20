package org.xposed.antforestx.core.bean

import kotlinx.serialization.Serializable


@Serializable
class QuestionMap:HashMap<String, QuestionData>()

@Serializable
data class QuestionData(
    val questionId: Long,
    val title: String,
    val answer: String
)