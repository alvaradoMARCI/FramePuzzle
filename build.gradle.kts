/*
 * FramePuzzle - Root build script
 * Plugins compartidos por todos los módulos.
 */

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}

// Forzar JavaPoet 1.13.0 en todo el proyecto para compatibilidad con Hilt 2.52
// (evita NoSuchMethodError canonicalName() en AggregateDepsTask, causado por
// databinding-compiler-common:8.5.2 que arrastra javapoet 1.10.0).
buildscript {
    configurations.classpath {
        resolutionStrategy.force("com.squareup:javapoet:${libs.versions.javapoet.get()}")
    }
}

subprojects {
    configurations.configureEach {
        resolutionStrategy {
            force("com.squareup:javapoet:${libs.versions.javapoet.get()}")
        }
    }
}
