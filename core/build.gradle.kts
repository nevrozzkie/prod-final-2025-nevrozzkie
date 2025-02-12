plugins {
    id("logic-setup")
//    id(libs.plugins.serialization.get().pluginId)
}

kotlin {
    dependencies {
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.android)
        implementation(libs.ktor.client.json)
        implementation(libs.ktor.client.negotiation)
        implementation(libs.ktor.client.logging)
    }
}