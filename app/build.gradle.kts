// build.gradle.kts (Module :app)

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Add the Google services plugin for Firebase integration
    id("com.google.gms.google-services")
    // Add the Kotlin Symbol Processing (KSP) plugin for Room's annotation processor
    id("com.google.devtools.ksp")
    // Add the Safe Args plugin for type-safe navigation
    id("androidx.navigation.safeargs.kotlin")
}

android {
    // IMPORTANT: Change this to your actual package name
    namespace = "com.yourname.mytodoapp"
    compileSdk = 34

    defaultConfig {
        // IMPORTANT: Change this to your actual package name
        applicationId = "com.yourname.mytodoapp"
        minSdk = 26
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
    // Enable View Binding to easily access UI elements from code
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // Standard AndroidX libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Navigation Component for moving between screens
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // ViewModel and LiveData for data handling that survives configuration changes
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.fragment:fragment-ktx:1.7.1")

    // Room Database for local, on-device storage
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // Use KSP instead of KAPT for Room's annotation processor (it's faster)
    ksp("androidx.room:room-compiler:$roomVersion")

    // Firebase Bill of Materials (BoM) - this ensures all your Firebase libraries are compatible
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Firebase Firestore for cloud database
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Firebase Authentication for anonymous user identification
    implementation("com.google.firebase:firebase-auth-ktx")

    // Testing libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.recyclerview:recyclerview:1.3.0")
}
