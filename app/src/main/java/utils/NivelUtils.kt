package com.example.habitos.util

/**
 * Calcula el nivel del usuario en función de los puntos acumulados.
 *
 * Este método forma parte del sistema de gamificación de la aplicación.
 * El nivel representa el progreso del usuario dentro de la app según
 * la cantidad de hábitos saludables que ha completado.
 *
 * Los niveles permiten motivar al usuario a continuar utilizando
 * la aplicación y mantener constancia en la realización de hábitos.
 *
 * Niveles implementados:
 *
 * - Diamond  → 250 puntos o más
 * - Platinum → entre 150 y 249 puntos
 * - Gold     → entre 100 y 149 puntos
 * - Silver   → entre 50 y 99 puntos
 * - Bronze   → menos de 50 puntos
 *
 * Parámetros:
 * - puntos → número total de puntos acumulados por el usuario
 *
 * Retorna:
 * - Una cadena de texto con el nombre del nivel correspondiente.
 */
fun calcularNivel(puntos: Int): String = when {

    /**
     * Nivel más alto del sistema de progreso.
     */
    puntos >= 250 -> "Diamond"

    /**
     * Nivel avanzado alcanzado tras acumular
     * una gran cantidad de puntos.
     */
    puntos >= 150 -> "Platinum"

    /**
     * Nivel intermedio que indica compromiso
     * con los hábitos saludables.
     */
    puntos >= 100 -> "Gold"

    /**
     * Nivel inicial que refleja constancia
     * en el uso de la aplicación.
     */
    puntos >= 50 -> "Silver"

    /**
     * Nivel básico asignado a los usuarios
     * que están comenzando en la aplicación.
     */
    else -> "Bronze"
}