package com.example.habitos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitos.data.entities.ForumMessage
import com.example.habitos.data.entities.ForumThread
import com.example.habitos.data.entities.ForumTopic
import com.example.habitos.data.entities.User
import com.example.habitos.data.repository.ForumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel encargado de gestionar toda la lógica relacionada
 * con el sistema de foro de la aplicación.
 *
 * Este ViewModel forma parte de la arquitectura MVVM y actúa
 * como intermediario entre la interfaz de usuario (pantallas
 * Compose) y la capa de datos gestionada por ForumRepository.
 *
 * Funcionalidades principales:
 *
 * - Gestión de temas del foro
 * - Gestión de hilos dentro de cada tema
 * - Envío y lectura de mensajes
 * - Sistema de likes en mensajes
 * - Moderación de contenido (editar o borrar)
 *
 * Los datos se exponen a la interfaz mediante StateFlow,
 * lo que permite que las pantallas Compose se actualicen
 * automáticamente cuando cambian los datos del foro.
 */
class ForumViewModel(
    private val repository: ForumRepository
) : ViewModel() {

    // ───────────── LISTA DE TEMAS DEL FORO ─────────────

    /**
     * Lista observable de temas del foro.
     */
    private val _temas = MutableStateFlow<List<ForumTopic>>(emptyList())
    val temas: StateFlow<List<ForumTopic>> = _temas


    // ───────────── LISTA DE HILOS ─────────────

    /**
     * Lista de hilos pertenecientes a un tema concreto.
     */
    private val _hilos = MutableStateFlow<List<ForumThread>>(emptyList())
    val hilos: StateFlow<List<ForumThread>> = _hilos


    // ───────────── LISTA DE MENSAJES ─────────────

    /**
     * Lista de mensajes dentro de un hilo de discusión.
     */
    private val _mensajes = MutableStateFlow<List<ForumMessage>>(emptyList())
    val mensajes: StateFlow<List<ForumMessage>> = _mensajes


    /**
     * Inicialización del ViewModel.
     *
     * Se ejecuta una carga inicial de datos del foro.
     * Además, se asegura que existan datos de ejemplo
     * si el foro está vacío.
     */
    init {
        viewModelScope.launch {
            repository.seedInicialSiNecesario()
            escucharTemas()
        }
    }


    // ───────────── GESTIÓN DE TEMAS ─────────────

    /**
     * Carga la lista de temas del foro desde el repositorio.
     */
    fun escucharTemas() {
        viewModelScope.launch {
            _temas.value = repository.obtenerTemas()
        }
    }

    /**
     * Crea un nuevo tema dentro del foro.
     */
    fun crearTema(nombre: String, descripcion: String) {
        viewModelScope.launch {
            repository.crearTema(nombre, descripcion)
            escucharTemas()
        }
    }

    /**
     * Permite modificar el nombre y la descripción de un tema existente.
     */
    fun editarTema(id: Int, nombre: String, descripcion: String) {
        viewModelScope.launch {
            repository.actualizarTema(id, nombre, descripcion)
            escucharTemas()
        }
    }

    /**
     * Elimina un tema del foro junto con todos sus hilos asociados.
     */
    fun borrarTema(id: Int) {
        viewModelScope.launch {
            repository.borrarTemaConHilos(id)
            escucharTemas()
        }
    }

    /**
     * Permite fijar o no fijar un tema dentro del foro.
     *
     * Solo los usuarios con rol ADMIN o SUPERADMIN
     * pueden realizar esta acción.
     */
    fun toggleFijarTema(tema: ForumTopic, user: User) {

        if (user.role == "USER") return

        viewModelScope.launch {
            repository.fijarTema(tema.id, !tema.fijado)
            escucharTemas()
        }
    }


    // ───────────── GESTIÓN DE HILOS ─────────────

    /**
     * Carga los hilos asociados a un tema concreto.
     */
    fun escucharHilos(topicId: String) {
        viewModelScope.launch {
            _hilos.value = repository.obtenerHilos(topicId.toInt())
        }
    }

    /**
     * Crea un nuevo hilo dentro de un tema del foro.
     */
    fun crearHilo(topicId: String, titulo: String, user: User) {
        viewModelScope.launch {
            repository.crearHilo(topicId.toInt(), titulo, user)
            escucharHilos(topicId)
        }
    }

    /**
     * Permite editar el título de un hilo.
     *
     * Solo puede editar:
     * - El autor del hilo
     * - Un SUPERADMIN
     */
    fun editarHilo(hiloId: Int, nuevoTitulo: String, user: User) {
        viewModelScope.launch {

            val hilo = _hilos.value.firstOrNull { it.id == hiloId }
                ?: return@launch

            if (user.role != "SUPERADMIN" && hilo.autorId != user.id)
                return@launch

            repository.actualizarHilo(hiloId, nuevoTitulo)

            escucharHilos(hilo.topicId.toString())
        }
    }

    /**
     * Elimina un hilo del foro junto con todos sus mensajes.
     */
    fun borrarHilo(hiloId: Int, topicId: String) {
        viewModelScope.launch {
            repository.borrarHiloConMensajes(hiloId)
            escucharHilos(topicId)
        }
    }


    // ───────────── GESTIÓN DE MENSAJES ─────────────

    /**
     * Carga los mensajes pertenecientes a un hilo concreto.
     */
    fun escucharMensajes(threadId: String) {
        viewModelScope.launch {
            _mensajes.value = repository.obtenerMensajes(threadId.toInt())
        }
    }

    /**
     * Envía un nuevo mensaje dentro de un hilo de discusión.
     */
    fun enviarMensaje(threadId: String, texto: String, user: User) {
        viewModelScope.launch {
            repository.enviarMensaje(threadId.toInt(), texto, user)
            escucharMensajes(threadId)
        }
    }

    /**
     * Elimina un mensaje del foro.
     */
    fun borrarMensaje(id: Int) {
        viewModelScope.launch {
            repository.borrarMensaje(id)
        }
    }


    // ───────────── SISTEMA DE LIKES ─────────────

    /**
     * Permite añadir o quitar un "like" a un mensaje.
     *
     * Tras realizar la acción se recargan los mensajes
     * para actualizar la interfaz.
     */
    fun toggleLike(mensajeId: Int, userId: Int, threadId: String) {
        viewModelScope.launch {
            repository.toggleLike(mensajeId, userId)

            // refresco de la interfaz
            escucharMensajes(threadId)
        }
    }

    /**
     * Comprueba si un usuario concreto ya ha dado like
     * a un mensaje determinado.
     *
     * El resultado se devuelve mediante un callback.
     */
    fun usuarioHaDadoLike(
        mensajeId: Int,
        userId: Int,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            val result =
                repository.usuarioHaDadoLike(mensajeId, userId)

            onResult(result)
        }
    }
}