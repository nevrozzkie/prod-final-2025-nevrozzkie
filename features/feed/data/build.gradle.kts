plugins {
    id("data-setup")
}

kotlin {
    dependencies {
        api(project(":features:feed:api"))
    }
}