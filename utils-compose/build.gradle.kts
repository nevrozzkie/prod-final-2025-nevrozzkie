plugins {
    id("compose-common-setup")
}



dependencies {


    api(project(":utils"))

    implementation(libs.androidx.accompanist.systemuicontroller)
}


