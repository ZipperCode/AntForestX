import org.zipper.ant.forest.build.BuildConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "org.xposed.antforestx.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {

        debug {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("java.lang.String", "MODULE_PACKAGE_NAME", "\"${BuildConfig.ApplicationId}\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("java.lang.String", "MODULE_PACKAGE_NAME", "\"${BuildConfig.ApplicationId}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly(fileTree("dir" to "libs", "includes" to listOf("*.jar")))
    implementation(projects.core)
    implementation(projects.data)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
    api(libs.moshi)
    implementation(libs.workmanager)
    implementation(libs.androidx.datastore.preferences)
}