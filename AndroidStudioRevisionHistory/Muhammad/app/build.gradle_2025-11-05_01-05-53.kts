import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("org.jetbrains.kotlin.plugin.compose")

    id("org.jlleitschuh.gradle.ktlint")

    id("org.jetbrains.kotlin.plugin.serialization")

    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.emptyactivity"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.emptyactivity"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")


    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.57.2")
    kapt("com.google.dagger:hilt-android-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Supabase
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.0.2")
    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.2")
    implementation("io.github.jan-tennert.supabase:storage-kt:3.0.2")

    // Ktor (required by Supabase & Gemini) - Aligned to a compatible version
    implementation("io.ktor:ktor-client-android:2.3.2")
    implementation("io.ktor:ktor-client-core:2.3.2")
    implementation("io.ktor:ktor-utils:2.3.2")

    

    // Gemini AI
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Add this block at the end of your build.gradle.kts file
// This forces all Ktor dependencies to use the version required by the Gemini AI library
configurations.all {
    resolutionStrategy.force("io.ktor:ktor-client-core:2.3.2")    resolutionStrategy.force("io.ktor:ktor-client-okhttp:2.3.2")
    resolutionStrategy.force("io.ktor:ktor-client-android:2.3.2")
    resolutionStrategy.force("io.ktor:ktor-client-content-negotiation:2.3.2")
    resolutionStrategy.force("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
    resolutionStrategy.force("io.ktor:ktor-utils:2.3.2")
}


kapt {
    correctErrorTypes = true
}

ktlint {
    android.set(true)
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    // Disable annoying rules
    filter {
        exclude("**/build/**")
    }
}
