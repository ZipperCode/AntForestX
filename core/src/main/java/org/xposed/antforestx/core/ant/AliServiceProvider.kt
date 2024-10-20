package org.xposed.antforestx.core.ant

import android.util.Log
import de.robv.android.xposed.XposedHelpers
import org.xposed.antforestx.core.util.findClass
import org.xposed.antforestx.core.util.invokeMethodByName
import org.xposed.antforestx.core.util.invokeStaticMethodByName
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap

object AliServiceProvider {

    private val logger get() = Timber.tag("AliServiceProvider")

    enum class ServiceType(val className: String) {
        RpcService("com.alipay.mobile.framework.service.common.RpcService"),
        SimpleRpcService("com.alipay.mobile.framework.service.ext.SimpleRpcService"),
        SocialSdkContactService("com.alipay.mobile.personalbase.service.SocialSdkContactService"),
        ;
    }

    private val serviceCache: MutableMap<ServiceType, Any?> = ConcurrentHashMap()


    fun getSimpleRpcService(): Any? {
        var service = serviceCache[ServiceType.SimpleRpcService]
        if (service == null) {
            service = getService(ServiceType.RpcService)
                .invokeMethodByName("getRpcProxy", ServiceType.SimpleRpcService.className.findClass())
            if (service != null) {
                serviceCache[ServiceType.SimpleRpcService] = service
            }
        }
        return service
    }

    fun getSocialSdkContactService(): Any? {
        return getService(ServiceType.SocialSdkContactService)
    }

    private fun getService(serviceType: ServiceType): Any? {
        var service = serviceCache[serviceType]
        if (service == null) {
            service = findServiceByInterface(serviceType)
            if (service != null) {
                serviceCache[serviceType] = service
            }
        }
        Timber.tag("Exception").d("getService ${serviceType.name} = $service")
        return service
    }


    private fun getMicroApplicationContext(): Any? {
        return "com.alipay.mobile.framework.AlipayApplication"
            .invokeStaticMethodByName("getInstance")
            .invokeMethodByName("getMicroApplicationContext")
    }

    private fun findServiceByInterface(serviceType: ServiceType): Any? {
        Timber.tag("Test").d(
            "findServiceByInterface: ${serviceType.className} cls = %s",
            XposedHelpers.findClassIfExists(serviceType.className, AntRuntime.classLoader)
        )

        return try {
            getMicroApplicationContext()
                .invokeMethodByName("findServiceByInterface", serviceType.className)
        } catch (e: Exception) {
            logger.e(e)
            Timber.tag("Exception").d("findServiceByInterface ${Log.getStackTraceString(Throwable())}")
            null
        }
    }

}