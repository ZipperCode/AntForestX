package org.zipper.antforestx.data.bean

import kotlinx.serialization.Serializable
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer


@Serializable
class QuestionMap : HashMap<String, QuestionData>() {

    companion object {
        val dsSerializer: BaseDataStoreSerializer<QuestionMap> by lazy {
            object : BaseDataStoreSerializer<QuestionMap>(serializer()) {
                override val defaultValue: QuestionMap
                    get() = QuestionMap()
            }
        }
    }
}

@Serializable
data class QuestionData(
    val questionId: Long,
    val title: String,
    val answer: String
)