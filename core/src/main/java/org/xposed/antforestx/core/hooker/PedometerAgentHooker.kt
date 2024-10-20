package org.xposed.antforestx.core.hooker

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.xposed.antforestx.core.bean.ClassMemberWrap
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.RandomUtils
import timber.log.Timber
import kotlin.math.min

object PedometerAgentHooker {
    private val wrap = ClassMemberWrap.of("com.alibaba.health.pedometer.core.datasource.PedometerAgent")

    private val readDailyStep
        get() = ClassMemberWrap.method(
            "readDailyStep"
        )

    fun hookReadDailyStep(loadPackageParam: LoadPackageParam) {
        wrap.method(readDailyStep).after { param ->
            // 读取日步数
            val originStep = param.result as Int
            val customStep = ConfigManager.otherConfig.customStepNum
            if (originStep >= customStep) {
                return@after
            }
            if (UserManager.forestDayRecord.isSyncStep) {
                return@after
            }
            val newStep = getRandomStep()
            UserManager.updateNewStep(newStep)
            Timber.tag("PedometerAgent").d("readDailyStep#before => %s", param.result)
            param.result = newStep
            Timber.tag("PedometerAgent").d("readDailyStep#after => %s", param.result)
        }.hook(loadPackageParam).onFailure {
            Timber.tag("PedometerAgent").e(it, "readDailyStep()")
        }
    }

    private fun getRandomStep(): Int {
        val step = UserManager.forestDayRecord.todayStepNum
        val newStep = RandomUtils.nextInt(step, step + 2000)
        return min(min(ConfigManager.otherConfig.customStepNum, 50000), newStep)
    }
}