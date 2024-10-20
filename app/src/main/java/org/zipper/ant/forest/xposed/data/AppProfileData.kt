package org.zipper.ant.forest.xposed.data

import kotlinx.serialization.Serializable
import org.zipper.ant.forest.xposed.enums.AppThemeScheme

@Serializable
data class AppProfileData(
    val useDynamicColor: Boolean = false,
    val themeScheme: AppThemeScheme = AppThemeScheme.Light
) {
}