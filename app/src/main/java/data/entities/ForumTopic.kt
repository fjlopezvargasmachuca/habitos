package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un tema principal dentro del foro.
 *
 * Los temas funcionan como categorías de discusión que agrupan
 * distintos hilos relacionados con una temática concreta.
 *
 * Dentro de cada tema los usuarios pueden crear hilos (`ForumThread`)
 * donde iniciar conversaciones y publicar mensajes (`ForumMessage`).
 *
 * La tabla asociada en la base de datos es `forum_topics`.
 */
@Entity(tableName = "forum_topics")
data class ForumTopic(

    /**
     * Identificador único del tema del foro.
     *
     * Este valor se genera automáticamente al insertar un nuevo tema en la base de datos.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Nombre del tema.
     */
    val nombre: String,

    /**
     * Descripción del tema.
     */
    val descripcion: String,

    /**
     * Indica si el tema está fijado en la parte superior del foro.
     *
     * - false → tema normal
     * - true → tema fijado (aparece destacado)
     */
    val fijado: Boolean = false
)