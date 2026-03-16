// Top-level build file where you can add configuration options common to all sub-projects/modules.
/**
 * Archivo de configuración principal de Gradle para el proyecto.
 *
 * Este archivo define los plugins que estarán disponibles para
 * los distintos módulos del proyecto Android.
 *
 * En este caso se utilizan alias definidos en el catálogo de
 * versiones (Version Catalog) para mantener una gestión centralizada
 * de dependencias y plugins.
 *
 * Plugins configurados:
 *
 * - Android Application Plugin → permite construir aplicaciones Android
 * - Kotlin Android Plugin → soporte para desarrollo en Kotlin
 * - Kotlin Compose Plugin → integración con Jetpack Compose
 * - Google Services Plugin → integración con servicios de Google
 *   como autenticación, Firebase o Google Sign-In
 */
plugins {

    /**
     * Plugin principal para aplicaciones Android.
     */
    alias(libs.plugins.android.application) apply false

    /**
     * Plugin que habilita el soporte de Kotlin en Android.
     */
    alias(libs.plugins.kotlin.android) apply false

    /**
     * Plugin que permite utilizar Jetpack Compose
     * dentro del proyecto.
     */
    alias(libs.plugins.kotlin.compose) apply false

    /**
     * Plugin de Google Services necesario para integrar
     * funcionalidades como Google Sign-In o Firebase.
     *
     * apply false indica que el plugin se declara aquí
     * pero se aplicará únicamente en los módulos que lo necesiten.
     */
    id("com.google.gms.google-services") version "4.4.4" apply false
}


/**
 * Bloque buildscript utilizado para declarar dependencias
 * necesarias durante el proceso de construcción del proyecto.
 *
 * En este caso se incluye el plugin de Google Services
 * como dependencia del sistema de build de Gradle.
 */
buildscript {

    dependencies {

        /**
         * Dependencia del plugin de Google Services.
         *
         * Este plugin permite integrar funcionalidades
         * relacionadas con los servicios de Google.
         */
        classpath("com.google.gms:google-services:4.4.2")
    }
}