plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jhoel.framepuzzle"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.jhoel.framepuzzle"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "0.3.0-alpha"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
            val storeFilePath = System.getenv("FRAMEPUZZLE_STORE_FILE")
            if (!storeFilePath.isNullOrEmpty()) {
                signingConfig = signingConfigs.create("release") {
                    storeFile = file(storeFilePath)
                    storePassword = System.getenv("FRAMEPUZZLE_STORE_PASSWORD")
                    keyAlias = System.getenv("FRAMEPUZZLE_KEY_ALIAS")
                    keyPassword = System.getenv("FRAMEPUZZLE_KEY_PASSWORD")
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true; buildConfig = true }
    packaging {
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:storage"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:utils"))
    implementation(project(":feature:camera"))
    implementation(project(":feature:library"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:settings"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.coroutines.android)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
