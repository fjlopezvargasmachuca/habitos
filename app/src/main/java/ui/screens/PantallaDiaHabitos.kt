package com.example.habitos.ui.screens

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.ui.viewmodel.CalendarViewModel
import com.example.habitos.ui.viewmodel.HabitViewModel
import com.example.habitos.data.entities.CalendarHabit

/**
 * Pantalla que muestra los hábitos asignados a un día concreto del calendario.
 *
 * Esta pantalla permite al usuario:
 *
 * - visualizar los hábitos programados para una fecha determinada
 * - marcar hábitos como realizados o no realizados
 * - eliminar hábitos del calendario
 * - exportar un hábito al calendario de Google
 * - añadir nuevos hábitos guardados al día seleccionado
 *
 * La información se obtiene desde el CalendarViewModel mediante StateFlow,
 * permitiendo que la interfaz se actualice automáticamente cuando cambian
 * los datos del calendario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDiaHabitos(
    navController: NavController,
    calendarViewModel: CalendarViewModel,
    habitViewModel: HabitViewModel,
    authViewModel: AuthViewModel,
    fechaKey: String
) {

    /**
     * Contexto necesario para lanzar intents del sistema,
     * como la exportación al calendario de Google.
     */
    val context = LocalContext.current

    /**
     * Cada vez que cambia la fecha seleccionada,
     * se solicita al ViewModel que cargue los hábitos de ese día.
     */
    LaunchedEffect(fechaKey) {
        calendarViewModel.selectDate(fechaKey)
    }

    /**
     * Lista de hábitos asignados a la fecha seleccionada.
     */
    val lista by calendarViewModel.habitsForSelectedDate.collectAsState()

    /**
     * Lista completa de hábitos guardados por el usuario,
     * que pueden añadirse al calendario.
     */
    val habitosGuardados by habitViewModel.listaHabitos.collectAsState()

    /**
     * Controla la visibilidad del diálogo para añadir hábitos.
     */
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hábitos del $fechaKey") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(12.dp)
        ) {

            /**
             * Botón que abre el diálogo para añadir un hábito al día.
             */
            Button(onClick = { mostrarDialogo = true }) {
                Text("Añadir hábito")
            }

            Spacer(Modifier.height(12.dp))

            /**
             * Si no hay hábitos programados para este día
             * se muestra un mensaje informativo en lugar de
             * una lista vacía.
             */
            if (lista.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "No hay hábitos para este día",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

            } else {

                /**
                 * Lista de hábitos programados para el día seleccionado.
                 */
                LazyColumn {

                    items(lista, key = { it.id }) { item ->

                        CalendarHabitItem(
                            calendarHabit = item,

                            onUpdateEstado = { estado ->
                                calendarViewModel.updateEstado(
                                    id = item.id,
                                    nuevoEstado = estado
                                )
                            },

                            onEliminar = {
                                calendarViewModel.deleteCalendarHabit(item)
                            },

                            onExportar = {
                                exportarAGoogleCalendar(context, item)
                            }
                        )
                    }
                }
            }

            /**
             * Diálogo para seleccionar un hábito guardado
             * y añadirlo al día del calendario.
             */
            if (mostrarDialogo) {

                Dialog(onDismissRequest = { mostrarDialogo = false }) {

                    Surface {

                        Column(Modifier.padding(16.dp)) {

                            /**
                             * Si no hay hábitos guardados se muestra un mensaje
                             * informando al usuario.
                             */
                            if (habitosGuardados.isEmpty()) {

                                Text(
                                    text = "No tienes hábitos guardados.\nPrimero crea hábitos en la sección correspondiente.",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )

                            } else {

                                LazyColumn {

                                    items(habitosGuardados) { hab ->

                                        TextButton(onClick = {

                                            calendarViewModel.addHabitToSelectedDate(hab)

                                            mostrarDialogo = false
                                        }) {

                                            Text(hab.titulo)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Componente reutilizable que representa un hábito dentro del calendario.
 *
 * Cada tarjeta muestra:
 *
 * - imagen del hábito (si existe)
 * - título
 * - estado del hábito
 * - acciones disponibles (exportar o eliminar)
 *
 * Permite actualizar el estado del hábito directamente desde la interfaz.
 */
@Composable
private fun CalendarHabitItem(
    calendarHabit: CalendarHabit,
    onUpdateEstado: (String) -> Unit,
    onEliminar: () -> Unit,
    onExportar: () -> Unit
) {

    /**
     * Estado actual del hábito (HECHO, NO_HECHO, PENDIENTE).
     */
    var estado by remember { mutableStateOf(calendarHabit.estado) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {

        Column(Modifier.padding(12.dp)) {

            /**
             * Si el hábito tiene imagen se muestra en la tarjeta.
             */
            if (calendarHabit.imagenUrl.isNotBlank()) {

                AsyncImage(
                    model = calendarHabit.imagenUrl,
                    contentDescription = calendarHabit.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }

            Text(
                calendarHabit.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))


            /**
             * Controles para marcar el hábito como realizado o no realizado.
             */
            Row(verticalAlignment = Alignment.CenterVertically) {

                Checkbox(
                    checked = estado == "HECHO",
                    onCheckedChange = {

                        estado = if (it) "HECHO" else "PENDIENTE"

                        onUpdateEstado(estado)
                    }
                )

                Text("Hecho")

                Spacer(Modifier.width(16.dp))

                Checkbox(
                    checked = estado == "NO_HECHO",
                    onCheckedChange = {

                        estado = if (it) "NO_HECHO" else "PENDIENTE"

                        onUpdateEstado(estado)
                    }
                )

                Text("No hecho")
            }

            Spacer(Modifier.height(8.dp))

            /**
             * Acciones disponibles para el hábito:
             *
             * - exportar al calendario de Google
             * - eliminar del calendario
             */
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                TextButton(onClick = onExportar) {
                    Text("Exportar")
                }

                IconButton(onClick = onEliminar) {
                    Icon(Icons.Filled.Delete, null)
                }
            }
        }
    }
}


/**
 * Función auxiliar que exporta un hábito al calendario de Google
 * utilizando un Intent del sistema Android.
 *
 * Se abre la aplicación de calendario instalada en el dispositivo
 * con el título del evento ya rellenado y con la fecha correcta
 * del hábito seleccionado.
 */
private fun exportarAGoogleCalendar(
    context: android.content.Context,
    item: CalendarHabit
) {

    try {

        /**
         * Conversión de la fecha del hábito (formato ISO yyyy-MM-dd)
         * a un objeto LocalDate.
         */
        val fecha = java.time.LocalDate.parse(item.fecha)

        /**
         * Se crea una instancia de Calendar para establecer
         * la hora del evento (09:00 por defecto).
         */
        val beginTime = java.util.Calendar.getInstance().apply {

            set(
                fecha.year,
                fecha.monthValue - 1, // Calendar usa meses 0-11
                fecha.dayOfMonth,
                9,
                0
            )
        }

        /**
         * Intent que abre Google Calendar para crear un nuevo evento.
         */
        val intent = Intent(Intent.ACTION_INSERT).apply {

            data = CalendarContract.Events.CONTENT_URI

            putExtra(CalendarContract.Events.TITLE, item.titulo)

            /**
             * Fecha de inicio del evento.
             */
            putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                beginTime.timeInMillis
            )

            /**
             * Fecha de finalización (1 hora después).
             */
            putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                beginTime.timeInMillis + 60 * 60 * 1000
            )
        }

        context.startActivity(intent)

    } catch (e: Exception) {

        e.printStackTrace()
    }
}
