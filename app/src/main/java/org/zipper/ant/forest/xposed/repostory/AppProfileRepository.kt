package org.zipper.ant.forest.xposed.repostory

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.zipper.ant.forest.xposed.data.AppProfileData
import org.zipper.ant.forest.xposed.enums.AppThemeScheme

class AppProfileRepository(
    private val appProfileDataSource: DataStore<Preferences>
) : KoinComponent {

    val appProfileData: Flow<AppProfileData> = appProfileDataSource.data.map { pref ->
        pref.appProfileData()
    }

    suspend fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        updateAppProfileData {
            it.copy(useDynamicColor = useDynamicColor)
        }
    }

    suspend fun updateDarkThemeScheme(darkThemeScheme: AppThemeScheme) {
        updateAppProfileData {
            it.copy(themeScheme = darkThemeScheme)
        }
    }

    private suspend fun updateAppProfileData(block: (AppProfileData) -> AppProfileData) {
        appProfileDataSource.edit {
            val data = it.appProfileData().run(block)
            it[PreferenceKeys.AppProfileData] = Json.encodeToString(serializer(), data)
        }
    }

    private fun Preferences.appProfileData(): AppProfileData {
        return this[PreferenceKeys.AppProfileData]?.let {
            Json.decodeFromString<AppProfileData>(serializer(), it)
        } ?: AppProfileData()
    }
}