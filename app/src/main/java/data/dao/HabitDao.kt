package com.example.habitos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.habitos.data.entities.HealthyHabit
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy

/**
 * Las operaciones definidas en este DAO permiten:
 * - Insertar nuevos hábitos
 * - Obtener hábitos almacenados
 * - Filtrar hábitos por categoría
 * - Eliminar hábitos
 * - Consultar hábitos individuales
 */
@Dao
interface HabitDao {

    /**
     * Insertar un nuevo hábito en la base de datos.
     *
     * @param h Objeto HealthyHabit que se desea almacenar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarHabito(h: HealthyHabit)

    /**
     * Obtener todos los hábitos de un usuario concreto.
     *
     * @param userId Identificador del usuario.
     * @return Flow que emite la lista de hábitos del usuario.
     */
    @Query("SELECT * FROM habitos WHERE userId = :userId ORDER BY id DESC")
    fun obtenerTodos(userId: Int): Flow<List<HealthyHabit>>

    /**
     * Insertar múltiples hábitos en la base de datos.
     *
     * @param lista Lista de objetos HealthyHabit a insertar.
     */
    @Insert
    suspend fun insertarMuchos(lista: List<HealthyHabit>)

    /**
     * Obtener todos los hábitos pertenecientes a una categoría específica
     * para un usuario concreto.
     */
    @Query("SELECT * FROM habitos WHERE categoria = :cat AND userId = :userId ORDER BY id DESC")
    fun obtenerPorCategoria(cat: String, userId: Int): Flow<List<HealthyHabit>>

    /**
     * Obtener los hábitos de una categoría concreta una única vez.
     */
    @Query("SELECT * FROM habitos WHERE categoria = :cat AND userId = :userId")
    suspend fun obtenerPorCategoriaUnaVez(cat: String, userId: Int): List<HealthyHabit>

    /**
     * Obtener todos los hábitos almacenados de un usuario.
     */
    @Query("SELECT * FROM habitos WHERE userId = :userId")
    suspend fun obtenerTodosUnaVez(userId: Int): List<HealthyHabit>

    /**
     * Eliminar un hábito utilizando su identificador.
     */
    @Query("DELETE FROM habitos WHERE id = :id")
    suspend fun borrarPorId(id: Int)

    /**
     * Obtener un hábito específico mediante su ID.
     */
    @Query("SELECT * FROM habitos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): HealthyHabit?

    /**
     * Comprueba si un hábito ya existe para un usuario concreto.
     *
     * Se utiliza para evitar guardar hábitos duplicados.
     *
     * @param titulo título del hábito
     * @param userId identificador del usuario
     * @return número de coincidencias encontradas
     */
    @Query("SELECT COUNT(*) FROM habitos WHERE titulo = :titulo AND userId = :userId")
    suspend fun contarHabitoUsuario(titulo: String, userId: Int): Int
}