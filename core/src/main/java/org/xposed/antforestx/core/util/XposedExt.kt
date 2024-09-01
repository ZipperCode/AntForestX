package org.xposed.antforestx.core.util

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.xposed.antforestx.core.ant.AntRuntime.classLoader
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * @author zipper
 */

inline fun newMethodBefore(
    crossinline before: ((XC_MethodHook.MethodHookParam) -> Unit),
): XC_MethodHook {
    return object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam?) {
            runCatch {
                if (param != null) {
                    before(param)
                }
            }
        }
    }
}

inline fun newMethodAfter(
    crossinline after: ((XC_MethodHook.MethodHookParam) -> Unit),
): XC_MethodHook {
    return object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam?) {
            runCatch {
                if (param != null) {
                    after(param)
                }
            }
        }
    }
}

inline fun newMethodHook(
    crossinline before: ((XC_MethodHook.MethodHookParam) -> Unit),
    crossinline after: ((XC_MethodHook.MethodHookParam) -> Unit),
): XC_MethodHook {
    return object : XC_MethodHook() {

        override fun beforeHookedMethod(param: MethodHookParam?) {
            runCatch {
                if (param != null) {
                    before(param)
                }
            }
        }

        override fun afterHookedMethod(param: MethodHookParam?) {
            runCatch {
                if (param != null) {
                    after(param)
                }
            }
        }
    }
}

inline fun newMethodReplace(
    crossinline replace: ((XC_MethodHook.MethodHookParam) -> Any?),
): XC_MethodReplacement {
    return object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam?): Any? {
            try {
                if (param != null) {
                    return replace(param)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}


inline fun newInvocation(crossinline block: (proxy: Any, method: Method, args: Array<out Any>) -> Any): InvocationHandler {
    return InvocationHandler { proxy, method, args -> block(proxy, method, args) }
}

inline fun <T> runCatch(crossinline block: () -> T): Result<T> {
    return runCatching(block).onFailure {
        XposedBridge.log(it)
    }
}

inline fun findAndHookMethodBefore(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline before: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    clazz ?: return
    runCatch {
        val paramList = arrayListOf<Any?>()
        val callback = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                runCatch {
                    if (param != null) {
                        before(param)
                    }
                }
            }
        }
        if (parameterTypes.isNotEmpty()) {
            for (any in parameterTypes) {
                paramList.add(any)
            }
        }
        paramList.add(callback)
        XposedHelpers.findAndHookMethod(clazz, methodName, *paramList.toArray())
    }
}

inline fun findAndHookMethodReplace(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline replace: ((XC_MethodHook.MethodHookParam) -> Any?),
) {
    clazz ?: return
    runCatch {
        val paramList = arrayListOf<Any?>()
        val callback = object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                try {
                    if (param != null) {
                        return replace(param)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return null
            }
        }
        if (parameterTypes.isNotEmpty()) {
            for (any in parameterTypes) {
                paramList.add(any)
            }
        }
        paramList.add(callback)
        XposedHelpers.findAndHookMethod(clazz, methodName, *paramList.toArray())
    }
}


fun findAndHookMethodDoNothing(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?
) {
    clazz ?: return
    runCatch {
        val paramList = arrayListOf<Any?>()
        val callback = XC_MethodReplacement.DO_NOTHING
        if (parameterTypes.isNotEmpty()) {
            for (any in parameterTypes) {
                paramList.add(any)
            }
        }
        paramList.add(callback)
        XposedHelpers.findAndHookMethod(clazz, methodName, *paramList.toArray())
    }
}

inline fun findAndHookMethodAfter(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Any?,
    crossinline after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    runCatch {
        val paramList = arrayListOf<Any?>()
        val callback = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                runCatch {
                    if (param != null) {
                        after(param)
                    }
                }
            }
        }
        if (parameterTypes.isNotEmpty()) {
            for (any in parameterTypes) {
                paramList.add(any)
            }
        }
        paramList.add(callback)
        XposedHelpers.findAndHookMethod(clazz, methodName, *paramList.toArray())
    }
}

