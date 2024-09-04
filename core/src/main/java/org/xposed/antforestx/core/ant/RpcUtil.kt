package org.xposed.antforestx.core.ant

import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import org.xposed.antforestx.core.hooker.H5RpcUtilHooker
import org.xposed.antforestx.core.util.CoroutineHelper
import org.xposed.antforestx.core.util.Logger
import org.xposed.antforestx.core.util.findClass
import org.xposed.antforestx.core.util.invokeMethodByName
import org.xposed.antforestx.core.util.invokeStaticMethodByName
import java.lang.reflect.InvocationTargetException

object RpcUtil {

    private const val SOCIAL_SDK_CONTACT_SERVICE = "com.alipay.mobile.personalbase.service.SocialSdkContactService"

    init {

    }

    private fun getMicroApplicationContext(): Any? {
        return "com.alipay.mobile.framework.AlipayApplication"
            .invokeStaticMethodByName("getInstance")
            .invokeMethodByName("getMicroApplicationContext")
    }

    fun getUserId(): String? {
        try {
            val callResult = getMicroApplicationContext()
                .invokeMethodByName("findServiceByInterface", SOCIAL_SDK_CONTACT_SERVICE.findClass()?.name)
                .invokeMethodByName("getMyAccountInfoModelByLocal")
            return XposedHelpers.getObjectField(callResult, "userId") as String
        } catch (th: Throwable) {
            Logger.e("", th)
        }
        return null
    }

    suspend fun request(protocol: String, params: String): Result<JSONObject> = withContext(Dispatchers.Default) {
        return@withContext runCatching {
            Logger.d("【RpcUtil】request: protocol = %s, params = %s", protocol, params)
            val responseData = H5RpcUtilHooker.invokeRpcCall13(protocol, params)
            Logger.d("【RpcUtil】response: %s", responseData)

            val jsonObject = JSONObject(responseData)
            val name = jsonObject.optString("name", "")
            if (name.contains("系统繁忙")) {
                throw Exception("系统繁忙")
            } else {
                jsonObject
            }
        }.onFailure {
            Logger.e("【RpcUtil】failure", it)
            if (it is InvocationTargetException) {
                val msg = it.message ?: ""
                if (msg.contains("登录超时")) {
                    Logger.e("登录超时，重启到登录页面")
                    Starter.restartLogin()
                    CoroutineHelper.cancelAllTask()
                } else if (msg.contains("[1004]") && "alipay.antmember.forest.h5.collectEnergy" == protocol) {
                    CoroutineHelper.cancelAllTask()
                }

                // TODO 后续看下
            }
        }
    }
}