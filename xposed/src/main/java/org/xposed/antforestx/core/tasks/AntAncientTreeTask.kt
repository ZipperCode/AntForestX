package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.xposed.antforestx.core.ant.AncientTreeRpcCall
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.log.ancient
import org.xposed.antforestx.core.util.log.enableForest
import timber.log.Timber

/**
 * 保护古树
 * @author  zhangzhipeng
 * @date    2024/9/4
 */
class AntAncientTreeTask {

    private val logger get() = Timber.ancient()
    suspend fun start() = withContext(Dispatchers.IO + CoroutineName("AntAncientTree")) {
        Timber.enableForest()
        if (!ConfigManager.enableAncientTree) {
            Timber.ancient().i("保护古树未开启")
            return@withContext
        }
        if (!UserManager.validateUser()) {
            logger.i("获取用户失败，不执行保护古数")
            return@withContext
        }
        runCatching {
            val cityCodeList = ConfigManager.forestConfig.ancientTreeCityCodeList
            Timber.ancient().i("开始保护古树，城市：%s", cityCodeList)
            for (cityCode in cityCodeList) {
                if (!UserManager.canAncientTreeToday(cityCode)) {
                    continue
                }
                doProtect(cityCode)
                delay(500)
            }
        }.onFailure {
            Timber.ancient().w(it, "保护古树失败，发生异常")
        }
    }

    private suspend fun doProtect(cityCode: String) {
        AncientTreeRpcCall.homePage(cityCode).onSuccess { jsonObject ->
            if (jsonObject.optString("resultCode") != "SUCCESS") {
                Timber.ancient().e("homePage resultCode != SUCCESS data = %s", jsonObject)
                return
            }
            val data = jsonObject.getJSONObject("data")
            if (!data.has("districtBriefInfoList")) {
                return
            }
            val districtBriefInfoList = data.optJSONArray("districtBriefInfoList") ?: return
            for (i in 0 until districtBriefInfoList.length()) {
                val districtBriefInfo = districtBriefInfoList.getJSONObject(i)
                if (districtBriefInfo.optInt("userCanProtectTreeNum", 0) < 1) {
                    continue
                }
                val districtInfo = districtBriefInfo.optJSONObject("districtInfo")
                districtInfo?.getString("districtCode")?.also {
                    Timber.ancient().i("开始保护古数，城市：%s，古数：%s", cityCode, it)
                    districtDetail(it)
                }
            }
        }
    }

    private suspend fun districtDetail(districtCode: String) = runCatching {
        AncientTreeRpcCall.districtDetail(districtCode).onSuccess { jsonObject ->
            if (jsonObject.optString("resultCode") != "SUCCESS") {
                Timber.e("districtDetail resultCode != SUCCESS data = %s", jsonObject)
                return@runCatching
            }
            val data = jsonObject.getJSONObject("data")
            if (!data.has("ancientTreeList")) {
                return@runCatching
            }
            val districtInfo = data.getJSONObject("districtInfo")
            val cityCode = districtInfo.getString("cityCode")
            val cityName = districtInfo.getString("cityName")
            val districtName = districtInfo.getString("districtName")
            val ancientTreeList = data.getJSONArray("ancientTreeList")
            for (i in 0 until ancientTreeList.length()) {
                val ancientTree = ancientTreeList.getJSONObject(i)
                if (ancientTree.getBoolean("hasProtected")) {
                    continue
                }
                val ancientTreeControlInfo: JSONObject = ancientTree.getJSONObject("ancientTreeControlInfo")
                val quota = ancientTreeControlInfo.optInt("quota", 0)
                val useQuota = ancientTreeControlInfo.optInt("useQuota", 0)
                if (quota <= useQuota) {
                    continue
                }
                val itemId = ancientTree.getString("projectId")
                if (!projectDetail(itemId, cityCode)) {
                    break
                }
            }
        }
    }

    private suspend fun projectDetail(ancientTreeProjectId: String, cityCode: String): Boolean {
        AncientTreeRpcCall.projectDetail(ancientTreeProjectId, cityCode).onSuccess {
            runCatching {
                if (it.optString("resultCode") != "SUCCESS") {
                    Timber.ancient().e("projectDetail resultCode != SUCCESS data = %s", it)
                    return true
                }
                val data = it.getJSONObject("data")
                if (!data.optBoolean("canProtect")) {
                    return true
                }
                val currentEnergy = data.getInt("currentEnergy")
                val ancientTree = data.getJSONObject("ancientTree")
                val activityId: String = ancientTree.getString("activityId")
                val projectId: String = ancientTree.getString("projectId")
                val ancientTreeInfo: JSONObject = ancientTree.getJSONObject("ancientTreeInfo")
                val name = ancientTreeInfo.getString("name")
                val age = ancientTreeInfo.getInt("age")
                val protectExpense = ancientTreeInfo.getInt("protectExpense")
                val cityCode1 = ancientTreeInfo.getString("cityCode")
                if (currentEnergy < protectExpense) {
                    return false
                }
                Timber.ancient().i("开始保护古数，城市：%s，古数：%s，年龄：%s，保护花费：%s", cityCode1, name, age, protectExpense)
                delay(500)

                AncientTreeRpcCall.protect(activityId, ancientTreeProjectId, cityCode).onSuccess {
                    if (it.optString("resultCode") != "SUCCESS") {
                        Timber.ancient().e("protect resultCode != SUCCESS data = %s", it)
                    } else {
                        Timber.ancient().i("保护古数成功，项目ID：%s", projectId)
                    }
                }
            }
        }
        delay(3000)
        return true
    }

}