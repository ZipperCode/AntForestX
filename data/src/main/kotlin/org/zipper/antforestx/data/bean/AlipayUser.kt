package org.zipper.antforestx.data.bean

import kotlinx.serialization.Serializable
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer


@Serializable
class AlipayUserData : HashMap<String, AlipayUser>() {

    companion object {
        val dsSerializer: BaseDataStoreSerializer<AlipayUserData> by lazy {
            object : BaseDataStoreSerializer<AlipayUserData>(serializer()) {
                override val defaultValue: AlipayUserData
                    get() = AlipayUserData()
            }
        }
    }
}


@Serializable
data class AlipayUser(
    /**
     * 用户名
     */
    val userId: String,
    /**
     * 账号
     */
    val account: String,
    /**
     * 姓名
     */
    val name: String,
    /**
     * 昵称
     */
    val nickname: String,
    /**
     * 显示名称
     */
    val displayName: String,
    /**
     * 是否拉黑
     */
    val blacked: Boolean = false
) {

    val friendNameInfo: String get() = "${displayName}(${account})"
}