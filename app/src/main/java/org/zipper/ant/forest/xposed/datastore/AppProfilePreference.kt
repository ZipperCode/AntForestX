package org.zipper.ant.forest.xposed.datastore

import kotlinx.serialization.Serializable
import org.zipper.ant.forest.xposed.enums.AppThemeScheme
import org.zipper.antforestx.data.serializer.BaseDataStoreSerializer

@Serializable
data class AppProfilePreference(
    val useDynamicColor: Boolean = false,
    val themeScheme: AppThemeScheme = AppThemeScheme.Light
) {
    companion object {
        val dataStoreSerializer: BaseDataStoreSerializer<AppProfilePreference> =
            object : BaseDataStoreSerializer<AppProfilePreference>(serializer()) {
                override val defaultValue: AppProfilePreference get() = AppProfilePreference()
            }
    }
}