plugins {
    id("compose-setup")
}

dependencies {
    api(project(":features:social:presentation"))
    implementation(libs.androidx.activity.compose)
}