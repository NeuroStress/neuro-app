pluginManagement {
    repositories {
        gradlePluginPortal()    // для Kotlin DSL
        google()                // плагин Android и Hilt
        mavenCentral()
    }
    plugins {
        // Подписываем плагин Hilt, чтобы его можно было вызывать в модулях
        id("com.google.dagger.hilt.android") version "2.47"
        // И Android-gradle-plugin, Kotlin-плагин, если вы хотите
        id("com.android.application") version "8.9.2"
        kotlin("android") version "1.9.0"
        kotlin("kapt") version "1.9.0"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "MyStressApp"
include(":app")
