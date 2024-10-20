package org.zipper.ant.forest.xposed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.zipper.ant.forest.xposed.ui.screen.LogcatScreen

@Serializable
data object LogcatRoute

fun NavController.navigateToLogcat(navOptions: NavOptions) = navigate(route = LogcatRoute, navOptions)

fun NavGraphBuilder.logcatScreen() {
    composable<LogcatRoute>() {
        LogcatScreen()
    }
}