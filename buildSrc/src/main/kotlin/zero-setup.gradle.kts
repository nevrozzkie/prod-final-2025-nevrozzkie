import org.gradle.accessors.dm.LibrariesForLibs

//val libs =  extensions.getByType<VersionCatalogsExtension>().named("libs")

val libs = the<LibrariesForLibs>()
plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
}

android {
    namespace = "com.prod2025.nevrozqIndividual"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
    }
}

kotlin {
    jvmToolchain(17)
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}


