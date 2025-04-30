// build.gradle.kts (корень)
plugins {
    id("com.android.application") version "7.3.1" apply false
    kotlin("android") version "1.7.10" apply false
}
subprojects {
    repositories {
        google()
        mavenCentral()
    }
}
