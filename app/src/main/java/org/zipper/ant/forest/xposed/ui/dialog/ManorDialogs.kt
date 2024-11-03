package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.zipper.ant.forest.xposed.ui.state.DonateEggDialogState
import org.zipper.ant.forest.xposed.ui.state.ManorFarmFertilizeDialogState
import org.zipper.ant.forest.xposed.ui.state.ManorFeedFriendDialogState
import org.zipper.ant.forest.xposed.ui.state.ManorRecallTypeDialogState
import org.zipper.ant.forest.xposed.ui.state.ManorRepatriateDialogState
import org.zipper.ant.forest.xposed.ui.state.ManorUnRepatriateFriendDialogState
import org.zipper.ant.forest.xposed.ui.state.None
import org.zipper.ant.forest.xposed.viewmodel.AntDataViewModel
import org.zipper.ant.forest.xposed.viewmodel.AntViewModel
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel
import org.zipper.antforestx.data.config.AntManorConfig
import org.zipper.antforestx.data.enums.RecallChicksType
import org.zipper.antforestx.data.enums.RepatriateType

@Composable
fun ManorConfigDialogs(
    manorConfig: AntManorConfig,
    appViewModel: AppViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    val dialogState by appViewModel.antDialogState.collectAsStateWithLifecycle()
    when (dialogState) {
        ManorRepatriateDialogState -> ManorRepatriateDialog(manorConfig, appViewModel, viewModel)
        ManorUnRepatriateFriendDialogState -> ManorUnRepatriateFriendDialog(
            manorConfig, viewModel, appViewModel
        )

        ManorRecallTypeDialogState -> ManorRecallTypeDialog(
            manorConfig, viewModel, appViewModel
        )

        DonateEggDialogState -> DonateEggDialog(
            manorConfig, viewModel, appViewModel
        )

        ManorFeedFriendDialogState -> ManorFeedFriendDialog(
            manorConfig, viewModel, appViewModel
        )

        ManorFarmFertilizeDialogState -> ManorFarmFertilizeDialog(
            manorConfig, viewModel, appViewModel
        )

        else -> Unit
    }
}

/**
 * 遣返小鸡类型
 */
@Composable
fun ManorRepatriateDialog(
    manorConfig: AntManorConfig,
    appViewModel: AppViewModel,
    viewModel: AntViewModel
) {

    var repatriateType by remember {
        mutableStateOf(manorConfig.repatriateType)
    }

    CustomDialog(
        title = "遣返小鸡类型",
        onDismiss = {
            appViewModel.onAntDialogStateChanged(None)
            viewModel.setRepatriateType(repatriateType)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            RepatriateType.entries.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = item == repatriateType, onClick = {
                        repatriateType = item
                    })
                    Text(text = item.title)
                }
            }
        }
    }
}

/**
 * 不遣返好友列表
 */
@Composable
fun ManorUnRepatriateFriendDialog(
    manorConfig: AntManorConfig,
    viewModel: AntViewModel,
    appViewModel: AppViewModel,
    dataViewModel: AntDataViewModel = koinViewModel()
) {
    val allUsers by dataViewModel.alipayUserList.collectAsStateWithLifecycle()

    AlipayUserCheckedDialog(
        userList = allUsers,
        initCheckedUsers = allUsers.filter { manorConfig.unRepatriateFriendList.contains(it.userId) },
        onDismiss = { checkedUsers ->
            viewModel.setUnRepatriateFriendList(checkedUsers.map { it.userId })
            appViewModel.onAntDialogStateChanged(None)
        })
}

@Composable
fun ManorRecallTypeDialog(
    manorConfig: AntManorConfig,
    viewModel: AntViewModel,
    appViewModel: AppViewModel
) {
    var recallType by remember {
        mutableStateOf(manorConfig.recallChicksType)
    }
    CustomDialog(
        title = "遣返小鸡类型",
        onDismiss = {
            appViewModel.onAntDialogStateChanged(None)
            viewModel.setRecallChicksType(recallType)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            RecallChicksType.entries.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = item == recallType, onClick = {
                        recallType = item
                    })
                    Text(text = item.desc)
                }
            }
        }
    }
}

@Composable
private fun DonateEggDialog(
    manorConfig: AntManorConfig,
    viewModel: AntViewModel,
    appViewModel: AppViewModel
) {

    IntValueInputDialog(title = "捐蛋数量",
        value = manorConfig.donateEggNumLimit,
        onDismiss = {
            appViewModel.onAntDialogStateChanged(None)
            viewModel.donateEggNumLimit(it)
        })
}

@Composable
fun ManorFeedFriendDialog(
    manorConfig: AntManorConfig,
    viewModel: AntViewModel,
    appViewModel: AppViewModel,
    dataViewModel: AntDataViewModel = koinViewModel()
) {
    val allUsers by dataViewModel.alipayUserList.collectAsStateWithLifecycle()

    AlipayUserCheckedDialog(
        userList = allUsers,
        initCheckedUsers = allUsers.filter { manorConfig.feedFriendChicksList.contains(it.userId) },
        onDismiss = { checkedUsers ->
            viewModel.feedFriendChicksList(checkedUsers.map { it.userId })
            appViewModel.onAntDialogStateChanged(None)
        })
}

@Composable
fun ManorFarmFertilizeDialog(
    manorConfig: AntManorConfig,
    viewModel: AntViewModel,
    appViewModel: AppViewModel
) {
    IntValueInputDialog(title = "施肥次数",
        value = manorConfig.farmFertilizeCount,
        onDismiss = {
            appViewModel.onAntDialogStateChanged(None)
            viewModel.farmFertilizeCount(it)
        })
}