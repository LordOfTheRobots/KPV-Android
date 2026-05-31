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
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven {
            url = uri("https://maven.yandex.ru/")
        }
    }
}

rootProject.name = "BankCardsManagement"

include(":app")

include(":core:network")
include(":core:database")
include(":core:maps")
include(":core:ui")
include(":core:common")

include(":data")

include(":feature:auth")
include(":feature:cards")
include(":feature:map")
include(":domain:core")
include(":domain:repository")
include(":domain:usecases")
include(":feature:core")
include(":core:navigation")
include(":feature:transactions")
include(":feature:settings")
include(":feature:transfer")
include(":feature:hw9")
