package org.xposed.antforestx.core.hooker

import android.app.Service
import org.xposed.antforestx.core.bean.ClassMemberWrap

object ServiceHooker {

    private val wrap = ClassMemberWrap.of(Service::class.java.name)

    private val onCreate get() = ClassMemberWrap.method("onCreate")

    private val onDestroy get() = ClassMemberWrap.method("onDestroy")

    fun onCreate(): ClassMemberWrap.Hooker {
        return wrap.method(onCreate)
    }

    fun onDestroy(): ClassMemberWrap.Hooker {
        return wrap.method(onDestroy)
    }
}