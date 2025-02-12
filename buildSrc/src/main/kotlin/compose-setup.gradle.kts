import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()
plugins {
    id("zero-setup")
    kotlin("plugin.compose")
}

kotlin {
    dependencies {
        // An error in the editor, although the dependencies are correctly resolved.
//        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.material3)
        implementation(libs.androidx.compose.icons)
        implementation(libs.androidx.compose.ui.tooling)
        implementation(libs.decompose.compose)
    }
}

