plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.dagger)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetpack.nav.safeargs)
}

android {
    namespace = "com.kafasan.store"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kafasan.store"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.jetpack.navigation.fragment)
    implementation(libs.jetpack.navigation.ui)
    implementation(libs.jetpack.navigation.compose)

    implementation(libs.dagger.hilt)
    implementation(libs.dagger.hilt.nav.compose)
    ksp(libs.dagger.compiler)

    implementation(libs.coil.okhttp)
    implementation(libs.coil.compose)

    implementation(libs.jetpack.paging.runtime)
    implementation(libs.jetpack.paging.compose)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gsonConverter)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.coroutines)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.dagger.hilt.test)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.jetpack.navigation.test)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.mockwebserver)
    kspTest(libs.dagger.compiler)

    androidTestImplementation(libs.dagger.hilt.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk)
    kspAndroidTest(libs.dagger.compiler)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}