// app/build.gradle.kts
plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
}
android {
    namespace = "com.example.stressmonitor"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.stressmonitor"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    aaptOptions {
        noCompress("tflite")
    }
}
dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    // BLE
    implementation("no.nordicsemi.android:ble:2.4.3")
    implementation("no.nordicsemi.android:ble-ktx:2.4.3")
    // Google Fit
    implementation("com.google.android.gms:play-services-fitness:21.0.1")
    implementation("com.google.android.gms:play-services-auth:20.5.0")
    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.12.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.3")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.12.0")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}
