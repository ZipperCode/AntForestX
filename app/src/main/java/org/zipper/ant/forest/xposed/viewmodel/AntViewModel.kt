package org.zipper.ant.forest.xposed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.zipper.antforestx.data.config.AntBasicConfig
import org.zipper.antforestx.data.config.AntConfig
import org.zipper.antforestx.data.config.AntForestConfig
import org.zipper.antforestx.data.config.AntManorConfig
import org.zipper.antforestx.data.config.AntOtherConfig
import org.zipper.antforestx.data.enums.EnergyWaterEnum
import org.zipper.antforestx.data.enums.RecallChicksType
import org.zipper.antforestx.data.enums.RepatriateType
import org.zipper.antforestx.data.repository.IAntConfigRepository

class AntViewModel : ViewModel(), KoinComponent {

    private val antConfigRepository: IAntConfigRepository by inject<IAntConfigRepository>()

    val antConfigFlow: StateFlow<AntConfig> = antConfigRepository.configFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = antConfigRepository.defaultConfig
    )

    fun switchEnableLogcat(enable: Boolean) {
        antConfigRepository.updateBasic {
            it.copy(enableLogcat = enable)
        }
    }

    fun enableToast(enable: Boolean) {
        antConfigRepository.updateBasic {
            it.copy(showToast = enable)
        }
    }

    fun enableParallel(enable: Boolean) {
        antConfigRepository.updateBasic {
            it.copy(isParallel = enable)
        }
    }

    fun enableNotification(enable: Boolean) {
        antConfigRepository.updateBasic {
            it.copy(showNotification = enable)
        }
    }

    /**
     * 收能量
     */
    fun enableCollectEnergy(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(isCollectEnergy = enable)
    }

    fun enableBatchCollectEnergy(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(isBatchRobEnergy = enable)
    }

    fun collectEnergyInterval(interval: Long) = antConfigRepository.updateForest {
        it.copy(collectInterval = interval)
    }

    fun enableLimitCollect(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(isCollectLimit = enable)
    }

    fun limitCountInMinute(count: Int) = antConfigRepository.updateForest {
        it.copy(limitCountInMinute = count)
    }

    fun enableCollectFriends(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableCollectFriends = enable)
    }

    fun unCollectFriendList(list: List<String>) = antConfigRepository.updateForest {
        it.copy(unCollectFriendList = list)
    }

    fun enableHelpFriendCollect(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(isHelpFriendCollect = enable)
    }

    /**
     * 道具配置
     */
    fun enableUseDoubleProp(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(useDoubleProp = enable)
    }

    fun useDoublePropTime(time: String) = antConfigRepository.updateForest {
        it.copy(useDoublePropTime = time)
    }

    fun useDoublePropLimit(limit: Int) = antConfigRepository.updateForest {
        it.copy(useDoublePropLimit = limit)
    }

    fun enableEnergyShieldProp(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableEnergyShieldProp = enable)
    }

    fun enableSendFriendProp(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableSendFriendProp = enable)
    }

    fun sendFriendPropList(list: List<String>) = antConfigRepository.updateForest {
        it.copy(sendFriendPropList = list)
    }

    fun sendPropFriends(list: List<String>) = antConfigRepository.updateForest {
        it.copy(sendPropFriends = list)
    }

    fun enableExchangeDoubleProp(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableExchangeDoubleProp = enable)
    }

    fun exchangeDoubleLimit(limit: Int) = antConfigRepository.updateForest {
        it.copy(exchangeDoubleLimit = limit)
    }

    fun enableExchangeShieldProp(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableExchangeShieldProp = enable)
    }

    fun exchangeShieldLimit(limit: Int) = antConfigRepository.updateForest {
        it.copy(exchangeShieldLimit = limit)
    }

    /**
     * 任务配置
     */

    fun enableForestTask(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableForestTask = enable)
    }

    fun enableEnergyRain(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(isCollectEnergyRain = enable)
    }

    fun energyRainSendFriends(list: List<String>) = antConfigRepository.updateForest {
        it.copy(energyFriendRainList = list)
    }

    /**
     * 浇水配置
     */
    fun enableWateringFriends(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableWateringFriend = enable)
    }

    fun wateringConfig(wateringEnum: EnergyWaterEnum) = antConfigRepository.updateForest {
        it.copy(wateringEnum = wateringEnum)
    }

    fun waterFriendList(list: List<String>) = antConfigRepository.updateForest {
        it.copy(waterFriendList = list)
    }

    fun enableCooperateWater(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableCooperateWater = enable)
    }

    fun cooperateTreeList(list: List<String>) = antConfigRepository.updateForest {
        it.copy(cooperateTreeList = list)
    }

    fun cooperateWaterLimit(limit: Int) = antConfigRepository.updateForest {
        it.copy(cooperateWaterLimit = limit)
    }

    /**
     * 海洋配置
     */

    fun enableProtectOcean(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableProtectOcean = enable)
    }

    fun enableOceanTask(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableOceanTask = enable)
    }

    fun enableCollectGarbage(enable: Boolean) = antConfigRepository.updateForest {
        it.copy(enableCollectGarbage = enable)
    }

    /**
     * 庄园
     */
    fun enableManor(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enable = enable)
    }

    fun enableRewardFriend(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isRewardFriend = enable)
    }

    fun enableRepatriateChicks(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isRepatriateChicks = enable)
    }

    fun setRepatriateType(type: RepatriateType) = antConfigRepository.updateManor {
        it.copy(repatriateType = type)
    }

    fun setUnRepatriateFriendList(list: List<String>) = antConfigRepository.updateManor {
        it.copy(unRepatriateFriendList = list)
    }


    fun enableRecallChicks(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isRecallChicks = enable)
    }

    fun setRecallChicksType(type: RecallChicksType) = antConfigRepository.updateManor {
        it.copy(recallChicksType = type)
    }

    fun enableCollectPropReward(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isCollectPropReward = enable)
    }

    fun enableChicksKitchen(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableChicksKitchen = enable)
    }

    fun enableUseSpecialFoods(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isUseSpecialFoods = enable)
    }

    fun enableCollectEgg(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableCollectEgg = enable)
    }

    fun enableDonateEgg(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableDonateEgg = enable)
    }

    fun donateEggNumLimit(limit: Int) = antConfigRepository.updateManor {
        it.copy(donateEggNumLimit = limit)
    }

    fun enableAnswerQuestion(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableAnswerQuestion = enable)
    }

    fun enableCollectFeedReward(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isCollectFeedReward = enable)
    }

    fun enableFeedChicks(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableFeedChicks = enable)
    }

    fun enableSpeedCard(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isUseSpeedCard = enable)
    }

    fun enableFeedFriendChicks(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isFeedFriendChicks = enable)
    }

    fun feedFriendChicksList(list: List<String>) = antConfigRepository.updateManor {
        it.copy(feedFriendChicksList = list)
    }

    fun enableNotifyFriendChicks(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isNotifyFriendChicks = enable)
    }

    fun notNotifyFriendChicksList(list: List<String>) = antConfigRepository.updateManor {
        it.copy(notNotifyFriendChicksList = list)
    }

    fun enableCollectWheat(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableCollectWheat = enable)
    }

    fun sendWheatFriendList(list: List<String>) = antConfigRepository.updateManor {
        it.copy(sendWheatFriendList = list)
    }

    fun enableChicksDiary(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableChicksDiary = enable)
    }

    fun enableChicksTask(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableChicksTask = enable)
    }

    fun enableFarm(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(enableFarm = enable)
    }

    fun collectFarmReward(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(collectFarmReward = enable)
    }

    fun farmFertilizeCount(count: Int) = antConfigRepository.updateManor {
        it.copy(farmFertilizeCount = count)
    }

    fun enableCatchChicks(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(isCatchChicks = enable)
    }

    fun enableDoFarmTask(enable: Boolean) = antConfigRepository.updateManor {
        it.copy(doFarmTask = enable)
    }

    /**
     * 其他
     */
    fun enableSign(enable: Boolean) = antConfigRepository.updateOther {
        it.copy(enableSign = enable)
    }

    fun enableIntegralTask(enable: Boolean) = antConfigRepository.updateOther {
        it.copy(enableIntegralTask = enable)
    }

    fun enableCollectIntegral(enable: Boolean) = antConfigRepository.updateOther {
        it.copy(enableCollectIntegral = enable)
    }

    fun enableStep(enable: Boolean) = antConfigRepository.updateOther {
        it.copy(enableStep = enable)
    }

    fun customStepNum(num: Int) = antConfigRepository.updateOther {
        it.copy(customStepNum = num)
    }

    fun enableMerchant(enable: Boolean) = antConfigRepository.updateOther {
        it.copy(enableMerchant = enable)
    }

    fun enableMerchantSign(enable: Boolean) = antConfigRepository.updateOther {
        it.copy(enableMerchantSign = enable)
    }

    fun enableMerchantTask(enable: Boolean) = antConfigRepository.updateOther {
        it.copy(enableMerchantTask = enable)
    }

    private fun IAntConfigRepository.updateBasic(block: (AntBasicConfig) -> AntBasicConfig) {
        viewModelScope.launch {
            updateConfig {
                it.copy(basicConfig = block(it.basicConfig))
            }
        }
    }

    private fun IAntConfigRepository.updateForest(block: (AntForestConfig) -> AntForestConfig) {
        viewModelScope.launch {
            updateConfig {
                it.copy(forestConfig = block(it.forestConfig))
            }
        }
    }

    private fun IAntConfigRepository.updateManor(block: (AntManorConfig) -> AntManorConfig) {
        viewModelScope.launch {
            updateConfig {
                it.copy(manorConfig = block(it.manorConfig))
            }
        }
    }

    private fun IAntConfigRepository.updateOther(block: (AntOtherConfig) -> AntOtherConfig) {
        viewModelScope.launch {
            updateConfig {
                it.copy(otherConfig = block(it.otherConfig))
            }
        }
    }

}