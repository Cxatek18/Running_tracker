import org.gradle.kotlin.dsl.implementation
import java.util.Properties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.example.runningtracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.runningtracker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "YANDEX_MAPKIT_API_KEY",
            localProperties.getProperty("YANDEX_MAPKIT_API_KEY", "")
        )
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "2.2"
        apiVersion = "2.2"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.2.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    hilt {
        enableAggregatingTask = false
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Core
    implementation(libs.core)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)

    // Navigation
    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.compose)

    // Lifecycle viewmodel compose
    implementation(libs.lifecycle.viewmodel.compose)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.adapter.rxjava)

    // Okhttp3
    implementation(libs.okhttp3.core)
    implementation(libs.okhttp3.logging.interceptor)

    // Coil compose
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.navigation.testing)

    // Yandex MapKit
    implementation(libs.yandex.maps.mobile)
    implementation(libs.yandex.mapkit.kmp)
    implementation(libs.yandex.mapkit.kmp.compose)

    // Accompanist Permissions
    implementation(libs.accompanist.permissions)

    // Kotlinx Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}