package com.example.habitos.utils

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.util.Calendar

/**
 * Envía un hábito al calendario de Google del usuario.
 *
 * Esta función permite exportar un hábito programado desde la
 * aplicación al calendario del dispositivo mediante un Intent
 * del sistema Android.
 *
 * Al ejecutarse, se abre la interfaz de creación de eventos del
 * calendario con los datos del hábito ya rellenados para que
 * el usuario pueda confirmarlo o modificarlo antes de guardarlo.
 *
 * Parámetros:
 *
 * - context → contexto de la aplicación necesario para iniciar el Intent
 * - titulo → título del evento que aparecerá en el calendario
 * - descripcion → descripción del evento
 * - fechaIso → fecha del evento en formato ISO (YYYY-MM-DD)
 *
 * Ejemplo de fecha válida:
 * "2025-12-09"
 *
 * El evento creado tiene una duración de 1 hora y comienza
 * automáticamente a las 09:00 de la mañana.
 *
 * Nota:
 * Esta funcionalidad está pensada para usuarios que han iniciado
 * sesión con Google y desean sincronizar sus hábitos con su calendario.
 */
fun exportarAHaciaGoogleCalendar(
    context: Context,
    titulo: String,
    descripcion: String,
    fechaIso: String       // Ejemplo: "2025-12-09"
) {

    try {

        /**
         * Separación de la fecha ISO en sus componentes
         * año, mes y día.
         */
        val partes = fechaIso.split("-")

        val year = partes[0].toInt()
        val month = partes[1].toInt() - 1 // Calendar usa meses de 0 a 11
        val day = partes[2].toInt()


        /**
         * Creación de la fecha de inicio del evento.
         *
         * En este caso se fija la hora a las 09:00 AM.
         */
        val beginTime = Calendar.getInstance().apply {
            set(year, month, day, 9, 0)
        }


        /**
         * Intent utilizado para abrir la interfaz de creación
         * de eventos del calendario del dispositivo.
         */
        val intent = Intent(Intent.ACTION_INSERT).apply {

            /**
             * URI del proveedor de eventos del calendario.
             */
            data = CalendarContract.Events.CONTENT_URI

            /**
             * Título del evento.
             */
            putExtra(CalendarContract.Events.TITLE, titulo)

            /**
             * Descripción del evento.
             */
            putExtra(CalendarContract.Events.DESCRIPTION, descripcion)

            /**
             * Hora de inicio del evento.
             */
            putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                beginTime.timeInMillis
            )

            /**
             * Hora de finalización del evento.
             *
             * En este caso se establece una duración de 1 hora.
             */
            putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                beginTime.timeInMillis + 60 * 60 * 1000
            )
        }


        /**
         * Lanzamiento del Intent para abrir la aplicación
         * de calendario del dispositivo.
         */
        context.startActivity(intent)

    } catch (e: Exception) {

        /**
         * Manejo básico de errores en caso de que ocurra
         * algún problema al crear el evento.
         */
        e.printStackTrace()
    }
}