pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ProdFinal"
include(":app")
include(":core")
include(":utils")
include(":utils-compose")
include(":features:settings:api")
include(":features:settings:data")
