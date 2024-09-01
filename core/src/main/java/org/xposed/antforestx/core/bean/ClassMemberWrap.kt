package org.xposed.antforestx.core.bean

import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.xposed.antforestx.core.ant.AntRuntime
import org.xposed.antforestx.core.ant.AntRuntime.classLoader
import org.xposed.antforestx.core.util.Logger
import org.xposed.antforestx.core.util.invokeStaticMethodByName
import org.xposed.antforestx.core.util.newMethodHook
import org.xposed.antforestx.core.util.newMethodReplace
import java.lang.reflect.Method
import java.lang.reflect.Type

private val classCache: MutableMap<ClassLoader, MutableMap<String, Class<*>>> = mutableMapOf()

private fun findClass(className: String, classLoader: ClassLoader): Class<*>? {
    val findClass = classCache[classLoader]?.get(className)
    if (findClass == null) {
        val clazz = XposedHelpers.findClassIfExists(className, classLoader)
        if (clazz != null) {
            var map = classCache[classLoader]
            if (map == null) {
                map = mutableMapOf()
                classCache[classLoader] = map
            }
            map[className] = clazz
        }
    }
    return findClass
}

@Suppress("UNUSED_EXPRESSION")
@JvmInline
value class ClassMemberWrap(
    val className: String
) {

    companion object {

        fun of(className: String): ClassMemberWrap {
            return ClassMemberWrap(className)
        }

        fun method(methodName: String, vararg parameterTypes: Type): MethodMemberWrap {
            return MethodMemberWrap(
                methodName,
                parameterTypes
            )
        }

        fun type(className: String): ClassTypeWrap {
            return ClassTypeWrap(className)
        }
    }

    @JvmInline
    value class ClassTypeWrap(
        val className: String
    ) : Type {

        fun toClass(): Class<*> {
            return XposedHelpers.findClass(className, AntRuntime.classLoader)
        }
    }

    fun printMethods() {
        val clazz = XposedHelpers.findClassIfExists(className, AntRuntime.classLoader)
        val name = clazz.simpleName
        for (declaredMethod in clazz.declaredMethods) {
            Logger.i(
                "PrintMethods => %s",
                "${declaredMethod.returnType} ${name}#${declaredMethod.name}(${declaredMethod.parameterTypes.joinToString(", ") { it.name }})"
            )
        }
    }

    fun method(methodWrap: MethodMemberWrap): Hooker {
        return Hooker(this, methodWrap)
    }

    fun invokeStaticMethodByName(methodName: String, vararg args: Any?): Any? {
        return findClass(className, AntRuntime.classLoader)?.invokeStaticMethodByName(methodName, *args)
    }

    fun invoker(methodWrap: MethodMemberWrap): Invoker {
        val findClazz = findClass(className, AntRuntime.classLoader)
        if (findClazz == null) {
            Logger.e("invoker#findClass $className error")
            return Invoker(this, methodWrap)
        }
        val result = runCatching {
            val paramList = arrayListOf<Class<*>>()
            if (methodWrap.parameterTypes.isNotEmpty()) {
                for (any in methodWrap.parameterTypes) {
                    var type = any
                    if (any is ClassTypeWrap) {
                        type = XposedHelpers.findClass(any.className, classLoader)
                    }
                    if (type is Class<*>) {
                        paramList.add(type)
                    }
                }
            }
            findClazz.getDeclaredMethod(methodWrap.methodName, *paramList.toTypedArray())
        }.onFailure {
            Logger.e("invoker#getDeclareMethod ${methodWrap.methodName} error", it)
        }
        if (result.isFailure) {
            return Invoker(this, methodWrap)
        }

        return Invoker(this, methodWrap, result.getOrNull())
    }

    class MethodMemberWrap(
        val methodName: String,
        val parameterTypes: Array<out Type>
    )

    class Hooker(
        private val classWrap: ClassMemberWrap,
        private val methodWrap: MethodMemberWrap,
        private var _before: ((MethodHookParam) -> Unit)? = null,
        private var _after: ((MethodHookParam) -> Unit)? = null

    ) {

        private fun findClass(classLoader: ClassLoader): Class<*>? {
            var findClass = classCache[classLoader]?.get(classWrap.className)
            if (findClass == null) {
                val clazz = XposedHelpers.findClassIfExists(classWrap.className, classLoader)
                if (clazz != null) {
                    var map = classCache[classLoader]
                    if (map == null) {
                        map = mutableMapOf()
                        classCache[classLoader] = map
                    }
                    map[classWrap.className] = clazz
                    findClass = clazz
                }
            }
            return findClass
        }

        private fun getParamTypeList(classLoader: ClassLoader): ArrayList<Any?> {
            val paramList = arrayListOf<Any?>()
            if (methodWrap.parameterTypes.isNotEmpty()) {
                for (any in methodWrap.parameterTypes) {
                    var type = any
                    if (any is ClassTypeWrap) {
                        type = XposedHelpers.findClass(any.className, classLoader)
                    }
                    paramList.add(type)
                }
            }
            return paramList
        }

        fun before(block: (MethodHookParam) -> Unit): Hooker {
            this._before = block
            return this
        }

        fun after(block: (MethodHookParam) -> Unit): Hooker {
            this._after = block
            return this
        }

        fun hook(param: LoadPackageParam): Result<Unit> {
            val findClass = findClass(param.classLoader) ?: return Result.failure(RuntimeException("${classWrap.className}未找到"))

            return runCatching {
                val callback = newMethodHook({
                    _before?.invoke(it)
                }, {
                    _after?.invoke(it)
                })
                val paramList = getParamTypeList(param.classLoader)
                paramList.add(callback)
                XposedHelpers.findAndHookMethod(
                    findClass,
                    methodWrap.methodName,
                    *paramList.toTypedArray()
                )
            }
        }

        fun replace(param: LoadPackageParam, block: (MethodHookParam) -> Any): Result<Unit> {
            val findClass = findClass(param.classLoader) ?: return Result.failure(RuntimeException("${classWrap.className}未找到"))

            return runCatching {
                val callback = newMethodReplace {
                    return@newMethodReplace block.invoke(it)
                }
                val paramList = getParamTypeList(param.classLoader)
                paramList.add(callback)
                XposedHelpers.findAndHookMethod(
                    findClass,
                    methodWrap.methodName,
                    *paramList.toTypedArray()
                )
            }
        }
    }

    class Invoker(
        private val classWrap: ClassMemberWrap,
        private val methodWrap: MethodMemberWrap,
        private val method: Method? = null
    ) {

        fun invokeStatic(vararg args: Any?): Result<Any?> {
            if (method == null) {
                return Result.failure(RuntimeException("${classWrap.className}#${methodWrap.methodName}未找到"))
            }
            return runCatching {
                method!!.invoke(null, *args)
            }
        }

        fun invoke(target: Any, vararg args: Any?): Result<Any?> {
            if (method == null) {
                return Result.failure(RuntimeException("${classWrap.className}#${methodWrap.methodName}未找到"))
            }
            return runCatching {
                method!!.invoke(target, *args)
            }
        }
    }
}