package org.zipper.ant.forest.xposed.ui.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import org.zipper.ant.forest.xposed.ui.presentation.destinations.LogcatScreenDestination
import org.zipper.ant.forest.xposed.ui.presentation.destinations.MainScreenDestination
import org.zipper.ant.forest.xposed.ui.presentation.destinations.ProfileScreenDestination

/**
 *
 * @author  zhangzhipeng
 * @date    2024/8/30
 */

enum class BottomBarItem(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    val label: String
){
    Home(MainScreenDestination, Icons.Filled.Home, "Home"),
    Logcat(LogcatScreenDestination, Icons.Filled.Notifications, "日志"),
    Profile(ProfileScreenDestination, Icons.Filled.Notifications, "Profile")
}