plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id(libs.plugins.antforestx.android.library.get().pluginId)
}

android {
    namespace = "org.zipper.antforest.data"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}