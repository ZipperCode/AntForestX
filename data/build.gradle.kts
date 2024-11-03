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
    implementation(projects.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.androidx.datastore.preferences)
}