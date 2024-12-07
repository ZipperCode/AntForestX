package org.xposed.antforestx.core.util

import android.content.pm.ApplicationInfo
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.util.concurrent.CopyOnWriteArraySet


object XposedByPass {

    val classloaders = CopyOnWriteArraySet<ClassLoader>()

    fun init(param: LoadPackageParam) {
        findAndHookMethodUnsafeAfter(ClassLoader::class.java, "loadClass", String::class.java) {
            if (it.args.getOrNull(0) != null) {
                if (it.args[0].toString().startsWith("de.robv.android.xposed")) {
                    throw ClassNotFoundException()
                }
            }
            if (it.result != null) {
                it.result.javaClass.classLoader?.let { classLoader ->
                    classloaders.add(classLoader)
                }
            }
        }

        findAndHookMethodAfter(StackTraceElement::class.java, "getClassName"){
            if (it.result.toString().startsWith("de.robv.android.xposed")) {
                it.result = ""
            }
        }
    }
}