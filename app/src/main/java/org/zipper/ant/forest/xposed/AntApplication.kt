package org.zipper.ant.forest.xposed

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.zipper.ant.forest.xposed.koin.appModule
import org.zipper.ant.forest.xposed.koin.appModules

class AntApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AntApplication)
            modules(appModules)
        }
    }
}