plugins {
    id("data-setup")
}

dependencies {
    api(project(":features:search:api"))
    implementation(project(":features:main:data"))
}
