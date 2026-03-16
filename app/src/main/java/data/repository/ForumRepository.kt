package com.example.habitos.data.repository

import com.example.habitos.data.dao.ForumDao
import com.example.habitos.data.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio encargado de gestionar todas las operaciones relacionadas
 * con el sistema de foro de la aplicación.
 *
 * Este repositorio forma parte de la arquitectura MVVM y actúa como
 * capa intermedia entre los ViewModels y el acceso directo a la base
 * de datos (DAO).
 *
 * Sus principales responsabilidades son:
 *
 * - Centralizar las operaciones relacionadas con el foro.
 * - Gestionar la lógica de negocio antes de acceder a la base de datos.
 * - Abstraer el acceso al DAO para desacoplar la capa de presentación.
 *
 * El repositorio trabaja con las siguientes entidades:
 *
 * - ForumTopic → temas principales del foro
 * - ForumThread → hilos dentro de un tema
 * - ForumMessage → mensajes dentro de un hilo
 * - ForumLike → likes en los mensajes
 */
class ForumRepository(private val dao: ForumDao) {

    // ───────────────── TEMAS ─────────────────

    /**
     * Obtiene todos los temas del foro.
     *
     * @return Lista de temas ordenados según la consulta definida en el DAO.
     */
    suspend fun obtenerTemas() = dao.obtenerTemas()

    /**
     * Crea un nuevo tema dentro del foro.
     *
     * @param nombre Nombre del tema.
     * @param descripcion Descripción del tema.
     */
    suspend fun crearTema(nombre: String, descripcion: String) {
        dao.insertarTema(
            ForumTopic(
                nombre = nombre,
                descripcion = descripcion
            )
        )
    }

    /**
     * Actualiza el nombre y la descripción de un tema existente.
     *
     * @param id Identificador del tema.
     * @param nombre Nuevo nombre del tema.
     * @param descripcion Nueva descripción del tema.
     */
    suspend fun actualizarTema(id: Int, nombre: String, descripcion: String) {
        dao.actualizarTema(id, nombre, descripcion)
    }

    /**
     * Elimina un tema junto con todos los hilos asociados.
     *
     * Esta operación se realiza en dos pasos:
     * 1. Eliminar los hilos relacionados con el tema.
     * 2. Eliminar el propio tema.
     *
     * @param temaId Identificador del tema a eliminar.
     */
    suspend fun borrarTemaConHilos(temaId: Int) {
        dao.borrarHilosPorTema(temaId)
        dao.borrarTema(temaId)
    }

    /**
     * Marca o desmarca un tema como fijado en el foro.
     *
     * Los temas fijados suelen aparecer en la parte superior
     * del listado de temas.
     *
     * @param id Identificador del tema.
     * @param fijado Estado del tema (true = fijado).
     */
    suspend fun fijarTema(id: Int, fijado: Boolean) {
        dao.fijarTema(id, fijado)
    }

    // ───────────────── HILOS ─────────────────

    /**
     * Obtiene todos los hilos asociados a un tema concreto.
     *
     * @param topicId Identificador del tema.
     * @return Lista de hilos pertenecientes al tema.
     */
    suspend fun obtenerHilos(topicId: Int) = dao.obtenerHilos(topicId)

    /**
     * Crea un nuevo hilo dentro de un tema.
     *
     * @param topicId Identificador del tema.
     * @param titulo Título del hilo.
     * @param user Usuario que crea el hilo.
     */
    suspend fun crearHilo(topicId: Int, titulo: String, user: User) {
        dao.insertarHilo(
            ForumThread(
                topicId = topicId,
                titulo = titulo,
                autorId = user.id,
                autorNick = user.nick
            )
        )
    }

    /**
     * Actualiza el título de un hilo existente.
     *
     * @param hiloId Identificador del hilo.
     * @param nuevoTitulo Nuevo título del hilo.
     */
    suspend fun actualizarHilo(hiloId: Int, nuevoTitulo: String) {
        dao.actualizarHilo(hiloId, nuevoTitulo)
    }

    /**
     * Elimina un hilo junto con los mensajes asociados.
     *
     * @param hiloId Identificador del hilo a eliminar.
     */
    suspend fun borrarHiloConMensajes(hiloId: Int) {
        dao.borrarHilo(hiloId)
    }

    // ───────────────── MENSAJES ─────────────────

    /**
     * Obtiene todos los mensajes pertenecientes a un hilo.
     *
     * @param threadId Identificador del hilo.
     * @return Lista de mensajes ordenados cronológicamente.
     */
    suspend fun obtenerMensajes(threadId: Int) = dao.obtenerMensajes(threadId)

    /**
     * Inserta un nuevo mensaje dentro de un hilo.
     *
     * @param threadId Identificador del hilo.
     * @param texto Contenido del mensaje.
     * @param user Usuario que publica el mensaje.
     */
    suspend fun enviarMensaje(threadId: Int, texto: String, user: User) {
        dao.insertarMensaje(
            ForumMessage(
                threadId = threadId,
                autorId = user.id,
                autorNick = user.nick,
                autorProvincia = user.provincia,
                contenido = texto
            )
        )
    }

    /**
     * Elimina un mensaje del foro.
     *
     * @param id Identificador del mensaje.
     */
    suspend fun borrarMensaje(id: Int) {
        dao.borrarMensaje(id)
    }

    // ───────────────── LIKES ─────────────────

    /**
     * Comprueba si un usuario ha dado "like" a un mensaje.
     *
     * @param mensajeId Identificador del mensaje.
     * @param userId Identificador del usuario.
     * @return true si el usuario ya ha dado like.
     */
    suspend fun usuarioHaDadoLike(mensajeId: Int, userId: Int): Boolean {
        return dao.usuarioHaDadoLike(mensajeId, userId)
    }

    /**
     * Añade o elimina un like de un mensaje.
     *
     * Si el usuario ya había dado like se elimina,
     * en caso contrario se inserta uno nuevo.
     *
     * Después se recalcula el contador total de likes
     * del mensaje para mantenerlo actualizado.
     *
     * @param mensajeId Identificador del mensaje.
     * @param userId Identificador del usuario.
     */
    suspend fun toggleLike(mensajeId: Int, userId: Int) {
        if (dao.usuarioHaDadoLike(mensajeId, userId)) {
            dao.borrarLike(mensajeId, userId)
        } else {
            dao.insertarLike(ForumLike(mensajeId, userId))
        }

        // Actualización del contador total de likes
        val totalLikes = dao.contarLikes(mensajeId)
        dao.actualizarLikes(mensajeId, totalLikes)
    }

    // ───────────────── SEED INICIAL ─────────────────

    /**
     * Inserta datos iniciales en el foro si la base de datos
     * aún no contiene ningún tema.
     *
     * Este método permite que el foro tenga contenido inicial
     * cuando la aplicación se ejecuta por primera vez.
     */
    suspend fun seedInicialSiNecesario() = withContext(Dispatchers.IO) {

        // Si ya existen temas no se insertan datos iniciales
        if (dao.obtenerTemas().isNotEmpty()) return@withContext

        val temaId = dao.insertarTemaReturnId(
            ForumTopic(
                nombre = "Hábitos saludables",
                descripcion = "Comparte y aprende hábitos para una vida mejor",
                fijado = true
            )
        )

        dao.insertarHilo(
            ForumThread(
                topicId = temaId.toInt(),
                titulo = "Bienvenido al foro 👋",
                autorId = 1,
                autorNick = "Superadmin"
            )
        )
    }
}