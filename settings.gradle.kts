pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // <-- Necesario para Kotlin Compose
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "habitos"
include(":app")