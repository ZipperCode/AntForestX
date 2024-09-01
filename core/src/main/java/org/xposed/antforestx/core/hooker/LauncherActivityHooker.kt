package org.xposed.antforestx.core.hooker

import android.os.Bundle
import org.xposed.antforestx.core.bean.ClassMemberWrap

object LauncherActivityHooker {

    private val wrap = ClassMemberWrap.of("com.alipay.mobile.quinox.LauncherActivity")

    private val onCreate get() = ClassMemberWrap.method("onCreate", Bundle::class.java)
    private val onResume get() = ClassMemberWrap.method("onResume")

    fun onCreate(): ClassMemberWrap.Hooker {
        return wrap.method(onCreate)
    }

    fun onResume(): ClassMemberWrap.Hooker {
        return wrap.method(onResume)
    }
}