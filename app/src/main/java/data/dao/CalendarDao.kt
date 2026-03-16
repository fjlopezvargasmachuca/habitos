package com.example.habitos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.habitos.data.entities.CalendarHabit
import androidx.room.OnConflictStrategy

/**
 * Las funciones definidas en este DAO son:
 * - Insertar hábitos en el calendario
 * - Consultar hábitos por fecha
 * - Actualizar estados de realización
 * - Eliminar hábitos
 * - Gestionar la aplicación de puntos asociados a los hábitos
 */
@Dao
interface CalendarDao {

    /**
     * Insertar un nuevo hábito en el calendario.
     *
     * @param habit Objeto CalendarHabit que contiene los dato del hábito que se desea insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(habit: CalendarHabit)

    /**
     * Obtener un hábito del calendario a partir de su ID.
     *
     * @param id Identificador único del hábito en el calendario.
     * @return Devuelve el objeto CalendarHabit correspondiente o null si no existe.
     */
    @Query("SELECT * FROM calendar_habits WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): CalendarHabit?

    /**
     * Cargar todos los hábitos programados para una fecha concreta.
     *
     * @param fecha Fecha en formato String (YYYY-MM-DD).
     * @return Lista de hábitos programados para esa fecha.
     */
    @Query("SELECT * FROM calendar_habits WHERE fecha = :fecha AND userId = :userId")
    suspend fun cargarHabitosPorFecha(fecha: String, userId: Int): List<CalendarHabit>

    /**
     * Eliminar un hábito del calendario utilizando su ID.
     *
     * @param id Identificador del hábito que se desea eliminar.
     */
    @Query("DELETE FROM calendar_habits WHERE id = :id")
    suspend fun eliminarPorId(id: Int)

    /**
     * Actualizar el estado de realización de un hábito.
     *
     * @param id del hábito que se desea actualizar.
     * @param estado Nuevo estado que tendrá el hábito.
     */
    @Query("UPDATE calendar_habits SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    /**
     * Marcar que los puntos asociados a un hábito ya han sido aplicados.
     *
     * @param id Identificador del hábito.
     */
    @Query(
        """
        UPDATE calendar_habits 
        SET puntosAplicados = 1 
        WHERE id = :id
        """
    )
    suspend fun marcarPuntosAplicados(id: Int)

    /**
     * Recuperar todos los hábitos almacenados en el calendario.
     *
     * @return Lista completa de hábitos registrados en el calendario.
     */
    @Query("SELECT * FROM calendar_habits WHERE userId = :userId")
    suspend fun obtenerTodos(userId: Int): List<CalendarHabit>

    /**
     * Marcar automáticamente como pendientes los hábitos
     * que pertenecen a fechas pasadas y que no fueron completados.
     *
     * @param hoy Fecha actual utilizada como referencia
     * para determinar qué hábitos pertenecen al pasado.
     */
    @Query(
        """
        UPDATE calendar_habits
        SET estado = 'PENDIENTE'
        WHERE fecha < :hoy AND estado != 'HECHO'
        """
    )
    suspend fun marcarNoHechosPasados(hoy: String)

    /**
     * Actualizar el estado de un hábito y marca automáticamente
     * que los puntos han sido aplicados.
     *
     * @param id Identificador del hábito.
     * @param estado Nuevo estado del hábito.
     */
    @Query(
        """
        UPDATE calendar_habits
        SET estado = :estado, puntosAplicados = 1
        WHERE id = :id
        """
    )
    suspend fun actualizarEstadoYPuntos(id: Int, estado: String)

}