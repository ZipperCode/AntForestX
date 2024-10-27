package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.zipper.ant.forest.xposed.R
import org.zipper.ant.forest.xposed.data.AppSettingsData
import org.zipper.ant.forest.xposed.enums.AppThemeScheme
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel
import org.zipper.ant.forest.xposed.ui.state.SettingsUiState
import org.zipper.ant.forest.xposed.ui.theme.supportsDynamicTheming

@Composable
fun SettingDialog(
    viewModel: AppViewModel = koinViewModel(),
    onDismiss: () -> Unit
) {
    val settingsUiState by viewModel.appSettingsUiState.collectAsStateWithLifecycle()
    SettingDialog(
        settingsUiState = settingsUiState,
        onDismiss = onDismiss,
        onChangeDynamicColorPreference = viewModel::updateDynamicColorPreference,
        onChangeDarkThemeScheme = viewModel::updateDarkThemeScheme
    )
}


@Composable
private fun SettingDialog(
    settingsUiState: SettingsUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onDismiss: () -> Unit,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeScheme: (darkThemeConfig: AppThemeScheme) -> Unit,
) {
    val configuration = LocalConfiguration.current
    AlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 80.dp)
            .heightIn(max = configuration.screenHeightDp.dp - 160.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Text(
                text = stringResource(R.string.app_dialog_setting_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                when (settingsUiState) {
                    is SettingsUiState.Loading -> {
                        Text(
                            text = stringResource(R.string.app_loading_content),
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    }

                    is SettingsUiState.Success -> {
                        SettingContent(
                            settingsData = settingsUiState.settingsData,
                            supportDynamicColor = supportDynamicColor,
                            onChangeDynamicColorPreference = onChangeDynamicColorPreference,
                            onChangeDarkThemeScheme = onChangeDarkThemeScheme
                        )

                    }
                }
                HorizontalDivider()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.app_dialog_confirm_text),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun ColumnScope.SettingContent(
    settingsData: AppSettingsData,
    supportDynamicColor: Boolean,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeScheme: (darkThemeConfig: AppThemeScheme) -> Unit,
) {
    AnimatedVisibility(visible = supportDynamicColor) {
        Column() {
            SettingsDialogSectionTitle(text = stringResource(R.string.app_dialog_setting_dynamic_color))
            Column(Modifier.selectableGroup()) {
                SettingsDialogRadioItem("是", settingsData.useDynamicColor, onClick = {
                    onChangeDynamicColorPreference(true)
                })
                SettingsDialogRadioItem("否", !settingsData.useDynamicColor, onClick = {
                    onChangeDynamicColorPreference(false)
                })
            }
        }
    }
    SettingsDialogSectionTitle(text = stringResource(R.string.app_dialog_setting_theme_scheme_title))
    Column(Modifier.selectableGroup()) {
        SettingsDialogRadioItem(
            text = stringResource(R.string.app_dialog_setting_theme_scheme_system),
            selected = settingsData.appThemeScheme == AppThemeScheme.System,
            onClick = { onChangeDarkThemeScheme(AppThemeScheme.System) },
        )
        SettingsDialogRadioItem(
            text = stringResource(R.string.app_dialog_setting_theme_scheme_light),
            selected = settingsData.appThemeScheme == AppThemeScheme.Light,
            onClick = { onChangeDarkThemeScheme(AppThemeScheme.Light) },
        )
        SettingsDialogRadioItem(
            text = stringResource(R.string.app_dialog_setting_theme_scheme_dark),
            selected = settingsData.appThemeScheme == AppThemeScheme.Dark,
            onClick = { onChangeDarkThemeScheme(AppThemeScheme.Dark) },
        )
    }
}

@Composable
private fun SettingsDialogRadioItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(selected, role = Role.RadioButton, onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsDialogPreview() {
    val settingsData = AppSettingsData(
        useDynamicColor = true,
        appThemeScheme = AppThemeScheme.System
    )
    SettingDialog(
        settingsUiState = SettingsUiState.Success(settingsData),
        onDismiss = {},
        onChangeDynamicColorPreference = {},
        onChangeDarkThemeScheme = {}
    )
}