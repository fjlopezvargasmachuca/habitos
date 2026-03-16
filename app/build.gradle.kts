/**
 * Archivo de configuración Gradle del módulo principal de la aplicación.
 *
 * Este archivo define:
 *
 * - Plugins utilizados en el proyecto
 * - Configuración del módulo Android
 * - Versión de SDK utilizada
 * - Opciones de compilación
 * - Dependencias externas del proyecto
 *
 * Aquí se declaran las librerías necesarias para implementar
 * las funcionalidades principales de la aplicación como:
 *
 * - Jetpack Compose (interfaz)
 * - Room (base de datos local)
 * - Navigation Compose (navegación entre pantallas)
 * - Coroutines (operaciones asíncronas)
 * - Hilt (inyección de dependencias)
 * - Google Sign-In (autenticación con Google)
 * - iText (generación de documentos PDF)
 * - Coil (carga de imágenes desde internet)
 */

plugins {

    /**
     * Plugin principal para construir aplicaciones Android.
     */
    alias(libs.plugins.android.application)

    /**
     * Plugin que habilita el soporte de Kotlin para Android.
     */
    alias(libs.plugins.kotlin.android)

    /**
     * Plugin que habilita Jetpack Compose en el proyecto.
     */
    alias(libs.plugins.kotlin.compose)

    /**
     * Kotlin Symbol Processing.
     * Utilizado por librerías como Room o Hilt para generar código.
     */
    alias(libs.plugins.ksp)

    /**
     * Plugin de Google Services necesario para utilizar
     * funcionalidades como Google Sign-In.
     */
    id("com.google.gms.google-services")

    /**
     * Plugin de Hilt para inyección de dependencias.
     */
    id("com.google.dagger.hilt.android") version "2.48" apply false
}


android {

    /**
     * Namespace principal del proyecto.
     */
    namespace = "com.example.habitos"

    /**
     * Versión de Android SDK utilizada para compilar la aplicación.
     */
    compileSdk = 36


    defaultConfig {

        /**
         * Identificador único de la aplicación.
         */
        applicationId = "com.example.habitos"

        /**
         * Versión mínima de Android soportada por la app.
         */
        minSdk = 24

        /**
         * Versión objetivo de Android.
         */
        targetSdk = 36

        /**
         * Control de versiones de la aplicación.
         */
        versionCode = 1
        versionName = "1.0"

        /**
         * Runner utilizado para ejecutar tests instrumentados.
         */
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    // ───────────── CONFIGURACIÓN DE BUILD TYPES ─────────────

    buildTypes {

        /**
         * Configuración para la versión de producción (release).
         */
        release {

            /**
             * Desactiva la ofuscación de código mediante Proguard.
             */
            isMinifyEnabled = false

            /**
             * Archivos de configuración de Proguard.
             */
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    // ───────────── OPCIONES DE COMPILACIÓN ─────────────

    compileOptions {

        /**
         * Compatibilidad con Java 11.
         */
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        /**
         * Habilita desugaring para poder usar APIs modernas
         * de Java como java.time en versiones antiguas de Android.
         */
        isCoreLibraryDesugaringEnabled = true
    }


    /**
     * Configuración de Kotlin.
     */
    kotlinOptions {
        jvmTarget = "11"
    }


    /**
     * Habilita Jetpack Compose en el proyecto.
     */
    buildFeatures {
        compose = true
    }
}


dependencies {

    // ───────────── ROOM (BASE DE DATOS LOCAL) ─────────────

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    /**
     * Procesador de anotaciones utilizado por Room
     * para generar el código de acceso a base de datos.
     */
    ksp("androidx.room:room-compiler:$room_version")


    // ───────────── VIEWMODEL Y LIFECYCLE ─────────────

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")


    // ───────────── NAVEGACIÓN ENTRE PANTALLAS ─────────────

    implementation("androidx.navigation:navigation-compose:2.7.7")


    // ───────────── COROUTINES (PROGRAMACIÓN ASÍNCRONA) ─────────────

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    // ───────────── JETPACK COMPOSE (INTERFAZ DE USUARIO) ─────────────

    implementation("androidx.compose.material3:material3:1.2.0-alpha05")
    implementation("androidx.compose.material:material-icons-extended:1.5.1")


    // ───────────── HILT (INYECCIÓN DE DEPENDENCIAS) ─────────────

    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-android-compiler:2.48")

    /**
     * Integración de Hilt con Navigation Compose.
     */
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")


    // ───────────── LIBRERÍAS BASE DE ANDROIDX ─────────────

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)


    // ───────────── DEPENDENCIAS DE DEBUG ─────────────

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // ───────────── DEPENDENCIAS DE TEST ─────────────

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)


    // ───────────── GENERACIÓN DE PDF ─────────────

    /**
     * Librería utilizada para crear documentos PDF
     * desde la aplicación.
     */
    implementation("com.itextpdf:itextg:5.5.10")


    // ───────────── AUTENTICACIÓN CON GOOGLE ─────────────

    /**
     * Librería necesaria para implementar Google Sign-In.
     */
    implementation("com.google.android.gms:play-services-auth:21.0.0")


    // ───────────── CARGA DE IMÁGENES (COIL) ─────────────

    /**
     * Librería utilizada para cargar imágenes desde internet
     * dentro de componentes Compose.
     */
    implementation("io.coil-kt:coil-compose:2.4.0")


    // ───────────── DESUGARING ─────────────

    /**
     * Permite utilizar APIs modernas de Java como java.time
     * en dispositivos con versiones antiguas de Android.
     */
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}
