package org.xposed.antforestx.core.hooker

import android.os.Bundle
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.xposed.antforestx.core.bean.ClassMemberWrap
import timber.log.Timber

object SocialSdkContactServiceHooker {

    private val wrap = ClassMemberWrap.of("com.alipay.mobile.socialcontactsdk.contact.data.SocialSdkContactServiceImpl")

    private val onCreateMethod = ClassMemberWrap.method("onCreate", Bundle::class.java)

    fun hookOnCreate(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        Timber.tag("SocialSdkContactServiceHooker").d("onCreate#start")
        wrap.method(onCreateMethod).after {
            Timber.tag("SocialSdkContactServiceHooker").d("onCreate#after")
        }.hook(loadPackageParam).onFailure {
            Timber.tag("SocialSdkContactServiceHooker").e(it, "onCreate#hook failed")
        }
    }
}