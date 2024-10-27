package org.zipper.ant.forest.xposed.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import org.zipper.ant.forest.xposed.navigation.HomeRoute
import org.zipper.ant.forest.xposed.navigation.TopLevelDestination
import org.zipper.ant.forest.xposed.navigation.homeScreen
import org.zipper.ant.forest.xposed.navigation.logcatScreen
import org.zipper.ant.forest.xposed.navigation.profileScreen
import org.zipper.ant.forest.xposed.ui.dialog.SettingDialog
import org.zipper.ant.forest.xposed.ui.state.MainAppState
import org.zipper.ant.forest.xposed.ui.util.LocalAppViewModel
import org.zipper.ant.forest.xposed.ui.util.LocalMainAppState
import org.zipper.ant.forest.xposed.ui.util.LocalSnackbarHost
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntForestXApp(
    appViewModel: AppViewModel,
    appState: MainAppState
) {
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    val currentTopLevelDestination = appState.currentTopLevelDestination
    val snackBarHostState = remember { SnackbarHostState() }
    if (showSettingsDialog) {
        SettingDialog {
            showSettingsDialog = false
        }
    }

    CompositionLocalProvider(
        LocalAppViewModel provides appViewModel,
        LocalSnackbarHost provides snackBarHostState,
        LocalMainAppState provides appState
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        if (currentTopLevelDestination != null) {
                            Text(stringResource(currentTopLevelDestination.titleRes))
                        }
                    },
                    actions = {
                        Box(modifier = Modifier.padding(end = 16.dp)) {
                            Icon(
                                imageVector = Icons.Filled.Settings, contentDescription = "",
                                modifier = Modifier
                                    .size(32.dp, 32.dp)
                                    .clickable(onClick = {

                                    })
                                    .clickable {
                                        showSettingsDialog = true
                                    }
                            )
                        }
                    }
                )
            },
            bottomBar = { BottomNavigation(appState) },
            snackbarHost = { SnackbarHost(snackBarHostState) },
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                DestinationsNavHost(appState)
            }
        }
    }
}



@Composable
private fun DestinationsNavHost(appState: MainAppState) {
    NavHost(
        navController = appState.navController,
        startDestination = HomeRoute
    ) {
        logcatScreen()
        homeScreen()
        profileScreen()
    }
}

@Composable
private fun BottomNavigation(appState: MainAppState) {
    val currentDestination = appState.currentDestination
    NavigationBar {
        TopLevelDestination.entries.forEach {
            NavigationBarItem(
                selected = currentDestination?.hasRoute(it.route) ?: false,
                onClick = {
                    appState.navigateToTopLevelDestination(it)
                },
                icon = {
                    Icon(
                        imageVector = if (currentDestination?.hasRoute(it.route) == true) it.selectedIcon else it.unselectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = it.titleRes))
                }
            )
        }
    }
}