import org.jetbrains.kotlin.gradle.dsl.JvmTarget
plugins {
    `kotlin-dsl`
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies{
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
}
tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

val pluginVersion = "1.0.0"

gradlePlugin{
    plugins{
        register("androidApplication") {
            id = "antforestx.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
            version = pluginVersion
        }

        register("androidLibrary") {
            id = "antforestx.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
            version = pluginVersion
        }
    }
}