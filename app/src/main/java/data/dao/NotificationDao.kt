package com.example.habitos.data.dao

import androidx.room.*
import com.example.habitos.data.entities.ForumNotification

/**
 *
 * Este DAO trata de las notificaciones que se utilizan para informar a los usuarios sobre
 * interacciones relevantes dentro del foro, como por ejemplo:
 * - Respuestas a un hilo
 * - Nuevos mensajes
 * - Interacciones con sus publicaciones
 *
 * Esto es una mejora futura dentro del aplicativo en cuento se asigne la BBDD en firestore.
 *
 */
@Dao
interface NotificationDao {

    /**
     * Insertar una nueva notificación en la base de datos.
     *
     * @param notificacion Objeto ForumNotification que contiene la información
     * de la notificación que se desea almacenar.
     */
    @Insert
    suspend fun insertar(notificacion: ForumNotification)

    /**
     * Obtener todas las notificaciones asociadas a un usuario concreto.
     *
     * @param userId Identificador del usuario receptor de las notificaciones.
     * @return Lista de notificaciones correspondientes al usuario.
     */
    @Query(
        """
        SELECT * FROM forum_notifications
        WHERE receptorId = :userId
        ORDER BY fecha DESC
        """
    )
    suspend fun obtenerPorUsuario(userId: Int): List<ForumNotification>

    /**
     * Marcar una notificación como leída.
     *
     * @param id Identificador de la notificación que se desea marcar como leída.
     */
    @Query("UPDATE forum_notifications SET leida = 1 WHERE id = :id")
    suspend fun marcarLeida(id: Int)
}