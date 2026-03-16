package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un hábito programado dentro del calendario del usuario.
 *
 * Esta clase corresponde a la tabla `calendar_habits` en la base de datos
 * y almacena los hábitos que el usuario tiene previstos realizar en una
 * fecha concreta.
 *
 * Cada registro representa una instancia concreta de un hábito dentro del
 * calendario, permitiendo realizar seguimiento diario del progreso del usuario.
 *
 * Esta entidad se utiliza principalmente en el módulo de calendario de la
 * aplicación, donde el usuario puede visualizar, completar o gestionar
 * los hábitos asignados a cada día.
 */
@Entity(tableName = "calendar_habits")
data class CalendarHabit(

    /**
     * Identificador único del hábito dentro del calendario.
     *
     * Se genera automáticamente por la base de datos cuando se inserta un nuevo registro.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Identificador del usuario al que pertenece este hábito
     * dentro del calendario.
     *
     * Permite separar los hábitos del calendario de cada usuario.
     */
    val userId: Int,

    /**
     * Identificador del hábito base al que hace referencia
     * este registro dentro del calendario.
     */
    val habitId: Int,

    /**
     * Fecha en la que el hábito está programado.
     *
     * Se almacena como cadena de texto normalmente en formato: YYYY-MM-DD
     */
    val fecha: String,

    /**
     * Título del hábito que se mostrará en el calendario.
     *
     * Este campo permite visualizar rápidamente la acción que el usuario debe realizar ese día.
     */
    val titulo: String,

    /**
     * Descripción detallada del hábito.
     *
     * Puede incluir información adicional o recomendaciones sobre cómo realizar el hábito.
     */
    val descripcion: String,

    /**
     * URL o ruta de la imagen asociada al hábito.
     *
     * Se utiliza para mostrar un icono o imagen representativa
     * del hábito dentro de la interfaz de usuario.
     */
    val imagenUrl: String,

    /**
     * Estado actual del hábito dentro del calendario.
     *
     * Valores posibles:
     * - PENDIENTE
     * - HECHO
     *
     * Este estado permite llevar el control del progreso diario del usuario.
     */
    val estado: String = "PENDIENTE",

    /**
     * Indica si los puntos asociados a este hábito ya han sido aplicados.
     *
     * Este campo se utiliza para evitar que el usuario pueda obtener
     * puntos múltiples veces modificando repetidamente el estado del hábito.
     */
    val puntosAplicados: Boolean = false
)