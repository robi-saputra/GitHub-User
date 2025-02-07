plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.dagger.hilt.android)
}
val propertiesFile = file("../networks/config/endpoint.properties")
android {
    namespace = "robi.codingchallenge.networks"
    compileSdk = 35
    buildFeatures.buildConfig = true

    defaultConfig {
        minSdk = 24
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            if (propertiesFile.exists()) {
                propertiesFile.forEachLine {
                    val (key, value) = it.split("=")
                    if (key.isNotBlank() && value.isNotBlank()) {
                        buildConfigField("String", key, value)
                    }
                }
            } else {
                throw GradleException("Properties file not found: $propertiesFile")
            }
            isMinifyEnabled = false
        }
        release {
            if (propertiesFile.exists()) {
                propertiesFile.forEachLine {
                    val (key, value) = it.split("=")
                    if (key.isNotBlank() && value.isNotBlank()) {
                        buildConfigField("String", key, value)
                    }
                }
            } else {
                throw GradleException("Properties file not found: $propertiesFile")
            }
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.google.devtools.ksp)
    implementation(libs.google.hilt.android)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.adapter.rxjava3)
    implementation(libs.squareup.converter.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging.interceptor)

    ksp(libs.google.dagger.compiler)
    ksp(libs.google.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

hilt {
    enableAggregatingTask = false
}