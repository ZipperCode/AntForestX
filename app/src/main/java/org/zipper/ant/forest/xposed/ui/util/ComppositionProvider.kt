package org.zipper.ant.forest.xposed.ui.util

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import org.zipper.ant.forest.xposed.ui.AppViewModel

val LocalSnackbarHost = compositionLocalOf<SnackbarHostState> {
    error("CompositionLocal LocalSnackbarController not present")
}

val LocalNavController = compositionLocalOf<NavHostController> {
    error("CompositionLocal LocalNavController not present")
}

val LocalAppViewModel = compositionLocalOf<AppViewModel> {
    error("CompositionLocal LocalAppViewModel not present")
}