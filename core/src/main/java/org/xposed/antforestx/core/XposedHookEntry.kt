package org.xposed.antforestx.core

import android.app.Application
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.xposed.antforestx.core.ant.AntRuntime
import org.xposed.antforestx.core.util.Logger
import org.xposed.antforestx.core.util.ModuleHelper
import org.xposed.antforestx.core.util.getObjectField
import org.xposed.antforestx.core.util.invokeStaticMethodByName

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
        Logger.d("handleLoadPackage >> %s", llparam.packageName)
        classLoader = llparam.classLoader
        ModuleHelper.initModule(llparam)
        AntRuntime.init(llparam)

    }
}