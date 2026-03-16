package com.example.habitos.data.utils

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.util.Calendar

/**
 * Exporta un hábito de la aplicación al calendario del usuario
 * utilizando la aplicación de Google Calendar u otra app de calendario
 * compatible instalada en el dispositivo.
 *
 * @param context Contexto de la aplicación necesario para lanzar el intent.
 * @param titulo Título del evento que aparecerá en el calendario.
 * @param descripcion Descripción del evento asociada al hábito.
 * @param fechaIso Fecha del evento en formato ISO `YYYY-MM-DD`.
 */
fun exportarAHaciaGoogleCalendar(
    context: Context,
    titulo: String,
    descripcion: String,
    fechaIso: String       // Ejemplo: "2025-12-09"
) {
    try {

        // Se separa la fecha en año, mes y día a partir del formato ISO
        val partes = fechaIso.split("-")
        val year = partes[0].toInt()
        val month = partes[1].toInt() - 1 // Calendar utiliza meses de 0 a 11
        val day = partes[2].toInt()

        // Se crea la fecha y hora de inicio del evento (09:00 AM)
        val beginTime = Calendar.getInstance().apply {
            set(year, month, day, 9, 0)
        }

        /**
         * Intent utilizado para abrir la interfaz de creación
         * de eventos del calendario del dispositivo.
         */
        val intent = Intent(Intent.ACTION_INSERT).apply {

            // Indica que el contenido pertenece al proveedor de eventos del calendario
            data = CalendarContract.Events.CONTENT_URI

            // Información básica del evento
            putExtra(CalendarContract.Events.TITLE, titulo)
            putExtra(CalendarContract.Events.DESCRIPTION, descripcion)

            // Hora de inicio del evento
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)

            // Hora de finalización del evento (1 hora después)
            putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                beginTime.timeInMillis + 60 * 60 * 1000
            )
        }

        // Lanza la aplicación de calendario para que el usuario confirme el evento
        context.startActivity(intent)

    } catch (e: Exception) {

        /**
         * En caso de error (por ejemplo formato de fecha incorrecto
         * o ausencia de aplicación de calendario), se captura la
         * excepción para evitar que la aplicación se cierre.
         */
        e.printStackTrace()
    }
}