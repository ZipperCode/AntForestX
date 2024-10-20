package org.xposed.antforestx.core

import android.app.Application
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.xposed.antforestx.core.ant.AntRuntime
import org.xposed.antforestx.core.hooker.RpcServiceHooker
import org.xposed.antforestx.core.util.CoroutineHelper
import org.xposed.antforestx.core.util.ModuleHelper
import org.xposed.antforestx.core.util.getObjectField
import org.xposed.antforestx.core.util.invokeStaticMethodByName
import org.xposed.antforestx.core.util.log.init
import timber.log.Timber

class XposedHookEntry : IXposedHookLoadPackage {

    companion object {
        lateinit var classLoader: ClassLoader

        val application: Application by lazy {
            XposedHelpers.findClass("android.app.ActivityThread", classLoader)
                .invokeStaticMethodByName("currentActivityThread")
                .getObjectField<Application>("mInitialApplication") as Application
        }
    }


    override fun handleLoadPackage(llparam: XC_LoadPackage.LoadPackageParam) {
        Timber.init()
        Timber.d("handleLoadPackage >> %s, processName = %s", llparam.packageName, llparam.processName)
        classLoader = llparam.classLoader
        ModuleHelper.initModule(llparam)
        AntRuntime.init(llparam)
//        RpcServiceHooker.hookGetRpcProxy(llparam)

    }
}