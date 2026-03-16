package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un mensaje publicado dentro de un hilo del foro.
 *
 * Cada registro de esta tabla corresponde a un mensaje escrito por un usuario
 * dentro de un hilo de discusión (`ForumThread`).
 *
 * Los mensajes permiten a los usuarios interactuar dentro del foro,
 * compartir experiencias sobre hábitos saludables, hacer preguntas
 * o responder a otros usuarios.
 *
 * La tabla asociada en la base de datos es `forum_messages`.
 */
@Entity(tableName = "forum_messages")
data class ForumMessage(

    /**
     * Identificador único del mensaje.
     *
     * Este valor se genera automáticamente al insertar el mensaje en la base de datos.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Identificador del hilo al que pertenece el mensaje.
     *
     * Permite relacionar el mensaje con la tabla `forum_threads`.
     */
    val threadId: Int,

    /**
     * Identificador del usuario que ha publicado el mensaje.
     *
     * Este campo permite saber qué usuario ha escrito el mensaje dentro del foro.
     */
    val autorId: Int,

    /**
     * Nombre del autor del mensaje.
     *
     * Se almacena directamente en el mensaje para facilitar
     * la visualización sin necesidad de consultar la tabla de usuarios.
     */
    val autorNick: String,

    /**
     * Provincia del autor del mensaje.
     *
     * Este dato se muestra en el foro como información adicional
     * sobre el usuario que ha publicado el mensaje.
     */
    val autorProvincia: String,

    /**
     * Contenido textual del mensaje publicado por el usuario.
     */
    val contenido: String,

    /**
     * Fecha de publicación del mensaje.
     */
    val fecha: Long = System.currentTimeMillis(),

    /**
     * Número total de likes que ha recibido el mensaje.
     *
     * Aunque los likes se almacenan en la tabla `forum_likes`,
     */
    val likes: Int = 0
)