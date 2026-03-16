package com.example.habitos.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Representa una insignia (badge) dentro del sistema de gamificación
 * de la aplicación.
 *
 * Cada insignia está asociada a un nivel alcanzado por el usuario
 * en función de los puntos obtenidos al completar hábitos.
 *
 * Propiedades de la insignia:
 *
 * - nombre → nombre del nivel alcanzado
 * - icono → icono representativo del nivel
 * - color → color visual utilizado para mostrar la insignia en la interfaz
 *
 * Estas insignias se utilizan principalmente en el perfil del usuario
 * y en otras pantallas donde se quiera mostrar el progreso del usuario
 * dentro del sistema de gamificación.
 */
data class Badge(
    val nombre: String,
    val icono: ImageVector,
    val color: Color
)


/**
 * Devuelve la insignia correspondiente al usuario en función
 * de los puntos acumulados.
 *
 * Este método forma parte del sistema de gamificación de la aplicación,
 * incentivando al usuario a completar hábitos para subir de nivel
 * y obtener nuevas insignias.
 *
 * Niveles implementados:
 *
 * - Leyenda → 350 puntos o más
 * - Ejemplar → 200 a 349 puntos
 * - Comprometido → 100 a 199 puntos
 * - Constante → 50 a 99 puntos
 * - Principiante → menos de 50 puntos
 *
 * Cada nivel tiene asociado un icono y un color para
 * facilitar su identificación visual en la interfaz.
 */
fun obtenerInsignia(puntos: Int): Badge {

    return when {

        /**
         * Nivel más alto del sistema de gamificación.
         */
        puntos >= 350 -> Badge(
            "Leyenda",
            Icons.Filled.Diamond,
            Color(0xFF00BCD4)
        )

        /**
         * Nivel avanzado alcanzado tras acumular una gran
         * cantidad de hábitos completados.
         */
        puntos >= 200 -> Badge(
            "Ejemplar",
            Icons.Filled.EmojiEvents,
            Color(0xFFFFD700)
        )

        /**
         * Nivel intermedio que indica constancia en la
         * realización de hábitos saludables.
         */
        puntos >= 100 -> Badge(
            "Comprometido",
            Icons.Filled.Star,
            Color(0xFFC0C0C0)
        )

        /**
         * Nivel inicial que indica que el usuario ya ha
         * comenzado a mantener hábitos de forma regular.
         */
        puntos >= 50 -> Badge(
            "Constante",
            Icons.Filled.Whatshot,
            Color(0xFFFF5722)
        )

        /**
         * Nivel básico asignado a los usuarios que están
         * comenzando a utilizar la aplicación.
         */
        else -> Badge(
            "Principiante",
            Icons.Filled.Eco,
            Color(0xFF4CAF50)
        )
    }
}