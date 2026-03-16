package com.example.habitos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habitos.data.entities.ForumLike
import com.example.habitos.data.entities.ForumMessage
import com.example.habitos.data.entities.ForumThread
import com.example.habitos.data.entities.ForumTopic


/**
 * Este DAO gestiona las entidades del foro:
 * - Temas del foro (ForumTopic)
 * - Hilos dentro de cada tema (ForumThread)
 * - Mensajes dentro de cada hilo (ForumMessage)
 * - Likes asociados a los mensajes (ForumLike)
 *
 * Las funciones definidas permiten:
 * - Crear, editar y eliminar temas
 * - Crear y gestionar hilos
 * - Publicar y eliminar mensajes
 * - Gestionar el sistema de likes
 */
@Dao
interface ForumDao {

    // ───────────────────────── TEMAS ─────────────────────────

    /**
     * Obtener todos los temas del foro.
     *
     * @return Lista de temas disponibles en el foro.
     */
    @Query(
        """
        SELECT * FROM forum_topics
        ORDER BY fijado DESC, nombre ASC
        """
    )
    suspend fun obtenerTemas(): List<ForumTopic>

    /**
     * Insertar un nuevo tema en el foro.
     *
     * @param topic Objeto ForumTopic con los datos del nuevo tema.
     */
    @Insert
    suspend fun insertarTema(topic: ForumTopic)

    /**
     * Insertar un nuevo tema y devuelve el ID generado.
     *
     * @param topic Tema que se desea insertar.
     * @return id para el nuevo tema.
     */
    @Insert
    suspend fun insertarTemaReturnId(topic: ForumTopic): Long

    /**
     * Actualizar el nombre y la descripción de un tema existente.
     *
     * @param id Identificador del tema.
     * @param nombre Nuevo nombre del tema.
     * @param descripcion Nueva descripción del tema.
     */
    @Query(
        """
        UPDATE forum_topics
        SET nombre = :nombre, descripcion = :descripcion
        WHERE id = :id
        """
    )
    suspend fun actualizarTema(id: Int, nombre: String, descripcion: String)

    /**
     * Marcar o desmarca un tema como fijado.
     *
     * @param id Identificador del tema.
     * @param fijado Indica si el tema debe estar fijado o no.
     */
    @Query(
        """
        UPDATE forum_topics
        SET fijado = :fijado
        WHERE id = :id
        """
    )
    suspend fun fijarTema(id: Int, fijado: Boolean)

    /**
     * Eliminar todos los hilos asociados a un tema concreto.
     *
     * @param temaId id del tema cuyos hilos se eliminarán.
     */
    @Query("DELETE FROM forum_threads WHERE topicId = :temaId")
    suspend fun borrarHilosPorTema(temaId: Int)

    /**
     * Eliminar un tema del foro.
     *
     * @param id Identificador del tema que se desea eliminar.
     */
    @Query("DELETE FROM forum_topics WHERE id = :id")
    suspend fun borrarTema(id: Int)

    // ───────────────────────── HILOS ─────────────────────────

    /**
     * Obtener todos los hilos pertenecientes a un tema.
     *
     * @param topicId Identificador del tema.
     * @return Lista de hilos asociados al tema.
     */
    @Query("SELECT * FROM forum_threads WHERE topicId = :topicId ORDER BY fecha ASC")
    suspend fun obtenerHilos(topicId: Int): List<ForumThread>

    /**
     * Insertar un nuevo hilo dentro de un tema.
     *
     * @param thread Objeto ForumThread que representa el hilo.
     */
    @Insert
    suspend fun insertarHilo(thread: ForumThread)

    /**
     * Eliminar un hilo del foro.
     *
     * @param id Identificador del hilo.
     */
    @Query("DELETE FROM forum_threads WHERE id = :id")
    suspend fun borrarHilo(id: Int)

    /**
     * Actualizar el título de un hilo existente.
     *
     * @param id Identificador del hilo.
     * @param titulo Nuevo título del hilo.
     */
    @Query("UPDATE forum_threads SET titulo = :titulo WHERE id = :id")
    suspend fun actualizarHilo(id: Int, titulo: String)

    // ───────────────────────── MENSAJES ─────────────────────────

    /**
     * Obtener todos los mensajes pertenecientes a un hilo.
     *
     * @param threadId Identificador del hilo.
     * @return Lista de mensajes del hilo.
     */
    @Query("SELECT * FROM forum_messages WHERE threadId = :threadId ORDER BY fecha ASC")
    suspend fun obtenerMensajes(threadId: Int): List<ForumMessage>

    /**
     * Insertar un nuevo mensaje dentro de un hilo.
     *
     * @param msg Objeto ForumMessage con el contenido del mensaje.
     */
    @Insert
    suspend fun insertarMensaje(msg: ForumMessage)

    /**
     * Eliminar un mensaje del foro.
     *
     * @param id Identificador del mensaje.
     */
    @Query("DELETE FROM forum_messages WHERE id = :id")
    suspend fun borrarMensaje(id: Int)

    // ───────────────────────── LIKES ─────────────────────────

    /**
     * Insertar un nuevo "like" asociado a un mensaje.
     *
     * @param like Objeto ForumLike con los datos del like.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarLike(like: ForumLike)

    /**
     * Eliminar un like previamente registrado.
     *
     * @param mensajeId Identificador del mensaje.
     * @param usuarioId Identificador del usuario.
     */
    @Query("DELETE FROM forum_likes WHERE mensajeId = :mensajeId AND usuarioId = :usuarioId")
    suspend fun borrarLike(mensajeId: Int, usuarioId: Int)

    /**
     * Contar el número total de likes asociados a un mensaje.
     *
     * @param mensajeId Identificador del mensaje.
     * @return Número total de likes.
     */
    @Query("SELECT COUNT(*) FROM forum_likes WHERE mensajeId = :mensajeId")
    suspend fun contarLikes(mensajeId: Int): Int

    /**
     * Comprobar si un usuario concreto ha dado like a un mensaje.
     *
     * @param mensajeId Identificador del mensaje.
     * @param usuarioId Identificador del usuario.
     * @return true si el usuario ya ha dado like, false en caso contrario.
     */
    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM forum_likes
            WHERE mensajeId = :mensajeId AND usuarioId = :usuarioId
        )
        """
    )
    suspend fun usuarioHaDadoLike(mensajeId: Int, usuarioId: Int): Boolean

    /**
     * Actualizar el contador de likes almacenado en un mensaje.
     *
     * @param mensajeId Identificador del mensaje.
     * @param likes Número total actualizado de likes.
     */
    @Query("UPDATE forum_messages SET likes = :likes WHERE id = :mensajeId")
    suspend fun actualizarLikes(mensajeId: Int, likes: Int)
}