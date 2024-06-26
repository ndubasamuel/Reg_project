// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()

    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }
}

plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id ("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}