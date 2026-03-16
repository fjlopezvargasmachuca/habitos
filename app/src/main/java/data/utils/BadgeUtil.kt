package com.example.habitos.data.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Clase de modelo que representa una insignia o badge dentro
 * del sistema de gamificación de la aplicación.
 *
 * Las insignias se utilizan para reconocer el progreso del usuario
 * en función de los puntos acumulados al completar hábitos.
 *
 * Cada insignia incluye:
 * - nombre → título de la insignia
 * - icono → icono visual asociado
 * - color → color representativo de la insignia
 *
 * Estas insignias se utilizan principalmente en:
 * - el perfil del usuario
 * - el ranking
 * - elementos visuales de progreso
 */
data class Badge(

    /**
     * Nombre de la insignia.
     */
    val nombre: String,

    /**
     * Icono asociado a la insignia.
     *
     * Se utiliza para mostrar visualmente el nivel alcanzado
     * por el usuario dentro de la aplicación.
     */
    val icono: ImageVector,

    /**
     * Color representativo de la insignia.
     *
     * Permite diferenciar visualmente los distintos niveles de progreso del usuario.
     */
    val color: Color
)

/**
 * Determina la insignia que corresponde a un usuario
 * en función de los puntos acumulados.
 *
 * @param puntos Número total de puntos del usuario.
 * @return Objeto `Badge` correspondiente al nivel alcanzado.
 */
fun obtenerInsignia(puntos: Int): Badge {

    return when {

        // Nivel máximo alcanzado por usuarios muy activos
        puntos >= 350 -> Badge(
            "Leyenda",
            Icons.Filled.Diamond,
            Color(0xFF00BCD4)
        )

        // Usuario con alto nivel de compromiso
        puntos >= 200 -> Badge(
            "Ejemplar",
            Icons.Filled.EmojiEvents,
            Color(0xFFFFD700)
        )

        // Usuario con progreso consolidado
        puntos >= 100 -> Badge(
            "Comprometido",
            Icons.Filled.Star,
            Color(0xFFC0C0C0)
        )

        // Usuario que mantiene cierta constancia
        puntos >= 50 -> Badge(
            "Constante",
            Icons.Filled.Whatshot,
            Color(0xFFFF5722)
        )

        // Nivel inicial para nuevos usuarios
        else -> Badge(
            "Principiante",
            Icons.Filled.Eco,
            Color(0xFF4CAF50)
        )
    }
}
