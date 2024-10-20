package org.zipper.ant.forest.xposed.ui.state

import org.zipper.ant.forest.xposed.data.AppSettingsData

sealed class SettingsUiState {
    data object Loading : SettingsUiState()
    data class Success(val settingsData: AppSettingsData) : SettingsUiState()
}