package com.example.habitos.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitos.data.entities.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import com.example.habitos.data.database.HabitDatabase

/**
 * ViewModel encargado de gestionar toda la lógica relacionada con
 * la autenticación y gestión de usuarios dentro de la aplicación.
 *
 * Este ViewModel sigue la arquitectura MVVM y actúa como intermediario
 * entre la interfaz de usuario (Jetpack Compose) y los datos de usuario.
 *
 * Funcionalidades principales implementadas:
 *
 * - Inicio de sesión manual mediante email y contraseña
 * - Registro de nuevos usuarios
 * - Inicio de sesión con Google
 * - Gestión del usuario autenticado
 * - Sistema de puntos para gamificación
 * - Moderación de usuarios (banear / desbanear / cambiar roles)
 * - Cierre de sesión
 *
 * Los datos se almacenan actualmente en memoria utilizando una lista
 * de usuarios simulada. Esto permite demostrar el funcionamiento de
 * la lógica de autenticación sin depender de una base de datos externa.
 *
 * La comunicación con la interfaz se realiza mediante StateFlow,
 * lo que permite que las pantallas Compose reaccionen automáticamente
 * cuando cambian los datos del usuario o la lista de usuarios.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // ───────────── BASE DE DATOS ─────────────

    /**
     * DAO de usuarios para persistir información en Room.
     */
    private val userDao = HabitDatabase
        .getDatabase(application)
        .userDao()


    // ───────────── GENERADOR DE ID ─────────────

    private val idGenerator = AtomicInteger(1)


    // ───────────── USUARIOS EN MEMORIA ─────────────

    private val usuariosLocales = mutableListOf<User>()


    // ───────────── STATE FLOWS ─────────────

    private val _usuarios = MutableStateFlow<List<User>>(emptyList())
    val usuarios: StateFlow<List<User>> = _usuarios

    private val _usuarioActual = MutableStateFlow<User?>(null)
    val usuarioActual: StateFlow<User?> = _usuarioActual

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError


    // ───────────── USUARIOS DEMO ─────────────

    private val superAdmin = User(
        id = idGenerator.getAndIncrement(),
        email = "demo1@demo.com",
        password = "1234",
        displayName = "Superadmin",
        nick = "Superadmin",
        provincia = "Sevilla",
        role = "SUPERADMIN",
        puntos = 120,
        baneado = false
    )

    private val adminDemo = User(
        id = idGenerator.getAndIncrement(),
        email = "admin@demo.com",
        password = "admin1234",
        displayName = "Admin",
        nick = "Admin",
        provincia = "Madrid",
        role = "ADMIN",
        puntos = 80,
        baneado = false
    )

    private val userDemo1 = User(
        id = idGenerator.getAndIncrement(),
        email = "user1@demo.com",
        password = "1234",
        displayName = "Usuario Uno",
        nick = "user1",
        provincia = "Valencia",
        role = "USER",
        puntos = 10,
        baneado = false
    )

    private val userDemo2 = User(
        id = idGenerator.getAndIncrement(),
        email = "user2@demo.com",
        password = "1234",
        displayName = "Usuario Dos",
        nick = "user2",
        provincia = "Barcelona",
        role = "USER",
        puntos = 5,
        baneado = false
    )


    init {

        usuariosLocales.addAll(
            listOf(superAdmin, adminDemo, userDemo1, userDemo2)
        )

        _usuarios.value = usuariosLocales.toList()

        viewModelScope.launch {

            usuariosLocales.forEach {

                val existente = userDao.obtenerPorEmail(it.email)

                if (existente == null) {
                    userDao.insertar(it)
                }
            }
        }
    }


    // ───────────── LOGIN MANUAL ─────────────

    fun login(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _mensajeError.value = "El email y la contraseña no pueden estar vacíos"
            return
        }

        val user = usuariosLocales.find {
            it.email == email && it.password == password
        }

        when {

            user == null ->
                _mensajeError.value = "Correo o contraseña incorrectos"

            user.baneado ->
                _mensajeError.value = "Este usuario está baneado"

            else -> {
                _usuarioActual.value = user
                _mensajeError.value = null
            }
        }
    }


    // ───────────── REGISTRO DE USUARIOS ─────────────

    fun registrar(
        nombre: String,
        nick: String,
        email: String,
        password: String,
        provincia: String
    ) {
        viewModelScope.launch {

            if (password.isBlank()) {
                _mensajeError.value = "La contraseña no puede estar vacía"
                return@launch
            }

            if (usuariosLocales.any { it.email == email }) {
                _mensajeError.value = "El email ya está registrado"
                return@launch
            }

            val nuevoUsuario = User(
                id = 0,
                email = email,
                password = password,
                displayName = nombre,
                nick = nick,
                provincia = provincia,
                role = "USER",
                puntos = 0,
                baneado = false
            )

            usuariosLocales.add(nuevoUsuario)

            /**
             * Guardar también el usuario en Room.
             */
            userDao.insertar(nuevoUsuario)

            _usuarios.value = usuariosLocales.toList()
            _usuarioActual.value = nuevoUsuario
            _mensajeError.value = null
        }
    }


    // ───────────── SISTEMA DE PUNTOS ─────────────

    fun sumarPuntos(cantidad: Int) {

        val user = _usuarioActual.value ?: return

        val nuevos = user.puntos + cantidad

        actualizarPuntos(user.id, nuevos)

        /**
         * Persistir en Room
         */
        viewModelScope.launch {
            userDao.sumarPuntos(user.id, cantidad)
        }
    }


    fun restarPuntos(cantidad: Int) {

        val user = _usuarioActual.value ?: return

        val nuevos = maxOf(0, user.puntos - cantidad)

        actualizarPuntos(user.id, nuevos)

        /**
         * Persistir en Room
         */
        viewModelScope.launch {
            userDao.sumarPuntos(user.id, -cantidad)
        }
    }


    private fun actualizarPuntos(userId: Int, puntos: Int) {

        actualizarUsuario(userId) {
            it.copy(puntos = puntos)
        }

        if (_usuarioActual.value?.id == userId) {
            _usuarioActual.value =
                _usuarioActual.value?.copy(puntos = puntos)
        }
    }


    // ───────────── GOOGLE SIGN-IN ─────────────

    private val googleClient: GoogleSignInClient by lazy {

        val options = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestEmail()
            .build()

        GoogleSignIn.getClient(application, options)
    }

    fun googleSignInIntent(): Intent = googleClient.signInIntent


    fun loginConGoogle(data: Intent?) {

        viewModelScope.launch {

            try {

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)

                val email = account.email ?: return@launch
                val name = account.displayName ?: "Usuario Google"

                val existente = userDao.obtenerPorEmail(email)

                val user = existente ?: User(
                    id = 0,
                    email = email,
                    password = null,
                    displayName = name,
                    nick = name,
                    provincia = "",
                    role = "USER",
                    puntos = 0,
                    baneado = false
                ).also {

                    userDao.insertar(it)
                }

                _usuarioActual.value = user
                _mensajeError.value = null

            } catch (e: Exception) {

                _mensajeError.value = "Error al iniciar sesión con Google"
            }
        }
    }


    // ───────────── MODERACIÓN DE USUARIOS ─────────────

    fun escucharUsuarios() {

        viewModelScope.launch {

            _usuarios.value = userDao.obtenerTodos()
        }
    }

    fun banearUsuario(id: Int) {

        viewModelScope.launch {

            userDao.actualizarEstadoBaneo(id, true)

            escucharUsuarios()
        }
    }

    fun desbanearUsuario(id: Int) {

        viewModelScope.launch {

            userDao.actualizarEstadoBaneo(id, false)

            escucharUsuarios()
        }
    }

    private fun actualizarUsuario(
        id: Int,
        transform: (User) -> User
    ) {
        val index = usuariosLocales.indexOfFirst { it.id == id }

        if (index != -1) {
            usuariosLocales[index] = transform(usuariosLocales[index])
            _usuarios.value = usuariosLocales.toList()
        }
    }

    fun cambiarRol(usuarioId: Int, nuevoRol: String) {

        val actual = _usuarioActual.value ?: return

        // solo SUPERADMIN puede cambiar roles
        if (actual.role != "SUPERADMIN") return

        viewModelScope.launch {

            userDao.actualizarRol(usuarioId, nuevoRol)

            escucharUsuarios()
        }
    }
    // ───────────── RANKING ─────────────
    /**
     * Cargar usuarios ordenados por puntos para el ranking.
     */
    fun cargarRanking() {

        viewModelScope.launch {

            val lista = userDao.obtenerTodos()

            _usuarios.value = lista
        }
    }
    // ───────────── ACTUALIZAR PERFIL ─────────────
    fun actualizarPerfil(
        id: Int,
        nick: String,
        provincia: String
    ) {

        viewModelScope.launch {

            userDao.actualizarPerfil(id, nick, provincia)

            actualizarUsuario(id) {
                it.copy(
                    nick = nick,
                    provincia = provincia
                )
            }

            if (_usuarioActual.value?.id == id) {

                _usuarioActual.value =
                    _usuarioActual.value?.copy(
                        nick = nick,
                        provincia = provincia
                    )
            }
        }
    }
    // ───────────── CIERRE DE SESIÓN ─────────────

    fun logout() {
        googleClient.signOut()
        _usuarioActual.value = null
    }
}