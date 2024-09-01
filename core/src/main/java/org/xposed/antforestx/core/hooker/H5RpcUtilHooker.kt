package org.xposed.antforestx.core.hooker

import org.xposed.antforestx.core.bean.ClassMemberWrap
import org.xposed.antforestx.core.util.Logger
import org.xposed.antforestx.core.util.invokeMethodByName

object H5RpcUtilHooker {

    private val booleanPrimitiveType = Boolean::class.javaPrimitiveType!!
    private val intPrimitiveType = Int::class.javaPrimitiveType!!

    private val wrap = ClassMemberWrap.of("com.alipay.mobile.nebulaappproxy.api.rpc.H5RpcUtil")

    private val h5PageWrapType = ClassMemberWrap.ClassTypeWrap("com.alipay.mobile.h5container.api.H5Page")

    private val fastJsonWrapType = ClassMemberWrap.ClassTypeWrap("com.alibaba.fastjson.JSONObject")

    private val rpcCallMethodWrap12
        get() = ClassMemberWrap.method(
            "rpcCall",
            String::class.java,
            String::class.java,
            String::class.java,
            booleanPrimitiveType,
            fastJsonWrapType,
            String::class.java,
            booleanPrimitiveType,
            h5PageWrapType,
            intPrimitiveType,
            String::class.java,
            booleanPrimitiveType,
            intPrimitiveType
        )

    /**
     * [
     * 1: alipay.mappconfig.appContainerCheck,
     * 2: [{"apFCParams":"{}","appId":"60000002","bizScenario":"","bizScene":"","httpMethod":"GET","insideClient":true,"latestJsapiModifiedTime":0,"newSceneInfo":"{\"appId\":\"20000001\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"clickInfo\":{\"spm\":\"a14.b62.c588.6\"},\"pageInfo\":[{\"spm\":\"a14.b62\"}]}","pre":false,"publicId":"","scene":0,"sourceAppId":"20000001","sourceId":"","targetEmbedWebView":false,"url":"https://render.alipay.com/p/yuyan/180020010001247580/home.html?caprMode=sync&__webview_options__=bc%3D3194732"}],
     * 3: ,
     * 4: true,
     * 5: {},
     * 6: null,
     * 7: false,
     * 8: null,
     * 9: 0,
     * 10: json,
     * 11: false,
     * 12: -1,
     * 13: null
     * ]
     */
    private val rpcCallMethodWrap13
        get() = ClassMemberWrap.method(
            "rpcCall",
            String::class.java,
            String::class.java,
            String::class.java,
            booleanPrimitiveType,
            fastJsonWrapType,
            String::class.java,
            booleanPrimitiveType,
            h5PageWrapType,
            intPrimitiveType,
            String::class.java,
            booleanPrimitiveType,
            intPrimitiveType,
            String::class.java
        )

    private val executeRpcWrap
        get() = ClassMemberWrap.method(
            "executeRpc",
            String::class.java,
            String::class.java
        )

    val rpcCallMethod13: ClassMemberWrap.Invoker by lazy {
        wrap.invoker(rpcCallMethodWrap13)
    }

    fun invokeRpcCall13(protocol: String, params: String): String {
        val response = rpcCallMethod13.invokeStatic(protocol, params, "", true, null, null, false, null, 0, "", false, -1, "")
        return response.invokeMethodByName("getResponse") as? String ?: ""
    }

    fun rpcCall12(): ClassMemberWrap.Hooker {
        return wrap.method(rpcCallMethodWrap12)
    }

    fun rpcCall13(): ClassMemberWrap.Hooker {
        return wrap.method(rpcCallMethodWrap13)
    }


    fun executeRpc(): ClassMemberWrap.Hooker {
        return wrap.method(executeRpcWrap)
    }

    fun printMethods() {
        Logger.d("【AntRuntime】H5RpcUtilHooker printMethods")
        wrap.printMethods()
    }
}