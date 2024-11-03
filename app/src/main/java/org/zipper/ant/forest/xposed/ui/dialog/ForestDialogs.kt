package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.zipper.ant.forest.xposed.R
import org.zipper.ant.forest.xposed.ui.component.CommonCheckBoxItem
import org.zipper.ant.forest.xposed.ui.state.None
import org.zipper.ant.forest.xposed.viewmodel.AntDataViewModel
import org.zipper.ant.forest.xposed.viewmodel.AntViewModel
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel
import org.zipper.antforestx.data.bean.AntForestPropInfo
import org.zipper.antforestx.data.config.AntForestConfig

@Composable
fun DoublePropTimeDialog(
    forestConfig: AntForestConfig,
    appViewModel: AppViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    StringValueInputDialog(
        title = stringResource(R.string.app_dialog_double_prop_time_title),
        forestConfig.useDoublePropTime,
        onDismiss = {
            viewModel.useDoublePropTime(it)
            appViewModel.onAntDialogStateChanged(None)
        })
}

/**
 * 使用双击卡限制
 */
@Composable
fun DoublePropLimitDialog(
    forestConfig: AntForestConfig,
    appViewModel: AppViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    IntValueInputDialog(
        title = stringResource(R.string.app_dialog_double_prop_time_title),
        value = forestConfig.useDoublePropLimit,
        onDismiss = {
            appViewModel.onAntDialogStateChanged(None)
            viewModel.useDoublePropLimit(it)
        })
}

/**
 * 送道具
 */
@Composable
fun ForestSendPropListDialog(
    forestConfig: AntForestConfig,
    appViewModel: AppViewModel = koinViewModel(),
    dataViewModel: AntDataViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    val sendFriendPropList = forestConfig.sendFriendPropList
    val forestPropList by dataViewModel.forestPropList.collectAsStateWithLifecycle()

    val initCheckPropList = remember(sendFriendPropList) {
        mutableStateListOf<AntForestPropInfo>(
            *forestPropList.filter { it.propId in sendFriendPropList }.toTypedArray()
        )
    }

    CustomDialog(
        title = "可送道具列表",
        onDismiss = {
            viewModel.sendFriendPropList(initCheckPropList.map { it.propId })
            appViewModel.onAntDialogStateChanged(None)
        },
        content = {
            Text(text = "选中道具一天只赠送一次")
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(forestPropList.size) { index ->
                    CommonCheckBoxItem(
                        checked = initCheckPropList.contains(forestPropList[index]),
                        text = forestPropList[index].propName,
                        onCheckedChange = { checked ->
                            if (checked) {
                                initCheckPropList.add(forestPropList[index])
                            } else {
                                initCheckPropList.remove(forestPropList[index])
                            }
                        },
                    )
                }
            }
        }
    )
}

/**
 * 送好友道具
 */
@Composable
fun ForestSendPropToFriendDialog(
    forestConfig: AntForestConfig,
    appViewModel: AppViewModel = koinViewModel(),
    dataViewModel: AntDataViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    val allUsers by dataViewModel.alipayUserList.collectAsStateWithLifecycle()
    AlipayUserCheckedDialog(
        userList = allUsers,
        initCheckedUsers = allUsers.filter { it.userId in forestConfig.sendPropFriends },
        onDismiss = { checkedUsers ->
            viewModel.sendPropFriends(checkedUsers.map { it.userId })
            appViewModel.onAntDialogStateChanged(None)
        }
    )
}

@Composable
fun ExchangedDoublePropLimitDialog(
    forestConfig: AntForestConfig,
    appViewModel: AppViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    IntValueInputDialog(
        title = "兑换双击卡限制",
        forestConfig.exchangeDoubleLimit,
        onDismiss = {
            appViewModel.onAntDialogStateChanged(None)
            viewModel.exchangeDoubleLimit(it)
        })
}

@Composable
fun ExchangedShieldPropLimitDialog(
    forestConfig: AntForestConfig,
    appViewModel: AppViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    IntValueInputDialog(
        title = "兑换能量盾限制",
        forestConfig.exchangeShieldLimit,
        onDismiss = {
            appViewModel.onAntDialogStateChanged(None)
            viewModel.exchangeShieldLimit(it)
        })
}