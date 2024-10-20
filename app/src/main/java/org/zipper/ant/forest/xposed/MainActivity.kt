package org.zipper.ant.forest.xposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.zipper.ant.forest.xposed.enums.AppThemeScheme
import org.zipper.ant.forest.xposed.navigation.HomeRoute
import org.zipper.ant.forest.xposed.navigation.TopLevelDestination
import org.zipper.ant.forest.xposed.navigation.homeScreen
import org.zipper.ant.forest.xposed.navigation.logcatScreen
import org.zipper.ant.forest.xposed.navigation.profileScreen
import org.zipper.ant.forest.xposed.ui.AntForestXApp
import org.zipper.ant.forest.xposed.ui.AppViewModel
import org.zipper.ant.forest.xposed.ui.dialog.SettingDialog
import org.zipper.ant.forest.xposed.ui.state.MainAppState
import org.zipper.ant.forest.xposed.ui.state.MainUIState
import org.zipper.ant.forest.xposed.ui.state.rememberMainAppState
import org.zipper.ant.forest.xposed.ui.theme.AntForestXTheme
import org.zipper.ant.forest.xposed.ui.util.LocalAppViewModel
import org.zipper.ant.forest.xposed.ui.util.LocalMainAppState
import org.zipper.ant.forest.xposed.ui.util.LocalSnackbarHost


class MainActivity : ComponentActivity(), KoinComponent {

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