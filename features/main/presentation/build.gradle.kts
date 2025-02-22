plugins {
    id("presentation-setup")
    kotlin("plugin.serialization")
}


dependencies {
    api(project(":features:main:api"))
    implementation(libs.kotlinx.serialization.json)
}

android {

    // fix IDE Error when using resources
    namespace = "com.prodfinal2025.nevrozq.features.main.presentation"
}