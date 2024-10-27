package org.zipper.ant.forest.xposed.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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
import org.zipper.ant.forest.xposed.utils.PermissionCompat

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */
class AppViewModel : ViewModel(), KoinComponent {

    private val appProfileRepository: AppProfileRepository by inject<AppProfileRepository>()

    private val androidContext: Context by inject<Context>()

    private val _storagePermissionState: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(PermissionCompat.getPermissionStatus(androidContext))
    }

    val storagePermissionState: StateFlow<Boolean> get() = _storagePermissionState

    val uiState: StateFlow<MainUIState> = appProfileRepository.appProfilePreference
        .map { MainUIState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            initialValue = MainUIState.Loading,
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val appSettingsUiState: StateFlow<SettingsUiState> = appProfileRepository.appProfilePreference
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

    fun onPermissionGranted(result: Boolean) {
        _storagePermissionState.value = result
    }
}