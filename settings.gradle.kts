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

include(":features:feed:api")
include(":features:feed:data")
include(":features:feed:presentation")
include(":features:feed:compose")

//include(":features:search:api")
//include(":features:search:data")
//include(":features:search:presentation")
//include(":features:search:compose")

//include(":features:settings:api")
//include(":features:settings:data")
