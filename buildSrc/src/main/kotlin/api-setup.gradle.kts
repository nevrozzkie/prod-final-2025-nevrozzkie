import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.kotlin

val libs = the<LibrariesForLibs>()
plugins {
    id("zero-setup")
//    kotlin("plugin.serialization")
}


dependencies {
//    implementation(libs.kotlinx.serialization.json)
    api(libs.kotlinx.coroutines)
}
