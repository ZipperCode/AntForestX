package org.xposed.antforestx.core.util

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findClass
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.xposed.antforestx.core.AntRuntime.classLoader
import timber.log.Timber
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

inline fun newUnsafeMethodAfter(
    crossinline after: ((XC_MethodHook.MethodHookParam) -> Unit),
): XC_MethodHook {
    return object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam?) {
            if (param != null) {
                after(param)
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


inline fun newInvocation(crossinline block: (proxy: Any, method: Method, args: Array<out Any>) -> Any?): InvocationHandler {
    return InvocationHandler { proxy, method, args -> block(proxy, method, args) }
}

inline fun <T> runCatch(crossinline block: () -> T): Result<T> {
    return runCatching(block).onFailure {
        XposedBridge.log(it)
    }
}

suspend inline fun runCatchSuspend(crossinline block: suspend () -> Unit) {
    try {
        block.invoke()
    } catch (e: Exception) {
        Timber.tag("Exception").e(e)
    }
}

suspend inline fun <T> runCatchSuspendResult(crossinline block: suspend () -> T): Result<T> {
    try {
        return Result.success(block.invoke())
    } catch (e: Exception) {
        e.printStackTrace()
        return Result.failure(e)
    }
}

fun findAndHookMethodInternal(clazz: Class<*>?, methodName: String, parameterTypes: Array<out Class<*>?>, callback: XC_MethodHook) {
    clazz ?: return
    runCatch {
        val paramList = arrayListOf<Any?>()
        if (parameterTypes.isNotEmpty()) {
            for (any in parameterTypes) {
                paramList.add(any)
            }
        }
        paramList.add(callback)
        XposedHelpers.findAndHookMethod(clazz, methodName, *paramList.toArray())
    }
}

inline fun findAndHookMethod(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline before: ((XC_MethodHook.MethodHookParam) -> Unit),
    crossinline after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    findAndHookMethodInternal(clazz, methodName, parameterTypes, newMethodHook(before, after))
}

inline fun findAndHookMethodBefore(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline before: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    findAndHookMethodInternal(clazz, methodName, parameterTypes, newMethodBefore(before))
}


inline fun findAndHookMethodReplace(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline replace: ((XC_MethodHook.MethodHookParam) -> Any?),
) {
    findAndHookMethodInternal(clazz, methodName, parameterTypes, newMethodReplace(replace))
}


fun findAndHookMethodDoNothing(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?
) {
    findAndHookMethodInternal(clazz, methodName, parameterTypes, XC_MethodReplacement.DO_NOTHING)
}

inline fun findAndHookMethodAfter(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    findAndHookMethodInternal(clazz, methodName, parameterTypes, newMethodAfter(after))
}

inline fun findAndHookMethodUnsafeAfter(
    clazz: Class<*>?,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    findAndHookMethodInternal(clazz, methodName, parameterTypes, newUnsafeMethodAfter(after))
}

fun findAndHookConstructorAfter(
    clazz: Class<*>?,
    vararg parameterTypes: Class<*>?,
    after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    clazz ?: return
    runCatch {
        XposedHelpers.findAndHookConstructor(clazz, *parameterTypes, newMethodAfter(after))
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
        XposedHelpers.findAndHookConstructor(clazz, *parameterTypes, newMethodAfter(after))
    }
}

fun LoadPackageParam.findAndHookConstructorAfter(
    clazz: Class<*>?,
    vararg parameterTypes: Class<*>?,
    after: ((XC_MethodHook.MethodHookParam) -> Unit),
) {
    clazz ?: return
    runCatch {
        XposedHelpers.findAndHookConstructor(clazz, *parameterTypes, newMethodAfter(after))
    }
}

fun LoadPackageParam.findAndHookMethodDoNothing(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>?
) {
    val clazz = XposedHelpers.findClassIfExists(className, classLoader) ?: return
    findAndHookMethodInternal(clazz, methodName, parameterTypes, XC_MethodReplacement.DO_NOTHING)
}

inline fun LoadPackageParam.findAndHookMethodReplace(
    className: String,
    methodName: String,
    vararg parameterTypes: Class<*>?,
    crossinline replace: ((XC_MethodHook.MethodHookParam) -> Any?),
) {
    val clazz = XposedHelpers.findClassIfExists(className, classLoader) ?: return
    findAndHookMethodInternal(clazz, methodName, parameterTypes, newMethodReplace(replace))
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
        Timber.tag("Exception").e(e)
        return null
    }
}

fun Class<*>.invokeStaticMethodByName(methodName: String, vararg args: Any?): Any? {
    try {
        return XposedHelpers.callStaticMethod(this, methodName, *args)
    } catch (e: Exception) {
        Timber.tag("Exception").e(e)
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
        Timber.tag("Exception").e(e)
        return null
    }
}

inline fun <reified T> Any?.getObjectField(fieldName: String): T? {
    try {
        return XposedHelpers.getObjectField(this, fieldName) as? T
    } catch (e: Exception) {
        Timber.tag("Exception").e(e)
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
