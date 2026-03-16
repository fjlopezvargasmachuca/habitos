package com.example.habitos.data.entities

import androidx.room.Entity

/**
 * Entidad que representa un "like" dentro del sistema de foro.
 *
 * Esta tabla almacena la relación entre un usuario y un mensaje
 * al que ha dado "like". Cada registro indica que un usuario ha
 * marcado como favorito o le gusta un mensaje concreto.
 *
 * La clave primaria está compuesta por los campos `mensajeId`
 * y `usuarioId`. Esto garantiza que un mismo usuario no pueda
 * dar más de un "like" al mismo mensaje.
 *
 */
@Entity(
    tableName = "forum_likes",
    primaryKeys = ["mensajeId", "usuarioId"]
)
data class ForumLike(

    /**
     * Identificador del mensaje al que se ha dado "like".
     *
     * Este campo referencia el mensaje dentro de la tabla`forum_messages`.
     */
    val mensajeId: Int,

    /**
     * Identificador del usuario que ha dado el "like".
     *
     * Este campo permite saber qué usuario ha interactuado con el mensaje.
     */
    val usuarioId: Int
)