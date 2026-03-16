package com.example.habitos.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitos.data.dao.CalendarDao
import com.example.habitos.data.entities.CalendarHabit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar la lógica relacionada con
 * los hábitos programados en el calendario de la aplicación.
 *
 * Este ViewModel forma parte de la arquitectura MVVM y actúa como
 * intermediario entre la interfaz de usuario (pantallas Compose)
 * y la capa de datos gestionada por Room a través de CalendarDao.
 *
 * Funcionalidades principales:
 *
 * - Cargar todos los hábitos del calendario
 * - Mostrar los hábitos asociados a una fecha concreta
 * - Añadir hábitos al calendario
 * - Eliminar hábitos programados
 * - Actualizar el estado de un hábito (HECHO / PENDIENTE / NO_HECHO)
 * - Aplicar o restar puntos al usuario según el estado del hábito
 *
 * Los datos se exponen a la interfaz mediante StateFlow para que
 * las pantallas Compose se actualicen automáticamente cuando
 * cambian los hábitos del calendario.
 */
class CalendarViewModel(
    private val calendarDao: CalendarDao,
    private val authViewModel: AuthViewModel
) : ViewModel() {


    // ───────────── HÁBITOS DEL DÍA SELECCIONADO ─────────────

    /**
     * Lista de hábitos asociados a la fecha seleccionada
     * en el calendario.
     */
    private val _habitsForSelectedDate =
        MutableStateFlow<List<CalendarHabit>>(emptyList())

    val habitsForSelectedDate: StateFlow<List<CalendarHabit>> =
        _habitsForSelectedDate


    // ───────────── TODOS LOS HÁBITOS DEL CALENDARIO ─────────────

    /**
     * Lista completa de hábitos programados en el calendario.
     * Se utiliza para generar vistas generales del calendario.
     */
    private val _allHabits =
        MutableStateFlow<List<CalendarHabit>>(emptyList())

    val allHabits: StateFlow<List<CalendarHabit>> =
        _allHabits


    /**
     * Fecha actualmente seleccionada en el calendario.
     */
    private var fechaSeleccionada: String = ""


    /**
     * Inicialización del ViewModel.
     *
     * Al crearse el ViewModel se realizan dos acciones:
     *
     * 1. Cargar todos los hábitos del calendario.
     * 2. Marcar automáticamente como no realizados los hábitos
     *    que pertenecen a fechas pasadas.
     */
    init {

        /**
         * Observa cambios en el usuario autenticado.
         *
         * Cada vez que cambia el usuario (login, logout o cambio de cuenta),
         * se recarga automáticamente el calendario para mostrar únicamente
         * los hábitos pertenecientes a ese usuario.
         */
        viewModelScope.launch {

            authViewModel.usuarioActual.collect {

                cargarTodos()

                if (fechaSeleccionada.isNotBlank()) {
                    selectDate(fechaSeleccionada)
                }
            }
        }

        cargarTodos()
        marcarPasados()
    }


    // ───────────── CARGA DE DATOS ─────────────

    /**
     * Carga todos los hábitos almacenados en el calendario
     * desde la base de datos Room.
     */
    private fun cargarTodos() {

        val user = authViewModel.usuarioActual.value ?: return

        viewModelScope.launch {
            _allHabits.value = calendarDao.obtenerTodos(user.id)
        }
    }


    /**
     * Marca automáticamente como NO_HECHO los hábitos que
     * pertenecen a fechas anteriores al día actual y que
     * no han sido completados.
     *
     * Esto permite mantener el estado del calendario actualizado.
     */
    private fun marcarPasados() {

        val hoy = java.time.LocalDate.now().toString()

        viewModelScope.launch {
            calendarDao.marcarNoHechosPasados(hoy)
            cargarTodos()
        }
    }


    // ───────────── SELECCIÓN DE FECHA ─────────────

    /**
     * Selecciona una fecha del calendario y carga los hábitos
     * asociados a ese día concreto.
     */
    fun selectDate(fecha: String) {

        fechaSeleccionada = fecha

        val user = authViewModel.usuarioActual.value ?: return

        viewModelScope.launch {
            _habitsForSelectedDate.value =
                calendarDao.cargarHabitosPorFecha(fecha, user.id)
        }
    }


    // ───────────── AÑADIR HÁBITO AL CALENDARIO ─────────────

    /**
     * Añade un hábito al día actualmente seleccionado.
     *
     * Se crea una instancia de CalendarHabit a partir de un
     * hábito general (HealthyHabit).
     */
    fun addHabitToSelectedDate(habit: com.example.habitos.data.entities.HealthyHabit) {

        if (fechaSeleccionada.isBlank()) return

        val user = authViewModel.usuarioActual.value ?: return

        val calendarHabit = CalendarHabit(
            userId = user.id,
            habitId = habit.id,
            fecha = fechaSeleccionada,
            titulo = habit.titulo,
            descripcion = habit.descripcion,
            imagenUrl = habit.imagenUrl,
            estado = "PENDIENTE"
        )

        viewModelScope.launch {

            calendarDao.insertar(calendarHabit)

            // Recargar datos tras la inserción
            selectDate(fechaSeleccionada)
            cargarTodos()
        }
    }


    // ───────────── ELIMINAR HÁBITO DEL CALENDARIO ─────────────

    /**
     * Elimina un hábito programado del calendario.
     */
    fun deleteCalendarHabit(habit: CalendarHabit) {

        viewModelScope.launch {

            calendarDao.eliminarPorId(habit.id)

            // Actualizar datos tras la eliminación
            selectDate(fechaSeleccionada)
            cargarTodos()
        }
    }


    // ───────────── ACTUALIZAR ESTADO DEL HÁBITO ─────────────

    /**
     * Actualiza el estado de un hábito programado.
     *
     * Estados posibles:
     * - PENDIENTE
     * - HECHO
     * - NO_HECHO
     *
     * Además, se aplica el sistema de gamificación:
     *
     * - Si el hábito se marca como HECHO → +10 puntos
     * - Si vuelve a estado PENDIENTE → -2 puntos
     */
    fun updateEstado(id: Int, nuevoEstado: String) {

        viewModelScope.launch {

            val habit = calendarDao.obtenerPorId(id) ?: return@launch

            val estadoAnterior = habit.estado

            when {

                // Marcar como hecho
                estadoAnterior != "HECHO" && nuevoEstado == "HECHO" ->
                    authViewModel.sumarPuntos(10)

                // Marcar como no hecho
                estadoAnterior != "NO_HECHO" && nuevoEstado == "NO_HECHO" ->
                    authViewModel.restarPuntos(2)

                // Deshacer hecho
                estadoAnterior == "HECHO" && nuevoEstado == "PENDIENTE" ->
                    authViewModel.restarPuntos(10)

                // Deshacer no hecho
                estadoAnterior == "NO_HECHO" && nuevoEstado == "PENDIENTE" ->
                    authViewModel.sumarPuntos(2)
            }

            calendarDao.actualizarEstado(id, nuevoEstado)

            selectDate(fechaSeleccionada)
            cargarTodos()
        }
    }
}