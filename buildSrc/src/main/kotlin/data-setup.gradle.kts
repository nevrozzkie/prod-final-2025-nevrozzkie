import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.dependencies

val libs = the<LibrariesForLibs>()
plugins {
    id("zero-setup")
    kotlin("plugin.serialization")
    id("androidx.room")
}


dependencies {
    implementation(libs.ktor.client.core)
    implementation(project(":core"))
    implementation(project(":utils"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.room.runtime)

    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

}
room {
    schemaDirectory("$projectDir/schemas")
}
