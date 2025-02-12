import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.dependencies

val libs = the<LibrariesForLibs>()
plugins {
    id("zero-setup")
}

kotlin {
    dependencies {
//        implementation(libs.kotlinx.coroutines)
        implementation(libs.ktor.client.core)
        implementation(project(":core"))
    }
}