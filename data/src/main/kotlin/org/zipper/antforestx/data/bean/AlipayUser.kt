package org.zipper.antforestx.data.bean

import kotlinx.serialization.Serializable


typealias AlipayUserData = HashMap<String, AlipayUser>


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