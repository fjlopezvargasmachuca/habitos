package com.example.habitos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.habitos.data.entities.User
import androidx.room.OnConflictStrategy

@Dao
interface UserDao {

    /**
     * Insertar un nuevo usuario en la base de datos.
     *
     * @param user Objeto User que contiene la información del usuario.
     * @return ID generado para el nuevo usuario insertado.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(user: User): Long

    /**
     * Obtener un usuario a partir de su dirección de correo electrónico.
     *
     * @param email Email del usuario.
     * @return Objeto User si existe, o null si no se encuentra.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun obtenerPorEmail(email: String): User?

    /**
     * Obtener un usuario mediante su identificador único.
     *
     * @param id Identificador del usuario.
     * @return Objeto User correspondiente o null si no existe.
     */
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): User?

    /**
     * Obtener todos los usuarios registrados en la aplicación.
     *
     * @return Lista de usuarios ordenados por puntuación.
     */
    @Query("SELECT * FROM users ORDER BY puntos DESC")
    suspend fun obtenerTodos(): List<User>

    /**
     * Actualizar los datos del perfil de un usuario.
     *
     * @param id Identificador del usuario.
     * @param nick Nuevo nombre de usuario.
     * @param provincia Nueva provincia asociada al usuario.
     */
    @Query(
        """
        UPDATE users 
        SET nick = :nick, provincia = :provincia 
        WHERE id = :id
        """
    )
    suspend fun actualizarPerfil(id: Int, nick: String, provincia: String)

    /**
     * Marcar un usuario como baneado dentro del sistema.
     *
     * @param id Identificador del usuario a banear.
     */
    @Query("UPDATE users SET baneado = 1 WHERE id = :id")
    suspend fun banear(id: Int)

    @Query("UPDATE users SET baneado = 0 WHERE id = :id")
    suspend fun desbanear(id: Int)

    @Query("UPDATE users SET baneado = :estado WHERE id = :id")
    suspend fun actualizarEstadoBaneo(id: Int, estado: Boolean)

    /**
     * Incrementar la puntuación de un usuario.
     *
     * @param id Identificador del usuario.
     * @param delta Cantidad de puntos que se deben añadir.
     */
    @Query("UPDATE users SET puntos = puntos + :delta WHERE id = :id")
    suspend fun sumarPuntos(id: Int, delta: Int)

    @Query("UPDATE users SET role = :nuevoRol WHERE id = :id")
    suspend fun actualizarRol(id: Int, nuevoRol: String)
}