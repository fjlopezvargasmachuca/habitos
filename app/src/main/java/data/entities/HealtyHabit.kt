package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Entidad que representa un hábito saludable dentro de la aplicación.
 *
 * Esta tabla almacena los distintos hábitos que pueden ser utilizados
 * por los usuarios para organizar sus rutinas diarias. Los hábitos
 * pueden pertenecer a diferentes categorías como salud física,
 * bienestar mental, nutrición o productividad.
 *
 * Cada hábito define una actividad que el usuario puede realizar
 * dentro del sistema de seguimiento de hábitos, pudiendo posteriormente
 * programarse en el calendario mediante otras entidades como
 * `ScheduledHabit` o `CalendarHabit`.
 *
 * La tabla asociada en la base de datos es `habitos`.
 */
@Entity(tableName = "habitos")
data class HealthyHabit(

    /**
     * Identificador único del hábito.
     *
     * Este valor se genera automáticamente al insertar el hábito en la base de datos.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Identificador del usuario propietario del hábito.
     *
     * Este campo permite que cada usuario tenga su propia lista
     * de hábitos almacenados en la base de datos.
     */
    val userId: Int,

    /**
     * Título o nombre del hábito.
     */
    val titulo: String,

    /**
     * Descripción detallada del hábito.
     */
    val descripcion: String,

    /**
     * Categoría a la que pertenece el hábito.
     */
    val categoria: String,

    /**
     * Duración estimada del hábito en minutos.
     */
    val duracionMin: Int = 0,

    /**
     * Nivel de dificultad del hábito.
     */
    val dificultad: String = ""
) {

    /**
     * URL o referencia de la imagen asociada al hábito.
     *
     * Está marcado con `@Ignore` para que Room no lo persista
     * en la base de datos, evitando así la necesidad de realizar
     * migraciones cuando se modifique este valor.
     */
    @Ignore
    var imagenUrl: String = ""
}