package org.zipper.ant.forest.xposed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.zipper.ant.forest.xposed.data.AppSettingsData
import org.zipper.ant.forest.xposed.enums.AppThemeScheme
import org.zipper.ant.forest.xposed.repostory.AppProfileRepository
import org.zipper.ant.forest.xposed.ui.state.MainUIState
import org.zipper.ant.forest.xposed.ui.state.SettingsUiState

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */
class AppViewModel : ViewModel(), KoinComponent {

    private val appProfileRepository: AppProfileRepository by inject<AppProfileRepository>()

    val uiState: StateFlow<MainUIState> = appProfileRepository.appProfileData
        .map { MainUIState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            initialValue = MainUIState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val appSettingsUiState: StateFlow<SettingsUiState> = appProfileRepository.appProfileData
        .map { SettingsUiState.Success(AppSettingsData(it.useDynamicColor, it.themeScheme)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = SettingsUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            appProfileRepository.updateDynamicColorPreference(useDynamicColor)
        }
    }

    fun updateDarkThemeScheme(darkThemeScheme: AppThemeScheme) {
        viewModelScope.launch {
            appProfileRepository.updateDarkThemeScheme(darkThemeScheme)
        }
    }
}