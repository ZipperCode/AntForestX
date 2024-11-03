plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id(libs.plugins.antforestx.android.library.get().pluginId)
}

android {
    namespace = "org.xposed.forestx.core"
}

dependencies {
    api(libs.timber)
    api(libs.kotlinx.coroutines.android)
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.moshi)
}