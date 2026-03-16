package com.example.habitos.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Tema principal de la aplicación Habitos.
 *
 * Esta función define el tema visual global de la aplicación utilizando
 * Material Design 3. Se encarga de aplicar los colores, tipografía y
 * estilos que serán utilizados por todas las pantallas de la aplicación.
 *
 * Características del tema:
 * - Soporte para modo claro y modo oscuro
 * - Compatibilidad con colores dinámicos de Android 12+
 * - Configuración automática de la barra de estado del sistema
 *
 * Este tema envuelve todo el contenido de la aplicación mediante
 * MaterialTheme para que los componentes de Jetpack Compose utilicen
 * automáticamente los estilos definidos.
 */
@Composable
fun HabitosAppTheme(
    dynamicColor: Boolean = true,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    /**
     * Esquema de colores que se aplicará al tema de la aplicación.
     *
     * Se determina según las siguientes condiciones:
     *
     * - Si el dispositivo soporta colores dinámicos (Android 12+),
     *   se utilizan los colores generados a partir del fondo del sistema.
     * - En caso contrario se utilizan esquemas de colores definidos
     *   manualmente para modo claro u oscuro.
     */
    val colorScheme =
        when {

            /**
             * Uso de colores dinámicos disponibles en Android 12 (API 31) o superior.
             */
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

                val context = LocalContext.current

                if (darkTheme)
                    dynamicDarkColorScheme(context)
                else
                    dynamicLightColorScheme(context)
            }

            /**
             * Esquema de colores para modo oscuro.
             */
            darkTheme -> darkColorScheme()

            /**
             * Esquema de colores para modo claro.
             */
            else -> lightColorScheme()
        }


    /**
     * Vista actual del sistema.
     * Se utiliza para acceder a la ventana de la aplicación y
     * modificar propiedades visuales del sistema.
     */
    val view = LocalView.current


    /**
     * Configuración de la barra de estado del sistema.
     *
     * Se ejecuta únicamente cuando la vista no está en modo
     * de previsualización dentro de Android Studio.
     */
    if (!view.isInEditMode) {

        SideEffect {

            /**
             * Acceso a la ventana de la actividad actual.
             */
            val window = (view.context as Activity).window

            /**
             * Se establece el color de la barra de estado
             * utilizando el color principal del tema.
             */
            window.statusBarColor = colorScheme.primary.toArgb()

            /**
             * Configura la apariencia de los iconos de la barra
             * de estado dependiendo del modo claro u oscuro.
             */
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }


    /**
     * Aplicación del tema Material Design a todo el contenido
     * de la aplicación.
     *
     * A partir de este punto, todos los componentes de Compose
     * utilizarán automáticamente:
     *
     * - El esquema de colores definido
     * - La tipografía de la aplicación
     * - Los estilos globales del tema
     */
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}