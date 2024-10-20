package org.zipper.ant.forest.xposed.data

import org.zipper.ant.forest.xposed.enums.AppThemeScheme

data class AppSettingsData(
    val useDynamicColor: Boolean,
    val appThemeScheme: AppThemeScheme
)