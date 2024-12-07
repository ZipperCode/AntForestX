package org.zipper.ant.forest.xposed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.zipper.ant.forest.xposed.ui.screen.HomeScreen
import org.zipper.ant.forest.xposed.ui.screen.LogcatScreen
import org.zipper.ant.forest.xposed.ui.screen.MainScreen
import org.zipper.ant.forest.xposed.ui.screen.ProfileScreen
import org.zipper.ant.forest.xposed.ui.screen.WebViewScreen
import org.zipper.ant.forest.xposed.ui.state.MainAppState
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel

@Serializable
data object MainRoute

fun NavGraphBuilder.mainScreen(
    appViewModel: AppViewModel,
) {
    composable<MainRoute> {
        MainScreen(appViewModel)
    }
}


@Serializable
data class WebViewRoute(
    val url: String
)

fun NavController.navigateToWebView(url: String, navOptions: NavOptions) = navigate(route = WebViewRoute(url), navOptions)

fun NavGraphBuilder.webViewScreen() {
    composable<WebViewRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<WebViewRoute>()
        WebViewScreen(route.url)
    }
}

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen() {
    composable<HomeRoute>() {
        HomeScreen()
    }
}

@Serializable
data object LogcatRoute

fun NavController.navigateToLogcat(navOptions: NavOptions) = navigate(route = LogcatRoute, navOptions)

fun NavGraphBuilder.logcatScreen() {
    composable<LogcatRoute>() {
        LogcatScreen()
    }
}

@Serializable
data object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions) = navigate(route = ProfileRoute, navOptions)

fun NavGraphBuilder.profileScreen() {
    composable<ProfileRoute>() {
        ProfileScreen()
    }
}