package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una notificación del sistema de foro.
 *
 * Esto es para una implementación futura donde la BBDD se realice mediante firestore
 *
 */
@Entity(tableName = "forum_notifications")
data class ForumNotification(

    /**
     * Identificador único de la notificación.
     *
     * Se genera automáticamente cuando se inserta el registro en la base de datos.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Identificador del usuario que recibe la notificación.
     *
     * Este campo permite relacionar la notificación con la tabla de usuarios (`users`).
     */
    val receptorId: Int,

    /**
     * Texto del mensaje de la notificación.
     *
     * Describe el evento ocurrido dentro del foro que ha generado la notificación.
     */
    val mensaje: String,

    /**
     * Indica si la notificación ha sido leída por el usuario.
     *
     * - false → notificación pendiente de leer
     * - true → notificación ya revisada
     */
    val leida: Boolean = false,

    /**
     * Fecha de creación de la notificación.
     */
    val fecha: Long = System.currentTimeMillis()
)