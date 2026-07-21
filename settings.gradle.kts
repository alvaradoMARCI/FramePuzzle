pluginManagement {
    repositories {
        google()
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
include(":app")
include(":core:domain")
include(":core:database")
include(":core:storage")
include(":core:security")
include(":core:designsystem")
include(":core:utils")
include(":feature:camera")
include(":feature:editor")
include(":feature:puzzle")
include(":feature:library")
include(":feature:profile")
include(":feature:backup")
include(":feature:transfer")
include(":feature:settings")
