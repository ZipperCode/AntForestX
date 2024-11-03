package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import org.zipper.ant.forest.xposed.ui.state.AlipayCustomStep
import org.zipper.ant.forest.xposed.ui.state.AntDialogUiState
import org.zipper.ant.forest.xposed.ui.state.None
import org.zipper.ant.forest.xposed.viewmodel.AntViewModel
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel
import org.zipper.antforestx.data.config.AntOtherConfig


@Composable
fun AlipayConfigDialogs(
    otherConfig: AntOtherConfig,
    antDialogState: AntDialogUiState,
    appViewModel: AppViewModel = koinViewModel(),
    viewModel: AntViewModel = koinViewModel()
) {
    when (antDialogState) {
        AlipayCustomStep -> CustomStepDialog(otherConfig, appViewModel, viewModel)

        else -> Unit
    }

}

@Composable
private fun CustomStepDialog(
    otherConfig: AntOtherConfig,
    appViewModel: AppViewModel,
    viewModel: AntViewModel
) {
    IntValueInputDialog(
        title = "自定义步数",
        value = otherConfig.customStepNum,
        onDismiss = {
            viewModel.customStepNum(it)
            appViewModel.onAntDialogStateChanged(None)
        })
}