package org.xposed.antforestx.core.hooker

import org.xposed.antforestx.core.bean.AlipayFriendBean
import org.xposed.antforestx.core.bean.ClassMemberWrap
import org.xposed.antforestx.core.util.Logger
import org.xposed.antforestx.core.util.getObjectFieldOrDefault
import org.xposed.antforestx.core.util.invokeMethodByName

object UserIndependentCacheHooker {

    private val wrap = ClassMemberWrap.of("com.alipay.mobile.socialcommonsdk.bizdata.UserIndependentCache")

    private val AliAccountDaoOpWrapType = ClassMemberWrap.type("com.alipay.mobile.socialcommonsdk.bizdata.contact.data.AliAccountDaoOp")

    fun getAllFriends(): Result<List<AlipayFriendBean>> {
        return runCatching {
            Logger.i("UserIndependentCacheHooker#getCacheObj")
            val daoOpObject = wrap.invokeStaticMethodByName(
                "getCacheObj",
                AliAccountDaoOpWrapType.toClass()
            ) ?: throw NullPointerException("getCacheObj return null")
            Logger.i("UserIndependentCacheHooker#daoOpObject = $daoOpObject")
            val allFriends = daoOpObject.invokeMethodByName(
                "getAllFriends"
            ) as? List<*> ?: throw NullPointerException("getAllFriends return null")
            Logger.i("UserIndependentCacheHooker#allFriends = $allFriends")
            val result = mutableListOf<AlipayFriendBean>()
            allFriends.forEach { friend ->
                val userId = friend.getObjectFieldOrDefault<String>("userId", "")
                val account = friend.getObjectFieldOrDefault<String>("account", "")
                val name = friend.getObjectFieldOrDefault<String>("name", "")
                val nickName = friend.getObjectFieldOrDefault<String>("nickName", "")
                var remarkName = friend.getObjectFieldOrDefault<String>("remarkName", "")
                val accountType = friend.getObjectFieldOrDefault<String>("accountType", "")
                val phoneNo = friend.getObjectFieldOrDefault<String>("phoneNo", "")
                val phoneName = friend.getObjectFieldOrDefault<String>("phoneName", "")
                if (remarkName.isBlank()) {
                    remarkName = nickName
                }
                remarkName += "|$name"
                result.add(AlipayFriendBean(userId, account, name, nickName, remarkName, accountType, phoneNo, phoneName))
                Logger.d("UserIndependentCacheHooker# userId: $userId, account: $account, name: $name, nickName: $nickName, remarkName: $remarkName")
            }
            return Result.success(result)
        }.onFailure {
            Logger.e("UserIndependentCacheHooker#getCacheObj error", it)
        }
    }
}