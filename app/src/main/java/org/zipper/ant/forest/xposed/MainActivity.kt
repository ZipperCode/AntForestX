package org.zipper.ant.forest.xposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.zipper.ant.forest.xposed.enums.AppThemeScheme
import org.zipper.ant.forest.xposed.ui.AntForestXApp
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel
import org.zipper.ant.forest.xposed.ui.state.MainUIState
import org.zipper.ant.forest.xposed.ui.state.rememberMainAppState
import org.zipper.ant.forest.xposed.ui.theme.AntForestXTheme
import org.zipper.ant.forest.xposed.utils.PermissionCompat


class MainActivity : FragmentActivity(), KoinComponent {

    private val appViewModel: AppViewModel by viewModel<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var uiState: MainUIState by mutableStateOf(MainUIState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appViewModel.uiState.onEach {
                    uiState = it
                }.collect()
            }
        }
        setContent {
            val darkTheme = shouldUseDarkTheme(uiState)
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {}
            }

            val appState = rememberMainAppState()

            AntForestXTheme(
                darkTheme = darkTheme,
                dynamicColor = shouldDisableDynamicTheming(uiState)
            ) {
                AntForestXApp(appViewModel, appState)
            }
        }
    }
}


@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainUIState,
): Boolean = when (uiState) {
    MainUIState.Loading -> false
    is MainUIState.Success -> !uiState.profileData.useDynamicColor
}


@Composable
private fun shouldUseDarkTheme(
    uiState: MainUIState,
): Boolean = when (uiState) {
    MainUIState.Loading -> isSystemInDarkTheme()
    is MainUIState.Success -> when (uiState.profileData.themeScheme) {
        AppThemeScheme.System -> isSystemInDarkTheme()
        AppThemeScheme.Light -> false
        AppThemeScheme.Dark -> true
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)