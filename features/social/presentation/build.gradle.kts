plugins {
    id("presentation-setup")
    id(libs.plugins.serialization.get().pluginId)
}


dependencies {
    api(project(":features:social:api"))
}