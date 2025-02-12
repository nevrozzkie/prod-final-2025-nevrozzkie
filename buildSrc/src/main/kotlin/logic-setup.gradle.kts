import org.gradle.accessors.dm.LibrariesForLibs


val libs = the<LibrariesForLibs>()
plugins {
    id("zero-setup")
}

kotlin {
    dependencies {
        // An error in the editor, although the dependencies are correctly resolved.
        api(libs.koin.android)
    }
}

