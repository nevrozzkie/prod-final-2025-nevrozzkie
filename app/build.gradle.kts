plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.compose.get().pluginId)
    id(libs.plugins.serialization.get().pluginId)
}

android {
    namespace = "com.prodfinal2025.nevrozq"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.prodfinal2025.nevrozq"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":features:main:compose"))
    implementation(project(":features:main:data"))
    implementation(project(":features:search:compose"))
    implementation(project(":features:search:data"))
    implementation(project(":features:finance:compose"))
    implementation(project(":features:finance:data"))
    implementation(project(":features:social:compose"))
    implementation(project(":features:social:data"))

    implementation(libs.decompose.compose)

    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.icons)
    implementation(libs.androidx.compose.icons.extended)
    implementation(libs.androidx.compose.ui.tooling)

    implementation(project(":core"))
    implementation(project(":utils-compose"))
}