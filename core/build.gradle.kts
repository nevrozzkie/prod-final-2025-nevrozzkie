plugins {
    id("zero-setup")
}


dependencies {
    api(libs.koin.android)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.json)
    implementation(libs.ktor.client.negotiation)
    implementation(libs.ktor.client.logging)
}
