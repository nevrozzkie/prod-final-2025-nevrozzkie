
plugins {
    id("api-setup")
    id(libs.plugins.serialization.get().pluginId)
}

dependencies {

    implementation(libs.kotlinx.serialization.json)
}