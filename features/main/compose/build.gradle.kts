plugins {
    id("compose-setup")
}

dependencies {
    api(project(":features:main:presentation"))
}

android {

    // fix IDE Error when using resources
    namespace = "com.prodfinal2025.nevrozq.features.compose"
}
