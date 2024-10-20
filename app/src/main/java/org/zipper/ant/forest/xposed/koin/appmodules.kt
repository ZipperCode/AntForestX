package org.zipper.ant.forest.xposed.koin

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.zipper.ant.forest.xposed.repostory.AppProfileRepository
import org.zipper.ant.forest.xposed.ui.AppViewModel

private val APP_PROFILE_DS_DL = named("app_profile_data_store")

private val Context.appProfileDataStore by preferencesDataStore("APP_PROFILE")

val appModule = module {
    single(APP_PROFILE_DS_DL) {
        androidContext().appProfileDataStore
    }
    factory {
        AppProfileRepository(get(APP_PROFILE_DS_DL))
    }
    viewModelOf(::AppViewModel)
}


val appModules = listOf(
    appModule
)