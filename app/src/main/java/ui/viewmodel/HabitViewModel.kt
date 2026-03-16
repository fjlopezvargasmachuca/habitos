package com.example.habitos.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.habitos.data.entities.HealthyHabit
import com.example.habitos.data.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import com.example.habitos.data.utils.HabitGenerator
import android.app.Application
import com.example.habitos.data.database.HabitDatabase
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
/**
 * ViewModel encargado de gestionar la lógica relacionada con
 * los hábitos saludables dentro de la aplicación.
 *
 * Este ViewModel forma parte de la arquitectura MVVM y actúa
 * como intermediario entre la interfaz de usuario (pantallas
 * desarrolladas con Jetpack Compose) y la capa de datos
 * gestionada mediante Room a través de HabitRepository.
 *
 * Funcionalidades principales:
 *
 * - Generar hábitos aleatorios mediante HabitGenerator
 * - Insertar hábitos en la base de datos
 * - Obtener hábitos por categoría
 * - Eliminar hábitos existentes
 * - Garantizar un número mínimo de hábitos por categoría
 * - Obtener hábitos para exportación a PDF
 *
 * Los datos se exponen a la interfaz utilizando StateFlow,
 * lo que permite que las pantallas Compose reaccionen
 * automáticamente cuando cambia la lista de hábitos.
 */
class HabitViewModel(
    application: Application,
    private val authViewModel: AuthViewModel
) : AndroidViewModel(application) {

    private val repo: HabitRepository
    private val generator = HabitGenerator()

    private val _habitoActual = MutableStateFlow(generator.nuevoHabito())
    val habitoActual: StateFlow<HealthyHabit> = _habitoActual

    val listaHabitos: StateFlow<List<HealthyHabit>>

    init {

        val db = HabitDatabase.getDatabase(application)

        repo = HabitRepository(db.habitDao())

        /**
         * Se obtiene el usuario actual.
         *
         * Si no existe usuario registrado se utiliza el usuario invitado (id = 0).
         */
        listaHabitos =
            authViewModel.usuarioActual
                .flatMapLatest { user ->

                    val userId = user?.id ?: 0

                    repo.obtenerTodos(userId)
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.Lazily,
                    emptyList()
                )
    }

    fun generarNuevoHabito() {
        _habitoActual.value = generator.nuevoHabito()
    }

    fun insertarActual() {

        val userId = authViewModel.usuarioActual.value?.id ?: 0

        viewModelScope.launch {

            val habit = _habitoActual.value.copy(userId = userId)

            val existe = repo.existeHabitoUsuario(habit.titulo, userId)

            if (!existe) {

                repo.insertar(habit)
            }

            generarNuevoHabito()
        }
    }

    fun insertarHabito(h: HealthyHabit): Boolean {

        val userId = authViewModel.usuarioActual.value?.id ?: 0

        var insertado = false

        viewModelScope.launch {

            val existe = repo.existeHabitoUsuario(h.titulo, userId)

            if (!existe) {

                repo.insertar(h.copy(userId = userId))

                insertado = true
            }
        }

        return insertado
    }


    fun borrarHabito(id: Int) {

        viewModelScope.launch {
            repo.borrarHabito(id)
        }
    }

    fun obtenerTodosUnaVez(onResult: (List<HealthyHabit>) -> Unit) {

        val userId = authViewModel.usuarioActual.value?.id ?: 0

        viewModelScope.launch {

            onResult(repo.obtenerTodosUnaVez(userId))
        }
    }


}