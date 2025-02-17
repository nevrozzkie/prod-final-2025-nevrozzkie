import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.the

val libs = the<LibrariesForLibs>()
plugins {
    id("zero-setup")
    kotlin("plugin.compose")
}


dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.icons.extended)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.decompose.compose)
    implementation(libs.decompose.core)

}


