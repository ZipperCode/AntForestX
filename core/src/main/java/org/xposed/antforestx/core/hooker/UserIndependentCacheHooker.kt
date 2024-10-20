package org.xposed.antforestx.core.hooker

import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.delay
import org.xposed.antforestx.core.bean.AlipayFriendBean
import org.xposed.antforestx.core.bean.ClassMemberWrap
import org.xposed.antforestx.core.util.getObjectFieldOrDefault
import org.xposed.antforestx.core.util.invokeMethodByName
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

    suspend fun getAllFriends(): Result<List<AlipayFriendBean>> {
        validaInit()
        return runCatching {
            Timber.i("UserIndependentCacheHooker#getCacheObj")
            val daoOpObject = wrap.invokeStaticMethodByName(
                "getCacheObj",
                AliAccountDaoOpWrapType.toClass()
            ) ?: throw NullPointerException("getCacheObj return null")
            Timber.i("UserIndependentCacheHooker#daoOpObject = $daoOpObject")
            val allFriends = daoOpObject.invokeMethodByName(
                "getAllFriends"
            ) as? List<*> ?: throw NullPointerException("getAllFriends return null")
            Timber.i("UserIndependentCacheHooker#allFriends = $allFriends")
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
                Timber.d("UserIndependentCacheHooker# userId: $userId, account: $account, name: $name, nickName: $nickName, remarkName: $remarkName")
            }
            return Result.success(result)
        }.onFailure {
            Timber.e(it, "UserIndependentCacheHooker#getCacheObj error")
        }
    }
}