package org.xposed.antforestx.core.ant

import android.app.Application
import android.app.Service
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import kotlinx.coroutines.delay
import org.xposed.antforestx.core.constant.ClassMember
import org.xposed.antforestx.core.hooker.H5AppRpcUpdate
import org.xposed.antforestx.core.hooker.LauncherActivityHooker
import org.xposed.antforestx.core.hooker.PedometerAgentHooker
import org.xposed.antforestx.core.hooker.RpcServiceHooker
import org.xposed.antforestx.core.hooker.ServiceHooker
import org.xposed.antforestx.core.hooker.SocialSdkContactServiceHooker
import org.xposed.antforestx.core.hooker.UserIndependentCacheHooker
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.DataInfoManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.tasks.AntForestTask
import org.xposed.antforestx.core.tasks.AntManorTask
import org.xposed.antforestx.core.tasks.AntMemberTask
import org.xposed.antforestx.core.tasks.AntOceanTask
import org.xposed.antforestx.core.tasks.ITask
import org.xposed.antforestx.core.util.AntForestNotification
import org.xposed.antforestx.core.util.AntToast
import org.xposed.antforestx.core.util.CoroutineHelper
import org.xposed.antforestx.core.util.findAndHookMethodAfter
import timber.log.Timber


object AntRuntime {

    private val logger get() = Timber.tag("AntRuntime")

    private var packageName = ""
    private var processName = ""

    lateinit var classLoader: ClassLoader

    private val isMainProgress get() = packageName == processName

    private var hasInit = false

    private val tasks: List<ITask> = listOf(
        AntForestTask(),
        AntOceanTask(),
        AntMemberTask(),
        AntManorTask()
    )

    fun init(param: LoadPackageParam) {
        if (hasInit) {
            return
        }
        packageName = param.packageName
        processName = param.processName
        classLoader = param.classLoader

        if (isMainProgress) {
            logger.d("init package = %s, process = %s", packageName, processName)
            findAndHookMethodAfter(Application::class.java, "onCreate") {
                logger.d("Application.onCreate(%s)", it.thisObject)
                CoroutineHelper.launch {
                    ConfigManager.init()
                }
                CoroutineHelper.launch {
                    UserManager.init()
                }
                CoroutineHelper.launch {
                    DataInfoManager.init()
                }
                initHooker(param)
                CoroutineHelper.launch {
                    delay(5_000)
                    logger.i("任务开始 packageName = %s packageName = %s", packageName, processName)
                    AntToast.forceShow("任务开始")
//                    AntWorkScheduler.setAlarm7(it.thisObject as Context)
                    for (task in tasks) {
                        if (ConfigManager.getConfig().basicConfig.isParallel) {
                            CoroutineHelper.launch {
                                task.start()
                            }
                        } else {
                            task.start()
                        }
                    }
                }
            }
//            initHooker(param)
        }
        hasInit = true
    }

    private fun initHooker(loadPackageParam: LoadPackageParam) {
        // 版本
        H5AppRpcUpdate.matchVersion().replace(loadPackageParam) {
            logger.d("H5AppRpcUpdate.matchVersion()")
            return@replace false
        }.onFailure {
            logger.e(it, "H5AppRpcUpdate.matchVersion()")
        }.onSuccess {
            logger.d("H5AppRpcUpdate.matchVersion() success")
        }

        // 步数
        PedometerAgentHooker.hookReadDailyStep(loadPackageParam)
        UserIndependentCacheHooker.hookInit(loadPackageParam)
        SocialSdkContactServiceHooker.hookOnCreate(loadPackageParam)
        hookService(loadPackageParam)

        RpcServiceHooker.hookGetRpcProxy(loadPackageParam)

    }

    private fun hookService(loadPackageParam: LoadPackageParam) {
//        LauncherActivityHooker.onCreate().after { param ->
//            logger.d("LauncherActivityHooker.onCreate() => %s", param.thisObject)
//        }.hook(loadPackageParam).onSuccess {
//            logger.d("LauncherActivityHooker.onCreate() success")
//        }.onFailure {
//            logger.e(it, "LauncherActivityHooker.onCreate()")
//        }
        // 服务
        LauncherActivityHooker.onResume().after { param ->
            logger.d("LauncherActivityHooker.onResume() => %s", param.thisObject)
            val userId = RpcUtil.getUserId() ?: return@after
            logger.d("LauncherActivityHooker.onResume() userId => %s", userId)
        }.hook(loadPackageParam).onSuccess {
            logger.d("LauncherActivityHooker.onResume() success")
        }.onFailure {
            logger.e(it, "LauncherActivityHooker.onResume()")
        }

        ServiceHooker.onCreate().after { param ->
            val service = param.thisObject as Service
            if (ClassMember.CURRENT_USING_SERVICE != service.javaClass.canonicalName) {
                return@after
            }
            logger.d("【AntRuntime】ServiceHooker.onCreate() => %s", param.thisObject)
            AntForestNotification.start(service)
            // AntForestNotification.setContentText("运行中...")

        }.hook(loadPackageParam).onSuccess {
            logger.d("【AntRuntime】ServiceHooker.onCreate() success")
        }.onFailure {
            logger.e(it, "【AntRuntime】ServiceHooker.onCreate()")
        }
    }
}