plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Plugin para Room , base de datos
    id("com.google.devtools.ksp")
}

android {
    namespace = "ifp.pmdm.practicafinal"
    compileSdk = 35

    defaultConfig {
        applicationId = "ifp.pmdm.practicafinal"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Navegación moderna entre fragmentos
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Animaciones Lottie (Para los 2 puntos extra de innovación en UI)
    implementation(libs.lottie)
    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Extensiones Kotlin para Room
    ksp(libs.androidx.room.compiler) // Procesador de anotaciones (Moderno)
    // CameraX - La forma fácil de usar la cámara
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Google ML Kit - Para leer códigos de barras (Barcode Scanning)
    implementation(libs.barcode.scanning)
    // WorkManager - Para tareas programadas (Alarmas que resisten reinicios)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}