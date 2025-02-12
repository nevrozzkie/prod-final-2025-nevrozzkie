plugins {
    id("compose-setup")
}

kotlin {
    dependencies {
        implementation(":features:feed:presentation")
    }
}