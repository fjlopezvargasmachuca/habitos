package com.example.habitos.data.repository

import com.example.habitos.data.dao.HabitDao
import com.example.habitos.data.entities.HealthyHabit
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con los hábitos saludables de la aplicación.
 *
 * Sus responsabilidades principales son:
 *
 * - Centralizar el acceso a los datos de los hábitos.
 * - Abstraer las operaciones del DAO para desacoplar la lógica
 *   de negocio de la capa de persistencia.
 * - Proporcionar métodos de consulta, inserción y eliminación
 *   de hábitos.
 *
 */
class HabitRepository(private val dao: HabitDao) {

    /**
     * Insertar un nuevo hábito en la base de datos.
     *
     * @param h Objeto `HealthyHabit` que representa el hábito a insertar.
     */
    suspend fun insertar(h: HealthyHabit) {
        dao.insertarHabito(h)
    }

    /**
     * Insertar múltiples hábitos en la base de datos.
     *
     * @param lista Lista de hábitos que se desean almacenar.
     */
    suspend fun insertarMuchos(lista: List<HealthyHabit>) {
        dao.insertarMuchos(lista)
    }

    /**
     * Obtener todos los hábitos almacenados para un usuario concreto.
     *
     * @param userId Identificador del usuario propietario de los hábitos.
     * @return Flujo de lista de hábitos.
     */
    fun obtenerTodos(userId: Int): Flow<List<HealthyHabit>> =
        dao.obtenerTodos(userId)

    /**
     * Obtener todos los hábitos de un usuario mediante una consulta puntual.
     *
     * @param userId Identificador del usuario.
     * @return Lista de hábitos.
     */
    suspend fun obtenerTodosUnaVez(userId: Int): List<HealthyHabit> =
        dao.obtenerTodosUnaVez(userId)

    /**
     * Obtener los hábitos pertenecientes a una categoría concreta para un usuario específico.
     *
     * @param cat Categoría de hábitos solicitada.
     * @param userId Identificador del usuario.
     * @return Flujo de lista de hábitos filtrados por categoría.
     */
    fun obtenerPorCategoria(cat: String, userId: Int): Flow<List<HealthyHabit>> =
        dao.obtenerPorCategoria(cat, userId)

    /**
     * Obtener los hábitos de una categoría concreta mediante
     *
     * @param cat Categoría de hábitos.
     * @param userId Identificador del usuario.
     * @return Lista de hábitos pertenecientes a esa categoría.
     */
    suspend fun obtenerPorCategoriaUnaVez(cat: String, userId: Int): List<HealthyHabit> =
        dao.obtenerPorCategoriaUnaVez(cat, userId)

    /**
     * Eliminar un hábito de la base de datos utilizando su identificador.
     *
     * @param id Identificador del hábito que se desea eliminar.
     */
    suspend fun borrarHabito(id: Int) {
        dao.borrarPorId(id)
    }

    /**
     * Comprobar si un hábito ya existe para un usuario concreto.
     *
     * @param titulo Título del hábito.
     * @param userId Identificador del usuario.
     *
     * @return `true` si el hábito ya existe, `false` en caso contrario.
     */
    suspend fun existeHabitoUsuario(titulo: String, userId: Int): Boolean {
        return dao.contarHabitoUsuario(titulo, userId) > 0
    }

    /**
     * Obtiene un hábito específico a partir de su identificador.
     *
     * @param id Identificador del hábito.
     * @return Objeto `HealthyHabit` correspondiente o `null` si no existe.
     */
    suspend fun obtenerPorId(id: Int): HealthyHabit? =
        dao.obtenerPorId(id)
}