package org.xposed.antforestx.core.tasks

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.xposed.antforestx.core.ant.AntCooperateRpcCall
import org.xposed.antforestx.core.bean.CooperateInfoBean
import org.xposed.antforestx.core.manager.ConfigManager
import org.xposed.antforestx.core.manager.UserManager
import org.xposed.antforestx.core.util.FileDataProvider
import org.xposed.antforestx.core.util.isSuccess
import org.xposed.antforestx.core.util.log.waterCooperate
import timber.log.Timber

/**
 * 合种树，浇水
 */
class AntCooperateTask {

    private val logger: Timber.Tree get() = Timber.waterCooperate()

    suspend fun start() = withContext(Dispatchers.IO + CoroutineName("AntCooperate")) {
        logger.i("开始执行浇水任务")
        if (!ConfigManager.forestConfig.isEnableCooperateWater) {
            logger.w("浇水功能未开启")
            return@withContext
        }
        runCatching {
            queryCooperatePlant()
        }
    }

    private suspend fun queryCooperatePlant() = kotlin.runCatching {
        val userId = UserManager.waitGetCurrentUid()
        // 合种数信息
        val cooperateInfo = ArrayList<CooperateInfoBean>()
        AntCooperateRpcCall.queryUserCooperatePlantList().onSuccess { jsonObject ->
            if (jsonObject.isSuccess()) {
                logger.w("合种数查询失败")
                return@onSuccess
            }
            val userCurrentEnergy = jsonObject.optInt("userCurrentEnergy")
            val cooperatePlants = jsonObject.optJSONArray("cooperatePlants") ?: return@onSuccess
            val cooperateList = UserManager.antRecord.forestDayRecord.cooperateWaterList.toMutableSet()

            for (i in 0 until cooperatePlants.length()) {
                val plant = cooperatePlants.getJSONObject(i)
                val cooperationId = plant.optString("cooperationId") ?: continue
                val name = plant.optString("name", "")
                val waterDayLimit = plant.optInt("waterDayLimit")
                cooperateInfo.add(CooperateInfoBean(cooperationId, name))

                if (cooperateList.contains(cooperationId)) {
                    // 已经浇过水了
                    continue
                }
                // 不存在配置，不处理
                ConfigManager.forestConfig.cooperateWaterList.firstOrNull { it == cooperationId } ?: continue
                var waterLimit = ConfigManager.forestConfig.cooperateWaterLimit

                if (waterLimit > waterDayLimit) {
                    waterLimit = waterDayLimit
                }
                if (waterLimit > userCurrentEnergy) {
                    waterLimit = userCurrentEnergy
                }
                if (waterLimit > 0) {
                    cooperateWater(userId, cooperationId, waterLimit, name)
                }
            }
            if (cooperateInfo.isNotEmpty()) {
                FileDataProvider.saveCooperate(cooperateInfo)
            }
        }.onFailure {
            logger.w("合种数量查询异常 %s", it.message)
            logger.e(it)
        }
    }

    private suspend fun validPlant(platJasonObject: JSONObject): Result<JSONObject> {
        if (platJasonObject.has("name")) {
            return Result.success(platJasonObject)
        }
        val cooperationId: String = platJasonObject.getString("cooperationId")
        return AntCooperateRpcCall.queryCooperatePlant(cooperationId)
    }

    private suspend fun cooperateWater(userId: String, cooperationId: String, num: Int, name: String) {
        logger.i("【合种】开始浇水 [%s] 水滴:%s", name, num)
        AntCooperateRpcCall.cooperateWater(userId, cooperationId, num).onSuccess { jsonObject ->
            if (jsonObject.isSuccess()) {
                logger.i("浇水成功，合种数:%s[%s] 结果: %s", cooperationId, name, jsonObject.optString("barrageText"))
            } else {
                logger.w("浇水失败，合种数:%s[%s]", cooperationId, name)
                logger.d("失败 => %s", jsonObject)
            }
        }.onFailure {
            logger.e(it, "浇水异常 ")
        }
    }

}