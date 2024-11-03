package org.zipper.ant.forest.xposed.koin

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.zipper.ant.forest.xposed.datastore.AppProfilePreference
import org.zipper.ant.forest.xposed.repostory.AppProfileRepository
import org.zipper.ant.forest.xposed.viewmodel.AntDataViewModel
import org.zipper.ant.forest.xposed.viewmodel.AntViewModel
import org.zipper.ant.forest.xposed.viewmodel.AppViewModel
import org.zipper.antforestx.data.antDataModule

private val APP_PREFERENCE_DS = named("app_preference_data_store")

private val APP_PROFILE_DS = named("app_profile_data_store")

private val Context.appProfileDataStore by preferencesDataStore("APP_PROFILE")

val appModule = module {
    single(APP_PROFILE_DS) {
        val context = androidContext()
        DataStoreFactory.create(
            serializer = AppProfilePreference.dataStoreSerializer
        ) {
            context.preferencesDataStoreFile("app_profile.ds")
        }
    }
    factory {
        AppProfileRepository(get(APP_PROFILE_DS))
    }
    viewModelOf(::AppViewModel)
    viewModelOf(::AntViewModel)
    viewModelOf(::AntDataViewModel)
}


val appModules = listOf(
    appModule,
    antDataModule
)