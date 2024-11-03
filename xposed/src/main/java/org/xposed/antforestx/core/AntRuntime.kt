package org.xposed.antforestx.core

import android.app.Application
import android.app.Service
import android.os.Process
import androidx.datastore.core.DataStore
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.xposed.antforestx.core.ant.RpcUtil
import org.xposed.antforestx.core.constant.ClassMember
import org.xposed.antforestx.core.hooker.H5AppRpcUpdate
import org.xposed.antforestx.core.hooker.LauncherActivityHooker
import org.xposed.antforestx.core.hooker.PedometerAgentHooker
import org.xposed.antforestx.core.hooker.RpcServiceHooker
import org.xposed.antforestx.core.hooker.ServiceHooker
import org.xposed.antforestx.core.hooker.UserIndependentCacheHooker
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.tasks.AntForestTask
import org.xposed.antforestx.core.tasks.AntManorTask
import org.xposed.antforestx.core.tasks.AntMemberTask
import org.xposed.antforestx.core.tasks.AntOceanTask
import org.xposed.antforestx.core.tasks.ITask
import org.xposed.antforestx.core.util.AntForestNotification
import org.xposed.antforestx.core.util.AntToast
import org.xposed.antforestx.core.util.findAndHookMethodAfter
import org.xposed.antforestx.core.util.findAndHookMethodBefore
import org.xposed.antforestx.core.util.log.init
import org.xposed.antforestx.core.util.runCatch
import org.xposed.forestx.core.utils.AppCoroutine
import org.zipper.antforestx.data.antDataModule
import timber.log.Timber
import java.io.File


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
        if (packageName != ClassMember.PACKAGE_NAME) {
            return
        }

        if (isMainProgress) {
            Timber.init()
            hookExceptionHandler()

            findAndHookMethodAfter(Application::class.java, "onCreate") {
                logger.d("Application.onCreate(%s)", it.thisObject)
                kotlin.runCatching {
                    val koinApplication = GlobalContext.getKoinApplicationOrNull()
                    if (koinApplication == null) {
                        startKoin {
                            androidContext(it.thisObject as Application)
                            modules(antDataModule)
                        }
                    } else {
                        GlobalContext.loadKoinModules(antDataModule)
                    }
                }
                AppCoroutine.launch {
                    ConfigManager.init()
                }
                AppCoroutine.launch {
                    UserManager.init()
                }
                initHooker(param)
                AppCoroutine.launch {

                    val api26Impl = XposedHelpers.findClassIfExists("androidx.datastore.core.Api26Impl", DataStore::class.java.classLoader)
                    Timber.e("androidx.datastore.core.Api26Impl >> %s", api26Impl)
                    findAndHookMethodAfter(api26Impl, "move", File::class.java, File::class.java) { hookParam ->
                        Timber.e("执行Api26Impl#move")
                        val src = hookParam.args[0] as File
                        val dst = hookParam.args[1] as File
                        val result = hookParam.result
                        Timber.e("src = %s, dst = %s, result = %s", src, dst, result)
                        runCatch {
                            Timber.e("src = %s ", src.exists())
                        }
                        runCatch {
                            Timber.e("dst = %s ", dst.exists())
                        }
                        // Timber.e("dst = %s ", dst.readText())
                    }

                    UserManager.initFriendsData()
//                    initTasks()
                }
            }
        }
        hasInit = true
    }

    private fun hookExceptionHandler() {
        val hookExceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
            // 出现异常不调用支付宝相关的处理
            logger.e(e, "uncaughtException: %s", t)
            AntToast.showShort("程序出现异常，5s后关闭应用")
            runBlocking {
                delay(5_000)
                Process.killProcess(Process.myPid())
            }
        }
        findAndHookMethodBefore(Thread::class.java, "setDefaultUncaughtExceptionHandler", Thread.UncaughtExceptionHandler::class.java) {
            it.args[0] = hookExceptionHandler
        }
    }

    private fun initHooker(loadPackageParam: LoadPackageParam) {
        // 版本
        H5AppRpcUpdate.matchVersion().replace(loadPackageParam) { false }
        // 步数
        PedometerAgentHooker.hookReadDailyStep(loadPackageParam)
        UserIndependentCacheHooker.hookInit(loadPackageParam)
        hookService(loadPackageParam)

        RpcServiceHooker.hookGetRpcProxy(loadPackageParam)

    }

    private suspend fun initTasks() {
        delay(5_000)
        logger.i("任务开始 packageName = %s packageName = %s", packageName, processName)
        AntToast.forceShow("任务开始")
        for (task in tasks) {
            if (ConfigManager.getConfig().basicConfig.isParallel) {
                AppCoroutine.launch {
                    task.start()
                }
            } else {
                task.start()
            }
        }
    }

    private fun hookService(loadPackageParam: LoadPackageParam) {
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