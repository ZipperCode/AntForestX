package org.xposed.antforestx.core.hooker

import android.util.Log
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.xposed.antforestx.core.AntRuntime.classLoader
import org.xposed.antforestx.core.bean.ClassMemberWrap
import org.xposed.antforestx.core.util.newInvocation
import timber.log.Timber
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object RpcServiceHooker {

    private val logger get() = Timber.tag("Hooker:RpcService")

    private val proxyCache: ConcurrentMap<Any, Any> = ConcurrentHashMap()

    private val rpcServiceWrap = ClassMemberWrap.of("com.alipay.mobile.framework.service.common.impl.RpcServiceImpl")

    private val getRpcProxyMethod = ClassMemberWrap.method("getRpcProxy", Class::class.java)

    fun hookGetRpcProxy(loadPackageParam: LoadPackageParam) {
        rpcServiceWrap.method(getRpcProxyMethod).after {
            val result = it.result
            val interfaces = if (result.javaClass.isInterface) result.javaClass else result.javaClass.interfaces[0]
            if (interfaces.simpleName != "SimpleRpcService") {
                return@after
            }
            val proxy = Proxy.newProxyInstance(classLoader, arrayOf(interfaces), newInvocation { proxy, method, args ->
                val res = method.invoke(result, *args)
                logger.d("${interfaces.simpleName}#Proxy#${method.name}() => args = ${args.joinToString()}")

                logger.e(
                    "${interfaces.simpleName}#Proxy#${method.name}() <= result = $res"
                )
                if (args[0].toString() == "com.alipay.antfarm.orchardIndex") {
                    Log.w("AntForest", "orchardIndex Trace = ${Log.getStackTraceString(Throwable())}")
                }
                logger.i(
                    "================================================================================================\n"
                )
                return@newInvocation res
            })
            it.result = proxy
        }.hook(loadPackageParam).onFailure {
            logger.d("hookGetRpcProxy failed %s", it.message)
        }
    }
}