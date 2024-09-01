package org.xposed.antforestx.core.hooker

import org.xposed.antforestx.core.bean.ClassMemberWrap

object PedometerAgent {
    private val wrap = ClassMemberWrap.of("com.alibaba.health.pedometer.core.datasource.PedometerAgent")

    private val readDailyStep
        get() = ClassMemberWrap.method(
            "readDailyStep"
        )

    fun readDailyStep(): ClassMemberWrap.Hooker {
        return wrap.method(readDailyStep)
    }

}