fun findAndHookConstructorAfter(
    clazz: Class<*>?,
    vararg parameterTypes: Class<*>?,
    after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    clazz ?: return
    runCatch {
        XposedHelpers.findAndHookConstructor(clazz, *parameterTypes, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                runCatch {
                    if (param != null) {
                        after(param)
                    }
                }
            }
        })
    }
}

fun LoadPackageParam.findAndHookMethodBefore(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    before: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    val clazz = XposedHelpers.findClassIfExists(className, classLoader) ?: return
    findAndHookMethodBefore(clazz, methodName, *parameterTypes) {
        before(it)
    }
}

fun LoadPackageParam.findAndHookMethodAfter(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    val clazz = XposedHelpers.findClassIfExists(className, classLoader) ?: return
    findAndHookMethodAfter(clazz, methodName, *parameterTypes) {
        after(it)
    }
}

fun LoadPackageParam.findAndHookConstructorAfter(
    className: String,
    vararg parameterTypes: Class<*>?,
    after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    val clazz = XposedHelpers.findClassIfExists(className, classLoader) ?: return
    runCatch {
        XposedHelpers.findAndHookConstructor(clazz, *parameterTypes, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                runCatch {
                    if (param != null) {
                        after(param)
                    }
                }
            }
        })
    }
}

fun LoadPackageParam.findAndHookConstructorAfter(
    clazz: Class<*>?,
    vararg parameterTypes: Class<*>?,
    after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    clazz ?: return
    runCatch {
        XposedHelpers.findAndHookConstructor(clazz, *parameterTypes, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                runCatch {
                    if (param != null) {
                        after(param)
                    }
                }
            }
        })
    }
}

fun LoadPackageParam.findAndHookMethodDoNothing(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>?
) {
    val clazz = XposedHelpers.findClassIfExists(className, classLoader) ?: return
    runCatch {
        val paramList = arrayListOf<Any?>()
        val callback = XC_MethodReplacement.DO_NOTHING
        if (parameterTypes.isNotEmpty()) {
            for (any in parameterTypes) {
                paramList.add(any)
            }
        }
        paramList.add(callback)
        XposedHelpers.findAndHookMethod(clazz, methodName, *paramList.toArray())
    }
}

inline fun LoadPackageParam.findAndHookMethodReplace(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline replace: ((XC_MethodHook.MethodHookParam) -> Any?),
) {
    val clazz = XposedHelpers.findClassIfExists(className, classLoader) ?: return
    runCatch {
        val paramList = arrayListOf<Any?>()
        val callback = object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                try {
                    if (param != null) {
                        return replace(param)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return null
            }
        }
        if (parameterTypes.isNotEmpty()) {
            for (any in parameterTypes) {
                paramList.add(any)
            }
        }
        paramList.add(callback)
        XposedHelpers.findAndHookMethod(clazz, methodName, *paramList.toArray())
    }
}


fun LoadPackageParam.findClassIfExists(className: String): Class<*>? {
    return XposedHelpers.findClassIfExists(className, classLoader)
}

fun String.findClass(): Class<*>? {
    return findClass(this, classLoader)
}


fun String.invokeStaticMethodByName(methodName: String, vararg args: Any?): Any? {
    try {
        return findClass(this, classLoader)?.invokeStaticMethodByName(methodName, *args)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Class<*>.invokeStaticMethodByName(methodName: String, vararg args: Any?): Any? {
    try {
        return XposedHelpers.callStaticMethod(this, methodName, *args)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Any?.invokeMethodByName(methodName: String, vararg args: Any?): Any? {
    try {
        if (this == null) {
            return null
        }
        return XposedHelpers.callMethod(this, methodName, *args)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

inline fun <reified T> Any?.getObjectField(fieldName: String): T? {
    try {
        return XposedHelpers.getObjectField(this, fieldName) as? T
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

inline fun <reified T> Any?.getObjectFieldOrDefault(fieldName: String, default: T): T {
    try {
        return XposedHelpers.getObjectField(this, fieldName) as? T ?: default
    } catch (e: Exception) {
        e.printStackTrace()
        return default
    }
}
