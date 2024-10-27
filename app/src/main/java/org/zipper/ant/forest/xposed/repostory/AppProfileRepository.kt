package org.zipper.ant.forest.xposed.repostory

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.zipper.ant.forest.xposed.datastore.AppProfilePreference
import org.zipper.ant.forest.xposed.enums.AppThemeScheme

class AppProfileRepository(
    private val appProfileDataSource: DataStore<AppProfilePreference>
) : KoinComponent {

    val appProfilePreference: Flow<AppProfilePreference> = appProfileDataSource.data

    suspend fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        appProfileDataSource.updateData {
            it.copy(useDynamicColor = useDynamicColor)
        }
    }

    suspend fun updateDarkThemeScheme(darkThemeScheme: AppThemeScheme) {
        appProfileDataSource.updateData {
            it.copy(themeScheme = darkThemeScheme)
        }
    }

}