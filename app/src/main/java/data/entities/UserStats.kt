package com.example.habitos.data.entities

/**
 * Clase de modelo que representa un resumen de estadísticas de actividad
 * del usuario dentro de la aplicación de hábitos saludables.
 *
 * Esta clase no es una entidad de base de datos, sino una estructura
 * de datos auxiliar utilizada para mostrar información agregada en
 * la pantalla de perfil o en secciones estadísticas de la aplicación.
 *
 * Información que proporciona:
 *
 * - completados → número de hábitos que el usuario ha realizado correctamente.
 * - totales → número total de hábitos registrados para el usuario.
 * - porcentaje → porcentaje de hábitos completados respecto al total.
 *
 * Este cálculo permite mostrar al usuario su progreso en la adopción
 * de hábitos saludables.
 */
data class UserStats(

    /**
     * Número de hábitos completados por el usuario.
     *
     * Corresponde al número de registros en `calendar_habits`
     * cuyo estado es "HECHO".
     */
    val completados: Int,

    /**
     * Número total de hábitos registrados para el usuario.
     *
     * Incluye tanto hábitos completados como pendientes o fallidos.
     */
    val totales: Int
) {

    /**
     * Porcentaje de hábitos completados respecto al total.
     *
     * Se calcula automáticamente a partir de los valores
     * `completados` y `totales`.
     *
     * Si no existen hábitos registrados (`totales = 0`),
     * el porcentaje se devuelve como 0 para evitar divisiones
     * por cero.
     */
    val porcentaje: Int
        get() = if (totales <= 0) 0 else (completados * 100 / totales)
}