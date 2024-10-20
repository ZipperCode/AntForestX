package org.zipper.ant.forest.xposed.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import org.zipper.ant.forest.xposed.ui.screen.ProfileScreen

@Serializable
data object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions) = navigate(route = ProfileRoute, navOptions)

fun NavGraphBuilder.profileScreen() {
    composable<ProfileRoute>() {
        ProfileScreen()
    }
}