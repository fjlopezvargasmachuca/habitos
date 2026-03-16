package com.example.habitos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa un usuario registrado en la aplicación.
 *
 * Esta tabla almacena la información básica necesaria para identificar
 * y gestionar a los usuarios dentro del sistema.
 *
 * Además, el sistema contempla diferentes niveles de permisos mediante
 * el campo `role`.
 */
@Entity(tableName = "users")
data class User(

    /**
     * Identificador único del usuario.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Dirección de correo electrónico del usuario.
     */
    val email: String,

    /**
     * Contraseña del usuario.
     *
     * Puede ser `null` en el caso de usuarios que se hayan
     * registrado mediante autenticación externa como Google Sign-In.
     */
    val password: String?,

    /**
     * Nombre visible del usuario.
     */
    val displayName: String,

    /**
     * Nick o alias del usuario dentro de la aplicación.
     */
    val nick: String,

    /**
     * Provincia del usuario.
     */
    val provincia: String,

    /**
     * Rol del usuario dentro del sistema.
     *
     * Define los permisos que tiene el usuario en la aplicación.
     * Los valores habituales son:
     *
     * - "USER" → usuario estándar.
     * - "ADMIN" → administrador con permisos de moderación.
     * - "SUPERADMIN" → administrador principal con control total.
     */
    val role: String = "USER",

    /**
     * Puntuación acumulada por el usuario.
     *
     * Los puntos se obtienen al completar hábitos en el calendario
     * y se utilizan para generar el ranking de usuarios dentro de la aplicación.
     */
    val puntos: Int = 0,

    /**
     * Indica si el usuario ha sido baneado por un administrador.
     *
     * - false → usuario activo.
     * - true → usuario bloqueado y sin acceso a ciertas funcionalidades.
     */
    val baneado: Boolean = false
)