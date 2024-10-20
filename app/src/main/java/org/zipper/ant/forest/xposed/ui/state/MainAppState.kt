package org.zipper.ant.forest.xposed.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import org.zipper.ant.forest.xposed.navigation.TopLevelDestination
import org.zipper.ant.forest.xposed.navigation.navigateToHome
import org.zipper.ant.forest.xposed.navigation.navigateToLogcat
import org.zipper.ant.forest.xposed.navigation.navigateToProfile

@Composable
fun rememberMainAppState(
    navController: NavHostController = rememberNavController(),
): MainAppState = remember(navController) {
    MainAppState(navController)
}


@Stable
class MainAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) ?: false
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // 仅一个实例
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.Home -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.Logcat -> navController.navigateToLogcat(topLevelNavOptions)
            TopLevelDestination.Profile -> navController.navigateToProfile(topLevelNavOptions)
        }
    }
}