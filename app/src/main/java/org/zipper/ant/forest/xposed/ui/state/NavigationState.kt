package org.zipper.ant.forest.xposed.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlin.reflect.KClass


data class NavigationAppState(
    val rootNavigationState: NavigationState
) {

    val rootStartDestination get() = rootNavigationState.startDestination

    val rootNavController get() = rootNavigationState.navController
}

@Composable
fun rememberNavigationState(
    startDestination: KClass<*>,
    navController: NavHostController = rememberNavController()
) = remember(startDestination, navController) {
    NavigationState(startDestination, navController)
}

@Stable
class NavigationState(
    val startDestination: KClass<*>,
    val navController: NavHostController
)