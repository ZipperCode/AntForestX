package org.zipper.ant.forest.xposed.ui.state

import org.zipper.ant.forest.xposed.datastore.AppProfilePreference

sealed class MainUIState {
    data object Loading : MainUIState()
    data class Success(val profileData: AppProfilePreference) : MainUIState()
}