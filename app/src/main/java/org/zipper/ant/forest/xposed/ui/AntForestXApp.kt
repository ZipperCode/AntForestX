package org.zipper.ant.forest.xposed.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.zipper.ant.forest.xposed.navigation.MainRoute
import org.zipper.ant.forest.xposed.navigation.mainScreen
import org.zipper.ant.forest.xposed.navigation.webViewScreen
import org.zipper.ant.forest.xposed.ui.util.LocalAppViewModel
import org.zipper.ant.forest.xposed.ui.util.LocalRootNavController
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel

@Composable
fun AntForestXApp(
    appViewModel: AppViewModel,
) {

    val rootNavController = rememberNavController()

    CompositionLocalProvider(
        LocalAppViewModel provides appViewModel,
        LocalRootNavController provides rootNavController
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = MainRoute::class,
        ) {
            mainScreen(appViewModel)
            webViewScreen()
        }
    }
}