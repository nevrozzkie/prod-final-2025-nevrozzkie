plugins {
    id("presentation-setup")
}

kotlin {
    dependencies {
        implementation(project(":features:feed:api"))
    }
}