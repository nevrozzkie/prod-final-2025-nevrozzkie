// Related to buildSrc/build.gradle.kts
plugins {
    id(libs.plugins.kotlin.android.get().pluginId).apply(false)
    id(libs.plugins.serialization.get().pluginId).apply(false)
    id(libs.plugins.kotlin.compose.get().pluginId) apply false
}

