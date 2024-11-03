package org.xposed.antforestx.core.hooker

import org.xposed.antforestx.core.bean.ClassMemberWrap


object H5AppRpcUpdate {

    private val wrap = ClassMemberWrap.of("com.alipay.mobile.nebulaappproxy.api.rpc.H5AppRpcUpdate")

    private val matchVersionMethod: ClassMemberWrap.MethodMemberWrap
        get() = ClassMemberWrap.method(
            "matchVersion",
            ClassMemberWrap.ClassTypeWrap("com.alipay.mobile.h5container.api.H5Page"),
            Map::class.java,
            String::class.java
        )

    fun matchVersion(): ClassMemberWrap.Hooker {
        return wrap.method(matchVersionMethod)
    }

}