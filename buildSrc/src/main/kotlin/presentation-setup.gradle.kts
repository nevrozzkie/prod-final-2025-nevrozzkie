import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

val libs = the<LibrariesForLibs>()
plugins {
    id("zero-setup")
}


dependencies {
    implementation(project(":core"))
    implementation(project(":utils"))


    api(libs.decompose.core)
    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.coroutines)
}
