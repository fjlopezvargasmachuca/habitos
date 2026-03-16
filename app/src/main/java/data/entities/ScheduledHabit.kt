package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un hábito programado para un usuario en una fecha concreta.
 *
 * Esta tabla permite registrar qué hábitos debe realizar un usuario
 * en un día determinado dentro del sistema de seguimiento de hábitos.
 *
 * A diferencia de la entidad `HealthyHabit`, que define el hábito de forma general,
 * esta entidad representa la planificación concreta de ese hábito para un usuario
 * en una fecha específica del calendario.
 *
 * Cada registro relaciona:
 * - un usuario (`userId`)
 * - un hábito (`habitId`)
 * - una fecha concreta (`fecha`)
 *
 * También permite registrar si el hábito ha sido completado o no por el usuario.
 *
 * La tabla asociada en la base de datos es `scheduled_habits`.
 */
@Entity(tableName = "scheduled_habits")
data class ScheduledHabit(

    /**
     * Identificador único del hábito programado.
     *
     * Este valor se genera automáticamente cuando el registro se inserta en la base de datos.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Identificador del usuario al que pertenece el hábito programado.
     */
    val userId: Int,

    /**
     * Identificador del hábito que se ha programado.
     */
    val habitId: Int,

    /**
     * Fecha en la que el usuario debe realizar el hábito.
     *
     * El formato utilizado es `YYYY-MM-DD`.
     */
    val fecha: String,

    /**
     * Indica si el hábito ha sido completado por el usuario.
     *
     * - false → el hábito aún no se ha completado
     * - true → el hábito ha sido realizado correctamente
     */
    val completado: Boolean = false
)