plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugin.convention.kotlin.android)
    implementation(libs.plugin.convention.android)
    implementation(libs.plugin.convention.compose.compiler)
    implementation(libs.plugin.convention.serialization)
    //https://stackoverflow.com/a/70878181
    // An error in the editor, although the dependencies are correctly resolved.
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
kotlin {
    jvmToolchain(17)
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}