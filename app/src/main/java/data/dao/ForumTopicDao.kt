package com.example.habitos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.habitos.data.entities.ForumTopic

/**
 * Las funciones definidas en este DAO son:
 * - Insertar nuevos temas
 * - Consultar los temas existentes
 * - Eliminar temas del foro
 */
@Dao
interface ForumTopicDao {

    /**
     * Insertar un nuevo tema en el foro.
     *
     * @param topic Objeto ForumTopic que contiene la información del tema
     * que se desea insertar en la base de datos.
     */
    @Insert
    suspend fun insertarTema(topic: ForumTopic)

    /**
     * Obtener todos los temas disponibles en el foro.
     *
     * @return Lista de objetos ForumTopic almacenados en la base de datos.
     */
    @Query("SELECT * FROM forum_topics ORDER BY nombre ASC")
    suspend fun obtenerTemas(): List<ForumTopic>

    /**
     * Eliminar un tema del foro utilizando su identificador.
     *
     * @param id Identificador del tema que se desea eliminar.
     */
    @Query("DELETE FROM forum_topics WHERE id = :id")
    suspend fun borrarTema(id: Int)
}
