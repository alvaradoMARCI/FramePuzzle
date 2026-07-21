/*
 * FramePuzzle
 * "Arma tus recuerdos"
 * Copyright (c) Jhoel
 *
 * Configuración de módulos del proyecto.
 * Clean Architecture + MVVM, multi-módulo.
 */

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FramePuzzle"

// ----------------------------------------------------------------------------
// Módulo app (capa Presentation principal + navegación)
// ----------------------------------------------------------------------------
include(":app")

// ----------------------------------------------------------------------------
// Core: elementos compartidos
// ----------------------------------------------------------------------------
include(":core:domain")
include(":core:database")
include(":core:storage")
include(":core:security")
include(":core:designsystem")
include(":core:utils")

// ----------------------------------------------------------------------------
// Feature: módulos funcionales
// ----------------------------------------------------------------------------
include(":feature:camera")
include(":feature:editor")
include(":feature:puzzle")
include(":feature:library")
include(":feature:profile")
include(":feature:backup")
include(":feature:transfer")
include(":feature:settings")
