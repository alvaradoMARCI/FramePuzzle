/*
 * FramePuzzle - app module
 * Punto de entrada de la aplicación. Contiene MainActivity, navegación y di.
 *
 * Configuración de firma (sección 47 del Master Document):
 *  - El keystore NUNCA se sube al repositorio.
 *  - Las credenciales se leen de variables de entorno o ~/.gradle/gradle.properties.
 *  - Sin credenciales => build release falla (no se permite APK sin firmar).
 */

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// ----------------------------------------------------------------------------
// Lectura segura de credenciales de firma (variables de entorno).
// ----------------------------------------------------------------------------
val framePuzzleStoreFile: File? = System.getenv("FRAMEPUZZLE_STORE_FILE")?.let { File(it) }
val framePuzzleStorePassword: String = System.getenv("FRAMEPUZZLE_STORE_PASSWORD").orEmpty()
val framePuzzleKeyAlias: String = System.getenv("FRAMEPUZZLE_KEY_ALIAS").orEmpty()
val framePuzzleKeyPassword: String = System.getenv("FRAMEPUZZLE_KEY_PASSWORD").orEmpty()

val hasSigningCredentials: Boolean =
    framePuzzleStoreFile != null && framePuzzleStoreFile.exists() &&
        framePuzzleStorePassword.isNotEmpty() &&
        framePuzzleKeyAlias.isNotEmpty() &&
        framePuzzleKeyPassword.isNotEmpty()

android {
    namespace = "com.jhoel.framepuzzle"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jhoel.framepuzzle"
        minSdk = 28
        targetSdk = 34
        versionCode = 4
        versionName = "0.4.0-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    // ------------------------------------------------------------------------
    // Signing config Release. Cargado desde variables de entorno.
    // Si las variables no existen, el build Release fallará con un mensaje claro.
    // ------------------------------------------------------------------------
    signingConfigs {
        create("release") {
            if (hasSigningCredentials) {
                storeFile = framePuzzleStoreFile
                storePassword = framePuzzleStorePassword
                keyAlias = framePuzzleKeyAlias
                keyPassword = framePuzzleKeyPassword
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            // Firma obligatoria: si no hay credenciales, el build falla aquí
            // (no se permite APK Release sin firmar).
            if (hasSigningCredentials) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                // No lanzamos exception en configuration phase porque rompe
                // otros tasks (wrapper, tasks, etc.). Solo falla al ejecutar
                // assembleRelease explícitamente.
                println("WARNING: FramePuzzle Release build requires signing credentials.")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
        }
    }

    // Enciende el reporte de advertencias como errores en lint crítico.
    lint {
        abortOnError = true
        checkReleaseBuilds = true
        warningsAsErrors = false
        disable += "MissingTranslation"
    }
}

// ----------------------------------------------------------------------------
// Guardián de firma Release: impide generar assembleRelease sin credenciales.
// ----------------------------------------------------------------------------
project.tasks.matching { it.name == "assembleRelease" }.configureEach {
    doFirst {
        if (!hasSigningCredentials) {
            throw GradleException(
                "FramePuzzle Release build requires signing credentials. " +
                    "Set env vars: FRAMEPUZZLE_STORE_FILE, FRAMEPUZZLE_STORE_PASSWORD, " +
                    "FRAMEPUZZLE_KEY_ALIAS, FRAMEPUZZLE_KEY_PASSWORD.",
            )
        }
    }
}

// Forzar JavaPoet 1.13.0 en classpath de KSP para compatibilidad con Hilt 2.52
// (algunas libs traen 1.11+ que rompe AggregateDepsTask).
configurations.matching { it.name.startsWith("ksp") }.configureEach {
    resolutionStrategy.force("com.squareup:javapoet:${libs.versions.javapoet.get()}")
}

dependencies {
    // Core modules
    implementation(project(":core:database"))
    implementation(project(":core:storage"))
    implementation(project(":core:security"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:utils"))

    // Feature modules
    implementation(project(":feature:camera"))
    implementation(project(":feature:editor"))
    implementation(project(":feature:puzzle"))
    implementation(project(":feature:library"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:backup"))
    implementation(project(":feature:transfer"))
    implementation(project(":feature:settings"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
