package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un hilo de discusión dentro del foro.
 *
 * Un hilo pertenece a un tema concreto (`ForumTopic`) y sirve como
 * contenedor para los mensajes publicados por los usuarios (`ForumMessage`).
 *
 * Dentro de cada hilo los usuarios pueden debatir sobre hábitos saludables,
 * compartir experiencias o plantear dudas relacionadas con el tema del foro.
 *
 * Los mensajes asociados al hilo se almacenan en la tabla `forum_messages`.
 */
@Entity(tableName = "forum_threads")
data class ForumThread(

    /**
     * Identificador único del hilo.
     *
     * Se genera automáticamente al insertar el registro en la base de datos.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Identificador del tema al que pertenece el hilo.
     *
     * Este campo permite relacionar el hilo con la tabla `forum_topics`.
     */
    val topicId: Int,

    /**
     * Título del hilo de discusión.
     *
     * Describe brevemente el contenido o pregunta que se tratará dentro del hilo.
     */
    val titulo: String,

    /**
     * Identificador del usuario que creó el hilo.
     */
    val autorId: Int,

    /**
     * Nick visible del autor del hilo.
     */
    val autorNick: String,

    /**
     * Fecha de creación del hilo.
     */
    val fecha: Long = System.currentTimeMillis()
)