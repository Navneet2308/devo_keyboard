import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
//    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.keyboard_app.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.keyboard_app.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.serialization.json.v160)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)
    // JetPref Datastore module
    implementation(project(":lib:android"))
    implementation(project(":lib:kotlin"))
    implementation ("androidx.activity:activity-compose:1.7.0")
    implementation ("androidx.lifecycle:lifecycle-service:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation ("com.louiscad.splitties:splitties-systemservices:3.0.0")
    implementation ("com.louiscad.splitties:splitties-views:3.0.0")
    implementation("com.google.accompanist:accompanist-flowlayout:0.32.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")

    implementation(libs.core.ktx)
    implementation(libs.androidx.adaptive.android)

    debugImplementation(libs.compose.ui.tooling)

}