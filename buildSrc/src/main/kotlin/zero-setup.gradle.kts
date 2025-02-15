import org.gradle.accessors.dm.LibrariesForLibs

//val libs =  extensions.getByType<VersionCatalogsExtension>().named("libs")

val libs = the<LibrariesForLibs>()
plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "com.prodfinal2025.nevrozq"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
}

dependencies {
    implementation(libs.kotlinx.datetime)
}

kotlin {
    jvmToolchain(17)
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}


