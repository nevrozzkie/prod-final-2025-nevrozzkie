plugins {
    id("presentation-setup")
    kotlin("plugin.serialization")
}


dependencies {
    api(project(":features:search:api"))
    api(project(":features:main:presentation"))
}