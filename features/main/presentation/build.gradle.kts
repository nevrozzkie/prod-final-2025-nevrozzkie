plugins {
    id("presentation-setup")
    kotlin("plugin.serialization")
}


dependencies {
    api(project(":features:main:api"))
}