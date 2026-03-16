package com.example.habitos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habitos.data.entities.ScheduledHabit
import kotlinx.coroutines.flow.Flow

/**
 *
 * Este DAO permite realizar operaciones como:
 *
 * - Insertar hábitos programados
 * - Actualizar el estado de un hábito
 * - Consultar hábitos de un usuario en una fecha concreta
 * - Obtener todos los hábitos programados de un usuario
 * - Obtener estadísticas de hábitos completados
 *
 * La librería Room se encarga de generar automáticamente la implementación
 * de estas consultas SQL en tiempo de compilación.
 */
@Dao
interface ScheduledHabitDao {

    /**
     * Insertar un nuevo hábito programado en la base de datos.
     *
     * @param scheduled Objeto ScheduledHabit que contiene
     * los datos del hábito programado.
     * @return ID generado para el registro insertado.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(scheduled: ScheduledHabit): Long

    /**
     * Actualizar la información de un hábito programado existente.
     *
     * @param scheduled Objeto ScheduledHabit actualizado.
     */
    @Update
    suspend fun actualizar(scheduled: ScheduledHabit)

    /**
     * Obtener todos los hábitos programados para un usuario en una fecha concreta.
     *
     * @param userId Identificador del usuario.
     * @param fecha Fecha en formato String (YYYY-MM-DD).
     * @return Flow que emite una lista de hábitos programados.
     */
    @Query("SELECT * FROM scheduled_habits WHERE userId = :userId AND fecha = :fecha")
    fun obtenerParaDia(userId: Int, fecha: String): Flow<List<ScheduledHabit>>

    /**
     * Obtener todos los hábitos programados de un usuario.
     *
     * @param userId Identificador del usuario.
     * @return Flow con todos los hábitos programados.
     */
    @Query("SELECT * FROM scheduled_habits WHERE userId = :userId")
    fun obtenerTodos(userId: Int): Flow<List<ScheduledHabit>>

    /**
     * Obtener un hábito programado específico mediante su ID.
     *
     * @param id Identificador del hábito programado.
     * @return Objeto ScheduledHabit o null si no existe.
     */
    @Query("SELECT * FROM scheduled_habits WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ScheduledHabit?

    /**
     * Obtener los hábitos programados para un usuario en una fecha concreta en una única consulta.
     *
     * @param userId Identificador del usuario.
     * @param fecha Fecha consultada.
     * @return Lista de hábitos programados para ese día.
     */
    @Query("SELECT * FROM scheduled_habits WHERE userId = :userId AND fecha = :fecha")
    suspend fun obtenerParaDiaUnaVez(userId: Int, fecha: String): List<ScheduledHabit>

    /**
     * Contar el número total de hábitos completados por un usuario.
     *
     * @param userId Identificador del usuario.
     * @return Número de hábitos completados.
     */
    @Query("SELECT COUNT(*) FROM scheduled_habits WHERE userId = :userId AND completado = 1")
    suspend fun contarCompletados(userId: Int): Int

    /**
     * Contar el número total de hábitos programados que tiene un usuario.
     *
     * @param userId Identificador del usuario.
     * @return Número total de hábitos programados.
     */
    @Query("SELECT COUNT(*) FROM scheduled_habits WHERE userId = :userId")
    suspend fun contarTotales(userId: Int): Int
}