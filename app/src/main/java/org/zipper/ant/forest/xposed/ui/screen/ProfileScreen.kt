package org.zipper.ant.forest.xposed.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.zipper.ant.forest.xposed.R
import org.zipper.ant.forest.xposed.ui.dialog.IntValueInputDialog
import org.zipper.ant.forest.xposed.utils.PermissionCompat
import org.zipper.ant.forest.xposed.viewmodel.AntViewModel
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel
import org.zipper.antforestx.data.config.AntBasicConfig
import org.zipper.antforestx.data.config.AntForestConfig
import org.zipper.antforestx.data.config.AntManorConfig
import org.zipper.antforestx.data.config.AntOtherConfig

@Composable
fun ProfileScreen(
    mainViewModel: AppViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val permissionState by mainViewModel.storagePermissionState.collectAsStateWithLifecycle()
    if (permissionState) {
        ProfileContent(pages = ProfilePages.entries)
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "请授予存储权限")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                PermissionCompat.requestStoragePermission(context as FragmentActivity, mainViewModel::onPermissionGranted)
            }) {
                Text(text = "去授权")
            }
        }
    }

}

@Composable
fun ProfileContent(
    pages: List<ProfilePages>,
    viewModel: AntViewModel = koinViewModel()
) {
    val antConfig by viewModel.antConfigFlow.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState {
        pages.size
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage
        ) {
            pages.forEachIndexed { index, profilePages ->
                Tab(
                    text = { Text(profilePages.title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }
        HorizontalPager(state = pagerState) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (pages[page]) {
                    ProfilePages.Basic -> {
                        ProfileBasicPageContent(antConfig.basicConfig)
                    }

                    ProfilePages.Forest -> {
                        ProfileForestPageContent(antConfig.forestConfig)
                    }

                    ProfilePages.Manor -> {
                        ProfileManorPageContent(antConfig.manorConfig)
                    }

                    ProfilePages.Other -> {
                        ProfileOtherPageContent(antConfig.otherConfig)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileBasicPageContent(
    basicConfig: AntBasicConfig,
    viewModel: AntViewModel = koinViewModel()
) {
    ProfileGroupCard(
        title = "插件配置"
    ) {
        ProfileSwitchRow("是否记录日志", basicConfig.enableLogcat, viewModel::switchEnableLogcat)
        ProfileSwitchRow("Toast提示", basicConfig.showToast, viewModel::enableToast)
        ProfileSwitchRow("任务并发", basicConfig.isParallel, viewModel::enableParallel)
        ProfileSwitchRow("状态栏禁删", basicConfig.showNotification, viewModel::enableNotification)
    }

}

@Composable
fun ProfileForestPageContent(
    forestConfig: AntForestConfig,
    viewModel: AntViewModel = koinViewModel()
) {
    var showCollectIntervalDialog by remember {
        mutableStateOf(false)
    }
    if (showCollectIntervalDialog) {
        IntValueInputDialog(
            title = stringResource(R.string.app_dialog_collect_interval_title),
            forestConfig.collectInterval.toInt(),
            onDismiss = {
                showCollectIntervalDialog = false
            },
            onValueChanged = {
                viewModel.collectEnergyInterval(it.toLong())
            })
    }

    var showCollectLimitDialog by remember {
        mutableStateOf(false)
    }
    if (showCollectLimitDialog) {
        IntValueInputDialog(
            title = stringResource(R.string.app_dialog_collect_limit_title),
            forestConfig.limitCountInMinute,
            onDismiss = {
                showCollectLimitDialog = false
            },
            onValueChanged = {
                viewModel.limitCountInMinute(it)
            })
    }

    ProfileGroupCard(title = "收能量") {
        ProfileSwitchRow(title = "收集能量", checked = forestConfig.isCollectEnergy, viewModel::enableCollectEnergy)

        ProfileSwitchRow(title = "一键收取", checked = forestConfig.isBatchRobEnergy, viewModel::enableBatchCollectEnergy)

        ProfileSettingRow(title = "收能量间隔: ${forestConfig.collectInterval}ms/次",
            modifier = Modifier.clickable { showCollectIntervalDialog = true })

        ProfileSwitchRow(title = "收取限制", checked = forestConfig.isCollectLimit, viewModel::enableLimitCollect)

        if (forestConfig.isCollectLimit) {
            ProfileSettingRow(title = "收能量限制: ${forestConfig.limitCountInMinute}/分钟",
                modifier = Modifier.clickable {
                    showCollectLimitDialog = true
                })
        }

        ProfileSwitchRow(title = "收好友能量", checked = forestConfig.enableCollectFriends, viewModel::enableCollectFriends)
        if (forestConfig.enableCollectFriends) {
            ProfileSettingRow(title = "不收好友列表", modifier = Modifier.clickable {

            })
            ProfileSwitchRow(title = "帮助好友收取", checked = forestConfig.isHelpFriendCollect, viewModel::enableHelpFriendCollect)
        }
    }

    ProfileGroupCard(title = "使用道具配置") {
        ProfileSwitchRow(title = "使用双击卡", checked = forestConfig.useDoubleProp, viewModel::enableUseDoubleProp)
        if (forestConfig.useDoubleProp) {
            ProfileSettingRow(title = "双击卡使用时段: ${forestConfig.useDoublePropTime}")
            ProfileSettingRow(title = "双击卡使用限制: ${forestConfig.useDoublePropLimit}个/天")
        }

        ProfileSwitchRow(title = "使用能量盾", checked = forestConfig.enableEnergyShieldProp, viewModel::enableEnergyShieldProp)
        ProfileSwitchRow(title = "赠送好友道具", checked = forestConfig.enableSendFriendProp, viewModel::enableSendFriendProp)
        if (forestConfig.enableSendFriendProp) {
            ProfileSettingRow(title = "赠送道具列表")
            ProfileSettingRow(title = "赠送好友列表")
        }


        ProfileSwitchRow(title = "兑换双击卡", checked = forestConfig.enableExchangeDoubleProp, viewModel::enableExchangeDoubleProp)
        if (forestConfig.enableExchangeDoubleProp) {
            ProfileSettingRow(title = "兑换数量")
        }
        ProfileSwitchRow(title = "兑换能量盾", checked = forestConfig.enableExchangeShieldProp, viewModel::enableExchangeShieldProp)
        if (forestConfig.enableExchangeShieldProp) {
            ProfileSettingRow(title = "兑换能量盾数量")
        }

    }
    ProfileGroupCard(title = "做任务配置") {
        ProfileSwitchRow(title = "开启森林任务", checked = forestConfig.enableForestTask, viewModel::enableForestTask)

        ProfileSwitchRow(title = "能量雨", checked = forestConfig.isCollectEnergyRain, viewModel::enableEnergyRain)
        if (forestConfig.isCollectEnergyRain) {
            ProfileSettingRow(title = "送能量雨好友列表")
        }
    }

    ProfileGroupCard(title = "浇水配置") {
        ProfileSwitchRow(title = "好友浇水", checked = forestConfig.enableWateringFriend, viewModel::enableWateringFriends)
        if (forestConfig.enableWateringFriend) {
            ProfileSettingRow(title = "浇水配置")
            ProfileSettingRow(title = "浇水好友列表")
        }

        ProfileSwitchRow(title = "合种浇水", checked = forestConfig.enableCooperateWater, viewModel::enableCooperateWater)
        if (forestConfig.enableCooperateWater) {
            ProfileSettingRow(title = "参与浇水的树")
            ProfileSettingRow(title = "每次合种浇水数量")
        }
    }

    ProfileGroupCard(title = "海洋配置") {
        ProfileSwitchRow(title = "开启海洋", checked = forestConfig.enableProtectOcean, viewModel::enableProtectOcean)
        if (forestConfig.enableProtectOcean) {
            ProfileSwitchRow(title = "做任务", checked = forestConfig.enableOceanTask, viewModel::enableOceanTask)
            ProfileSwitchRow(title = "捡垃圾", checked = forestConfig.enableCollectGarbage, viewModel::enableCollectGarbage)
        }
    }

}

@Composable
fun ProfileManorPageContent(
    manorConfig: AntManorConfig,
    viewModel: AntViewModel = koinViewModel()
) {
    ProfileGroupCard(title = "蚂蚁庄园配置") {
        ProfileSwitchRow(title = "开启庄园", checked = manorConfig.enable, viewModel::enableManor)
        if (!manorConfig.enable) {
            return@ProfileGroupCard
        }

        ProfileSwitchRow(title = "打赏好友奖励", checked = manorConfig.isRewardFriend, viewModel::enableRewardFriend)

        ProfileSwitchRow(title = "可遣返小鸡", checked = manorConfig.isRepatriateChicks, viewModel::enableRepatriateChicks)
        if (manorConfig.isRepatriateChicks) {
            ProfileSettingRow(title = "遣返小鸡类型")
            ProfileSettingRow(title = "不遣返好友列表")
        }
        ProfileSwitchRow(title = "召回小鸡", checked = manorConfig.isRecallChicks, viewModel::enableRecallChicks)
        if (manorConfig.isRecallChicks) {
            ProfileSettingRow(title = "召回小鸡类型")
        }
        ProfileSwitchRow(title = "收集道具奖励", checked = manorConfig.isCollectPropReward, viewModel::enableCollectPropReward)

        ProfileSwitchRow(title = "小鸡厨房", checked = manorConfig.enableChicksKitchen, viewModel::enableChicksKitchen)
        ProfileSwitchRow(title = "使用特殊食物", checked = manorConfig.isUseSpecialFoods, viewModel::enableUseSpecialFoods)
        ProfileSwitchRow(title = "收取爱心鸡蛋", checked = manorConfig.enableCollectEgg, viewModel::enableCollectEgg)
        ProfileSwitchRow(title = "捐蛋", checked = manorConfig.enableDonateEgg, viewModel::enableDonateEgg)
        if (manorConfig.enableDonateEgg) {
            ProfileSettingRow(title = "捐蛋数量")
        }

        ProfileSwitchRow(title = "喂鸡", checked = manorConfig.enableAnswerQuestion, viewModel::enableAnswerQuestion)
        ProfileSwitchRow(title = "使用加速卡", checked = manorConfig.isUseSpeedCard, viewModel::enableSpeedCard)
        ProfileSwitchRow(title = "帮助好友喂鸡", checked = manorConfig.isFeedFriendChicks, viewModel::enableFeedFriendChicks)
        if (manorConfig.isFeedFriendChicks) {
            ProfileSettingRow(title = "帮喂鸡好友列表")
        }
        ProfileSwitchRow(title = "送麦子", checked = manorConfig.enableCollectWheat, viewModel::enableCollectWheat)
        if (manorConfig.enableCollectWheat) {
            ProfileSettingRow(title = "送麦子好友列表")
        }

        ProfileSwitchRow(title = "小鸡日记", checked = manorConfig.enableChicksDiary, viewModel::enableChicksDiary)
    }
    ProfileGroupCard(title = "做任务配置") {
        ProfileSwitchRow(title = "做任务", checked = manorConfig.enableChicksTask, viewModel::enableChicksTask)

        ProfileSwitchRow(title = "庄园小课堂", checked = true) {

        }
    }
    ProfileGroupCard(title = "农场配置") {
        ProfileSwitchRow(title = "开启农场", checked = manorConfig.enableFarm, viewModel::enableFarm)
        ProfileSwitchRow(title = "收取奖励", checked = manorConfig.collectFarmReward, viewModel::collectFarmReward)
        ProfileSettingRow(title = "施肥次数")
        ProfileSwitchRow(title = "捉鸡", checked = manorConfig.isCatchChicks, viewModel::enableCatchChicks)
        ProfileSwitchRow(title = "做农场任务", checked = manorConfig.doFarmTask, viewModel::enableDoFarmTask)
    }
}


@Composable
fun ProfileOtherPageContent(
    otherConfig: AntOtherConfig,
    viewModel: AntViewModel = koinViewModel()
) {
    ProfileGroupCard(title = "会员中心") {
        ProfileSwitchRow(title = "会员签到", checked = otherConfig.enableSign, viewModel::enableSign)

        ProfileSwitchRow(title = "执行积分任务", checked = otherConfig.enableIntegralTask, viewModel::enableIntegralTask)
        ProfileSwitchRow(title = "收积分", checked = otherConfig.enableCollectIntegral, viewModel::enableCollectIntegral)
    }
    ProfileGroupCard(title = "步数") {
        ProfileSwitchRow(title = "步数修改", checked = otherConfig.enableStep, viewModel::enableStep)
        if (otherConfig.enableStep) {
            ProfileSettingRow(title = "自定义步数")
        }
    }
    ProfileGroupCard(title = "商家服务") {
        ProfileSwitchRow(title = "签到", checked = otherConfig.enableMerchant, viewModel::enableMerchant)
        ProfileSwitchRow(title = "签到", checked = otherConfig.enableMerchantSign, viewModel::enableMerchantSign)
        ProfileSwitchRow(title = "做任务", checked = otherConfig.enableMerchantTask, viewModel::enableMerchantTask)
    }
}


@Composable
fun ProfileSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(24.dp))
        Switch(
            checked = checked, onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun ProfileSettingRow(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = ">")
    }
}

@Composable
fun ProfileGroupCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            if (!title.isNullOrEmpty()) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            content()
        }
    }
}

@Preview
@Composable
fun ProfileSwitchRowPreview() {
    ProfileSwitchRow("", true) {

    }
}

enum class ProfilePages(
    val title: String
) {
    Basic("基础"),
    Forest("森林"),
    Manor("庄园"),
    Other("其他")
}
