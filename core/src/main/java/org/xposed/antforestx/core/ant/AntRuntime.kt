package org.xposed.antforestx.core.ant

import android.app.Application
import android.app.Service
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.xposed.antforestx.core.constant.ClassMember
import org.xposed.antforestx.core.hooker.H5AppRpcUpdate
import org.xposed.antforestx.core.hooker.H5RpcUtilHooker
import org.xposed.antforestx.core.hooker.LauncherActivityHooker
import org.xposed.antforestx.core.hooker.PedometerAgent
import org.xposed.antforestx.core.hooker.ServiceHooker
import org.xposed.antforestx.core.hooker.UserIndependentCacheHooker
import org.xposed.antforestx.core.tasks.AntForestNotification
import org.xposed.antforestx.core.util.Logger
import org.xposed.antforestx.core.util.findAndHookMethodAfter

object AntRuntime {

    var packageName = ""
        private set
    var processName = ""
        private set

    lateinit var classLoader: ClassLoader

    val isMainProgress get() = packageName == processName

    private var hasInit = false

    fun init(param: LoadPackageParam) {
        if (hasInit) {
            return
        }
        packageName = param.packageName
        processName = param.processName
        classLoader = param.classLoader

        if (isMainProgress) {
            Logger.d("【AntRuntime】init package = %s, process = %s", packageName, processName)
            findAndHookMethodAfter(Application::class.java, "onCreate") {
                Logger.d("【AntRuntime】Application.onCreate(%s)", it.thisObject)
                initHooker(param)
            }
//            initHooker(param)
        }
        hasInit = true
    }

    private fun initHooker(loadPackageParam: LoadPackageParam) {
        // 版本
        H5AppRpcUpdate.matchVersion().replace(loadPackageParam) {
            Logger.d("【AntRuntime】H5AppRpcUpdate.matchVersion()")
            return@replace false
        }.onFailure {
            Logger.e("【AntRuntime】H5AppRpcUpdate.matchVersion()", it)
        }.onSuccess {
            Logger.d("【AntRuntime】H5AppRpcUpdate.matchVersion() success")
        }

        // 步数
        PedometerAgent.readDailyStep().after { param ->
            // 读取日步数
            Logger.d("【AntRuntime】PedometerAgent.readDailyStep() => %s", param.result)
        }.hook(loadPackageParam).onSuccess {
            Logger.d("【AntRuntime】PedometerAgent.readDailyStep() success")
        }.onFailure {
            Logger.e("【AntRuntime】PedometerAgent.readDailyStep()", it)
        }
        hookService(loadPackageParam)
        H5RpcUtilHooker.printMethods()

        H5RpcUtilHooker.rpcCall13().before { param ->
            Logger.d("【AntRuntime】H5RpcUtilHooker.rpcCall13() => %s", param.args.contentToString())
        }.after { param ->
            Logger.d("【AntRuntime】H5RpcUtilHooker.rpcCall13() => %s", param.result)
        }.hook(loadPackageParam).onSuccess {
            Logger.d("【AntRuntime】H5RpcUtilHooker.rpcCall13() success")
        }.onFailure {
            Logger.e("【AntRuntime】H5RpcUtilHooker.rpcCall13()", it)
        }

        H5RpcUtilHooker.executeRpc().before { param ->
            Logger.d("【AntRuntime】H5RpcUtilHooker.executeRpc() => %s", param.args.contentToString())
        }.hook(loadPackageParam).onSuccess {
            Logger.d("【AntRuntime】H5RpcUtilHooker.executeRpc() success")
        }.onFailure {
            Logger.e("【AntRuntime】H5RpcUtilHooker.executeRpc()", it)
        }
    }

    private fun hookService(loadPackageParam: LoadPackageParam) {
        LauncherActivityHooker.onCreate().after { param ->
            Logger.d("【AntRuntime】LauncherActivityHooker.onCreate() => %s", param.thisObject)
        }.hook(loadPackageParam).onSuccess {
            Logger.d("【AntRuntime】LauncherActivityHooker.onCreate() success")
        }.onFailure {
            Logger.e("【AntRuntime】LauncherActivityHooker.onCreate()", it)
        }
        // 服务
        LauncherActivityHooker.onResume().after { param ->
            Logger.d("【AntRuntime】LauncherActivityHooker.onResume() => %s", param.thisObject)
            val userId = RpcUtil.getUserId() ?: return@after
            Logger.d("【AntRuntime】LauncherActivityHooker.onResume() userId => %s", userId)
            UserIndependentCacheHooker.getAllFriends()
        }.hook(loadPackageParam).onSuccess {
            Logger.d("【AntRuntime】LauncherActivityHooker.onResume() success")
        }.onFailure {
            Logger.e("【AntRuntime】LauncherActivityHooker.onResume()", it)
        }

        ServiceHooker.onCreate().after { param ->
            val service = param.thisObject as Service
            if (ClassMember.CURRENT_USING_SERVICE != service.javaClass.canonicalName) {
                return@after
            }
            Logger.d("【AntRuntime】ServiceHooker.onCreate() => %s", param.thisObject)
            AntForestNotification.start(service)
            // AntForestNotification.setContentText("运行中...")

        }.hook(loadPackageParam).onSuccess {
            Logger.d("【AntRuntime】ServiceHooker.onCreate() success")
        }.onFailure {
            Logger.e("【AntRuntime】ServiceHooker.onCreate()", it)
        }
    }
}