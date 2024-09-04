package org.xposed.antforestx.core.tasks

import android.util.Log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xposed.antforestx.core.ant.AncientTreeRpcCall
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.CoroutineHelper
import org.xposed.antforestx.core.util.Logger

/**
 *
 * @author  zhangzhipeng
 * @date    2024/9/4
 */
class AntAncientTreeTask {

    suspend fun start() = withContext(Dispatchers.IO + CoroutineName("AntAncientTree")) {
        if (!ConfigManager.enableAncientTree) {
            Logger.i("保护古树未开启")
            return@withContext
        }
        Logger.d("保护古树任务开始执行")
        val userId = UserManager.waitGetCurrentUid()
        val cityCodeList = ConfigManager.forestConfig.ancientTreeCityCodeList

        for (cityCode in cityCodeList) {
            if (!UserManager.canAncientTreeToday(cityCode)) {
                continue
            }

        }
    }

    private suspend fun doProtect(cityCode: String) {
        AncientTreeRpcCall.homePage(cityCode).onSuccess { jsonObject ->
            jsonObject.optString("resultCode") == "SUCCESS"
        }
    }
}