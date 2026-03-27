plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs") version "2.9.0"
}

android {
    namespace = "com.example.movenow"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.movenow"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // --- UI ---
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // --- Navigation ---
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // --- Firebase (Auth + Firestore) ---
    implementation(platform(libs.firebase.bom)) // BOM para mantener todo sincronizado
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // --- Google Sign-In ---
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- WorkManager para tareas en segundo plano ---
    implementation("androidx.work:work-runtime:2.9.0")

    // --- MPAndroidChart para gráficas ---
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Es para Guava
    implementation("com.google.guava:guava:33.0.0-android")

}
