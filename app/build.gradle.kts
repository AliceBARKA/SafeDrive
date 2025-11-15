plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services) // 🔹 Firebase Plugin
}

android {
    namespace = "com.example.monapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.monapplication"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Firebase dependencies
    implementation(platform(libs.firebase.bom)) // Déclare le BOM Firebase pour gérer les versions automatiquement
    implementation(libs.firebase.auth.ktx) // Firebase Authentication
    implementation(libs.firebase.firestore.ktx) // Firebase Firestore
    implementation(libs.firebase.database.ktx) // Firebase Database
    implementation(libs.okhttp)


    // AndroidX dependencies
    implementation(libs.androidx.core.ktx) // Extensions de la bibliothèque AndroidX Core
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // ViewModel avec Kotlin extensions
    implementation(libs.androidx.activity.ktx) // Activité avec Kotlin extensions
    implementation(libs.androidx.lifecycle.runtime.ktx) // Runtime de lifecycle avec Kotlin extensions
    implementation(libs.androidx.material) // Material Design
    implementation(libs.appcompat)// Support AppCompat pour la compatibilité avec les anciennes versions d'Android

    // Google Play services
    implementation(libs.play.services.location) // Google Play services pour la gestion de la localisation





    // Test dependencies
    testImplementation(libs.junit) // JUnit pour les tests unitaires
    androidTestImplementation(libs.androidx.junit) // JUnit pour les tests Android
    androidTestImplementation(libs.androidx.espresso.core) // Espresso pour les tests UI
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Vérifie la dernière version dans la console Firebase
        // Autres classpaths si nécessaire
    }
}
