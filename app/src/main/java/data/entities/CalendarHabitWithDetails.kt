package com.example.habitos.data.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habitos.data.entities.HealthyHabit

/**
 * Clase de proyección que combina información de las entidades
 * CalendarHabit y HealthyHabit mediante una relación definida
 * con la anotación @Relation de Room.
 *
 * En este caso permite obtener:
 * - La información del hábito programado en el calendario (CalendarHabit)
 * - Los detalles del hábito original almacenado en el catálogo (HealthyHabit)
 *
 * La anotación @Embedded inserta directamente los campos de CalendarHabit
 * dentro del objeto resultante, mientras que @Relation establece la relación
 * entre el campo habitId de CalendarHabit y el campo id de HealthyHabit.
 */
data class CalendarHabitWithDetails(

    /**
     * Información del hábito programado en el calendario.
     */
    @Embedded
    val calendarHabit: CalendarHabit,

    /**
     * Información detallada del hábito original asociado.
     */
    @Relation(
        parentColumn = "habitId",
        entityColumn = "id"
    )
    val habit: HealthyHabit
)