// build.gradle.kts (Project)

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    // Add the Google services plugin
    id("com.google.gms.google-services") version "4.4.1" apply false
    // Add the KSP plugin
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    // Add the Safe Args plugin
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}