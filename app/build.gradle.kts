
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs")
    id("kotlin-kapt")
}

android {
    namespace = "com.register"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.register"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    kapt ("com.android.databinding:compiler:3.1.4")
    implementation ("com.android.databinding:viewbinding:8.3.1")

//    Rx Java
    implementation ("io.reactivex.rxjava2:rxjava:2.2.9")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.0")

//    LiveData
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
//    Rx Binding
    implementation ("com.jakewharton.rxbinding2:rxbinding:2.1.1")
// Room
    implementation("androidx.room:room-rxjava2:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")

    implementation ("android.arch.persistence.room:runtime:1.1.1")
    implementation ("android.arch.persistence.room:rxjava2:1.1.1")
    kapt ("android.arch.persistence.room:compiler:1.1.1")

//    annotationProcessor ("android.arch.persistence.room:compiler:$rootProject.roomVersion")
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    //navigation dependencies
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

//    Dagger2
    kapt ("com.google.dagger:dagger-compiler:2.48.1")
    implementation ("com.google.dagger:dagger:2.48.1")
    implementation ("com.google.dagger:dagger-android:2.48.1")
    implementation ("com.google.dagger:dagger-android-support:2.48.1")
    kapt ("com.google.dagger:dagger-android-processor:2.48.1")
    kapt("com.google.dagger:dagger-compiler:2.48.1")



}