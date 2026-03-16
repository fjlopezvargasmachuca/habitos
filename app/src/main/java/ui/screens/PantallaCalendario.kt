package com.example.habitos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.filter
import androidx.compose.foundation.layout.Column

/**
 * Pantalla principal del calendario de hábitos.
 *
 * Esta pantalla permite al usuario visualizar todos los hábitos registrados
 * organizados en un calendario mensual interactivo.
 *
 * Funcionalidades principales:
 *
 * - Visualización del calendario por meses.
 * - Estadísticas rápidas de hábitos (hechos, no hechos, pendientes).
 * - Mensajes motivacionales aleatorios.
 * - Indicadores visuales de estado de hábitos por día.
 * - Navegación al detalle de hábitos de una fecha concreta.
 *
 * Los colores del calendario indican el estado de los hábitos:
 *
 * - Verde → todos los hábitos del día completados.
 * - Rojo → hábitos no realizados.
 * - Amarillo → mezcla de completados y pendientes.
 * - Gris → día sin hábitos registrados.
 *
 * Además, el día actual se resalta con un borde más grueso para facilitar
 * su identificación dentro del calendario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCalendario(
    navController: NavController,
    calendarViewModel: CalendarViewModel
) {

    /**
     * Estado del mes actualmente visualizado en el calendario.
     * Permite navegar entre meses usando los botones de navegación.
     */
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    /**
     * Fecha actual utilizada para resaltar el día presente en el calendario.
     */
    val hoy = LocalDate.now().toString()

    /**
     * Lista completa de hábitos almacenados en el calendario.
     * Se obtiene desde el ViewModel mediante un StateFlow.
     */
    val allHabits = calendarViewModel.allHabits.collectAsState(
        initial = emptyList()
    ).value

    /**
     * Estadísticas globales de hábitos.
     */
    val hechos = allHabits.count { it.estado == "HECHO" }
    val noHechos = allHabits.count { it.estado == "NO_HECHO" }
    val pendientes = allHabits.count { it.estado == "PENDIENTE" }

    /**
     * Mensaje motivacional mostrado en la parte superior del calendario.
     */
    var mensajeMotivacional by remember { mutableStateOf("") }

    /**
     * Al iniciar la pantalla se selecciona un mensaje motivacional aleatorio.
     */
    LaunchedEffect(Unit) {
        mensajeMotivacional = mensajesMotivacionales.random()
    }

    /**
     * Layout principal de la pantalla utilizando Scaffold.
     * Incluye la barra superior y el contenido desplazable.
     */
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendario") },

                /**
                 * Botón de navegación para volver a la pantalla anterior.
                 */
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        /**
         * Lista vertical que contiene todos los componentes de la pantalla.
         */
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {

            /**
             * Estadísticas de hábitos.
             * Se organizan en columnas para evitar superposición
             * entre iconos y texto en pantallas pequeñas.
             */
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✔")
                        Text("Hechos: $hechos")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("❌")
                        Text("No hechos: $noHechos")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⏳")
                        Text("Pendientes: $pendientes")
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            /**
             * Tarjeta que muestra el mensaje motivacional.
             */
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "💪 $mensajeMotivacional",
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(12.dp))
            }

            /**
             * Selector de mes del calendario.
             * Permite navegar entre meses anterior y siguiente.
             */
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = {
                        currentYearMonth = currentYearMonth.minusMonths(1)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                    }

                    Text(
                        text = currentYearMonth.month
                            .getDisplayName(TextStyle.FULL, Locale("es"))
                            .replaceFirstChar { it.uppercase() } +
                                " ${currentYearMonth.year}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    IconButton(onClick = {
                        currentYearMonth = currentYearMonth.plusMonths(1)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            /**
             * Cabecera con los días de la semana.
             */
            item {

                val diasSemana = listOf("L", "M", "X", "J", "V", "S", "D")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    diasSemana.forEach { dia ->
                        Text(
                            text = dia,
                            modifier = Modifier.width(45.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(Modifier.height(6.dp))
            }

            /**
             * Calendario principal representado mediante una cuadrícula de 7 columnas.
             */
            item {

                val primerDiaSemana =
                    (currentYearMonth.atDay(1).dayOfWeek.value + 6) % 7

                val diasMes = currentYearMonth.lengthOfMonth()

                val totalCeldas = primerDiaSemana + diasMes

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(360.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    items(totalCeldas) { index ->

                        /**
                         * Celdas vacías para alinear el primer día del mes
                         * con su día de la semana correspondiente.
                         */
                        if (index < primerDiaSemana) {

                            Box(Modifier.size(45.dp))

                        } else {

                            val dia = index - primerDiaSemana + 1

                            val fechaKey = String.format(
                                "%04d-%02d-%02d",
                                currentYearMonth.year,
                                currentYearMonth.monthValue,
                                dia
                            )

                            /**
                             * Hábitos correspondientes al día seleccionado.
                             */
                            val habitsToday =
                                allHabits.filter { it.fecha == fechaKey }

                            val total = habitsToday.size

                            val hechosDia =
                                habitsToday.count { it.estado == "HECHO" }

                            val noHechosDia =
                                habitsToday.count { it.estado == "NO_HECHO" }

                            /**
                             * Color del día según el estado de los hábitos.
                             */
                            val colorDia = when {
                                total == 0 -> Color(0xFFE0E0E0)
                                hechosDia == total -> Color(0xFF4CAF50)
                                noHechosDia > 0 && hechosDia == 0 -> Color(0xFFF44336)
                                else -> Color(0xFFFFC107)
                            }

                            /**
                             * Color del texto del día.
                             * Se calcula dinámicamente para asegurar contraste
                             * correcto tanto en modo claro como en modo oscuro.
                             */
                            val colorTexto = when (colorDia) {
                                Color(0xFF4CAF50),
                                Color(0xFFF44336) -> Color.White
                                else -> Color.Black
                            }

                            /**
                             * Indica si la celda corresponde al día actual.
                             */
                            val esHoy = fechaKey == hoy

                            /**
                             * Celda del día dentro del calendario.
                             * Permite navegar al detalle del día.
                             */
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .background(
                                        colorDia,
                                        if (esHoy)
                                            MaterialTheme.shapes.medium
                                        else
                                            MaterialTheme.shapes.small
                                    )
                                    .border(
                                        if (esHoy) 3.dp else 1.dp,
                                        if (esHoy)
                                            Color(0xFF2962FF) // azul fuerte visible en modo claro y oscuro
                                        else
                                            Color.DarkGray
                                    )
                                    .clickable {
                                        navController.navigate(
                                            "calendario/dia/$fechaKey"
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {

                                /**
                                 * Número del día dentro del calendario.
                                 */
                                Text(
                                    text = dia.toString(),
                                    color = colorTexto
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Lista de frases motivacionales utilizadas en la pantalla del calendario.
 *
 * Se selecciona una frase aleatoria cada vez que se abre la pantalla
 * para incentivar al usuario a mantener la constancia en sus hábitos.
 */
private val mensajesMotivacionales = listOf(
    "Cada hábito completado es una victoria 🏆",
    "Hoy es un buen día para mejorar 💚",
    "Un pequeño paso hoy cambia tu mañana",
    "La constancia vence a la motivación",
    "No busques perfección, busca progreso",
    "Tu yo del futuro te lo agradecerá",
    "Hazlo por ti 💪",
    "Los hábitos crean resultados",
    "Un día a la vez",
    "Sigue, aunque cueste",
    "Todo esfuerzo cuenta",
    "No pares ahora",
    "Estás más cerca de lo que crees",
    "Disciplina > excusas",
    "Hoy suma"
)