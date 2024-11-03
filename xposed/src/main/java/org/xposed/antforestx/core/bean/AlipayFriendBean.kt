package org.xposed.antforestx.core.bean

import kotlinx.serialization.Serializable

/**
 * 好友
 * @author zipper
 *
 * @param userId 用户id 对应 userId
 * @param account 账号 对应 account
 * @param name 姓名 对应 name
 * @param nickname 昵称 对应 nickName
 * @param accountType 账号类型 对应 accountType
 * @param phoneNo 手机号 对应 phoneNo
 * @param phoneName 手机显示名称 对应 phoneName
 */
@Serializable
data class AlipayFriendBean(
    val userId: String,
    val account: String,
    val name: String,
    val nickname: String,
    val remarkName:String,
    val accountType: String,
    val phoneNo: String,
    val phoneName: String
)