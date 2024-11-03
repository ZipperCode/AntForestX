package org.xposed.antforestx.core.util

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.xposed.antforestx.core.BuildConfig

object ModuleHelper {

    @JvmStatic
    fun active(): Boolean = false

    @JvmStatic
    fun initModule(loadPackageParam: LoadPackageParam) {
        if (BuildConfig.MODULE_PACKAGE_NAME != loadPackageParam.packageName) {
            return
        }
        findAndHookMethodReplace(this.javaClass, "active") {
            return@findAndHookMethodReplace true
        }
    }
}