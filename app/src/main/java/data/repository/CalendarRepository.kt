package com.example.habitos.data.repository

import com.example.habitos.data.dao.CalendarDao
import com.example.habitos.data.entities.CalendarHabit

/**
 * Repositorio encargado de gestionar las operaciones relacionadas
 * con los hábitos programados en el calendario.
 *
 * En la arquitectura MVVM utilizada en la aplicación, el repositorio
 * actúa como capa intermedia entre los ViewModels y el acceso directo
 * a la base de datos (DAO).
 *
 * Sus principales responsabilidades son:
 *
 * - Centralizar el acceso a los datos del calendario.
 * - Abstraer las operaciones del DAO para que la capa de presentación
 *   no dependa directamente de la base de datos.
 * - Gestionar la lógica relacionada con la inserción, modificación
 *   y consulta de hábitos programados.
 *
 * Este repositorio utiliza `CalendarDao` para interactuar con
 * la tabla `calendar_habits` de la base de datos.
 */
class CalendarRepository(private val calendarDao: CalendarDao) {

    /**
     * Obtiene todos los hábitos programados para una fecha concreta.
     *
     * @param fecha Fecha en formato `YYYY-MM-DD`.
     * @param userId Identificador del usuario propietario del calendario.
     * @return Lista de hábitos programados para ese día.
     */
    suspend fun cargarHabitosDeFecha(fecha: String, userId: Int): List<CalendarHabit> {
        return calendarDao.cargarHabitosPorFecha(fecha, userId)
    }

    /**
     * Inserta un nuevo hábito en el calendario para una fecha específica.
     *
     * Este método crea un objeto `CalendarHabit` con estado inicial
     * `"PENDIENTE"` y lo guarda en la base de datos.
     *
     * @param userId Identificador del usuario propietario del calendario.
     * @param habitId Identificador del hábito base (`HealthyHabit`).
     * @param fecha Fecha en la que se programará el hábito.
     * @param titulo Título del hábito.
     * @param descripcion Descripción del hábito.
     * @param imagenUrl Imagen asociada al hábito (utilizada en la UI).
     */
    suspend fun insertarHabitEnCalendario(
        userId: Int,
        habitId: Int,
        fecha: String,
        titulo: String,
        descripcion: String,
        imagenUrl: String
    ) {
        val calendarHabit = CalendarHabit(
            userId = userId,
            habitId = habitId,
            fecha = fecha,
            titulo = titulo,
            descripcion = descripcion,
            imagenUrl = imagenUrl,
            estado = "PENDIENTE"
        )
        calendarDao.insertar(calendarHabit)
    }

    /**
     * Elimina un hábito programado del calendario mediante su identificador.
     *
     * @param id Identificador del registro en `calendar_habits`.
     */
    suspend fun borrarPorId(id: Int) {
        calendarDao.eliminarPorId(id)
    }

    /**
     * Actualiza el estado de un hábito programado.
     *
     * Los estados posibles suelen ser:
     * - PENDIENTE
     * - HECHO
     * - FALLIDO
     *
     * @param id Identificador del hábito en el calendario.
     * @param estado Nuevo estado del hábito.
     */
    suspend fun actualizarEstado(id: Int, estado: String) {
        calendarDao.actualizarEstado(id, estado)
    }

    /**
     * Obtiene todos los hábitos registrados en el calendario.
     *
     * @param userId Identificador del usuario propietario del calendario.
     * @return Lista completa de registros de `calendar_habits`.
     */
    suspend fun obtenerTodos(userId: Int): List<CalendarHabit> {
        return calendarDao.obtenerTodos(userId)
    }

    /**
     * Obtiene un hábito del calendario mediante su identificador.
     *
     * @param id Identificador del hábito en el calendario.
     * @return El objeto `CalendarHabit` correspondiente o null si no existe.
     */
    suspend fun obtenerPorId(id: Int) =
        calendarDao.obtenerPorId(id)

    /**
     * Actualiza el estado de un hábito y marca que los puntos
     * correspondientes ya han sido aplicados al usuario.
     *
     * Este método se utiliza para evitar que un mismo hábito
     * otorgue puntos múltiples veces.
     *
     * @param id Identificador del hábito.
     * @param estado Nuevo estado del hábito.
     */
    suspend fun actualizarEstadoYPuntos(id: Int, estado: String) {
        calendarDao.actualizarEstadoYPuntos(id, estado)
    }

    /**
     * Marca como no realizados los hábitos de fechas pasadas
     * que no hayan sido completados.
     *
     * Esta función se utiliza para mantener actualizado el estado
     * de los hábitos en el calendario cuando el usuario no los
     * ha completado en su día correspondiente.
     *
     * @param hoy Fecha actual utilizada como referencia.
     */
    suspend fun marcarPasadosComoNoHechos(hoy: String) {
        calendarDao.marcarNoHechosPasados(hoy)
    }
}