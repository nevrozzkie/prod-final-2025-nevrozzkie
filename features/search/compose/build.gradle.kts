plugins {
    id("compose-setup")
}

dependencies {
    api(project(":features:search:presentation"))
    implementation(project(":features:main:compose"))
}
