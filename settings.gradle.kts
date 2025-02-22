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

include(":features:main:api")
include(":features:main:data")
include(":features:main:presentation")
include(":features:main:compose")

include(":features:search:api")
include(":features:search:data")
include(":features:search:presentation")
include(":features:search:compose")

include(":features:finance:api")
include(":features:finance:data")
include(":features:finance:presentation")
include(":features:finance:compose")

include(":features:social:api")
include(":features:social:data")
include(":features:social:presentation")
include(":features:social:compose")

//include(":features:search:api")
//include(":features:search:data")
//include(":features:search:presentation")
//include(":features:search:compose")

//include(":features:settings:api")
//include(":features:settings:data")
