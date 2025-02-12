plugins {
    id("compose-setup")
}

kotlin {
    dependencies {
        api(project(":utils"))

        implementation(libs.androidx.accompanist.systemuicontroller)
    }
}

