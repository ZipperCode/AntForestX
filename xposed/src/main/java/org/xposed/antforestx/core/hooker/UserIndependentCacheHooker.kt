package org.xposed.antforestx.core.hooker

import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.delay
import org.xposed.antforestx.core.bean.AlipayFriendBean
import org.xposed.antforestx.core.bean.ClassMemberWrap
import org.xposed.antforestx.core.util.getObjectFieldOrDefault
import org.xposed.antforestx.core.util.invokeMethodByName
import org.xposed.antforestx.core.util.moshi
import org.zipper.antforestx.data.bean.AlipayUser
import org.zipper.antforestx.data.bean.AlipayUserData
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean


object UserIndependentCacheHooker {

    private val wrap = ClassMemberWrap.of("com.alipay.mobile.socialcommonsdk.bizdata.UserIndependentCache")

    private val AliAccountDaoOpWrapType = ClassMemberWrap.type("com.alipay.mobile.socialcommonsdk.bizdata.contact.data.AliAccountDaoOp")

    private val initCacheForCurrentUserMethod = ClassMemberWrap.method("initCacheForCurrentUser")

    private val hasInit = AtomicBoolean(false)

    fun hookInit(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        if (hasInit.get()) {
            Timber.tag("UserIndependentCacheHooker").d("initCacheForCurrentUser#hasInit")
            return
        }
        wrap.method(initCacheForCurrentUserMethod).after {
            Timber.tag("UserIndependentCacheHooker").d("initCacheForCurrentUser#after")
            hasInit.set(true)
        }.hook(loadPackageParam).onFailure {
            Timber.tag("UserIndependentCacheHooker").e(it, "initCacheForCurrentUser#error")
        }
    }

    private suspend fun validaInit() {
        var count = 0
        while (!hasInit.get() || count < 10) {
            delay(500)
            count++
        }
    }

    suspend fun getFriendsMap(): Result<AlipayUserData> {
        validaInit()
        return runCatching {
            val userListData = AlipayUserData()
            val daoOpObject = wrap.invokeStaticMethodByName("getCacheObj", AliAccountDaoOpWrapType.toClass())
                ?: throw NullPointerException("getCacheObj return null")
            val allFriends = daoOpObject.invokeMethodByName("getAllFriends") as? List<*>
                ?: throw NullPointerException("getAllFriends return null")
            Timber.d("UserIndependentCacheHooker#allFriends = %s", moshi.adapter(List::class.java).toJson(allFriends))
            allFriends.forEach { friend ->
                val userId = friend.getObjectFieldOrDefault<String>("userId", "")
                val account = friend.getObjectFieldOrDefault<String>("account", "")
                val name = friend.getObjectFieldOrDefault<String>("name", "")
                val nickName = friend.getObjectFieldOrDefault<String>("nickName", "")
                val displayName = friend.getObjectFieldOrDefault<String>("displayName", "")
                var remarkName = friend.getObjectFieldOrDefault<String>("remarkName", "")
                val blacked = friend.getObjectFieldOrDefault<Boolean>("blacked", false)
                if (remarkName.isBlank()) {
                    remarkName = nickName
                }
                remarkName += "|$name"
                val item = AlipayUser(userId, account, name, nickName, displayName, blacked)
                userListData[userId] = item
                Timber.d("UserIndependentCacheHooker# AlipayUser = %s", item)
            }
            return Result.success(userListData)
        }.onFailure {
            Timber.e(it, "UserIndependentCacheHooker#getCacheObj error")
        }
    }
}