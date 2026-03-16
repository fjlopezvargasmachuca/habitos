package com.example.habitos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.habitos.data.utils.PdfGenerator
import com.example.habitos.ui.viewmodel.HabitViewModel

/**
 * Pantalla para generar y guardar nuevos hábitos saludables.
 *
 * Esta pantalla permite al usuario:
 *
 * - Generar hábitos aleatorios utilizando el generador de hábitos.
 * - Visualizar los detalles del hábito generado (imagen, descripción, categoría, etc.).
 * - Guardar el hábito en la base de datos local.
 * - Exportar todos los hábitos guardados en formato PDF.
 *
 * La pantalla utiliza el HabitViewModel para gestionar el estado del hábito actual
 * y para interactuar con la base de datos mediante el repositorio correspondiente.
 *
 * Además, se implementan pequeñas animaciones visuales para mejorar la experiencia
 * de usuario cuando aparece el hábito generado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNuevoHabito(
    viewModel: HabitViewModel,
    navController: NavController
) {

    /**
     * Hábito actualmente generado o seleccionado.
     */
    val habito by viewModel.habitoActual.collectAsState()

    /**
     * Contexto actual de la aplicación.
     * Se utiliza para operaciones como exportar el PDF.
     */
    val context = LocalContext.current


    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("Nuevo Hábito") },

                /**
                 * Botón de navegación para volver a la pantalla anterior.
                 */
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->


        /**
         * Contenedor principal scrollable.
         *
         * Se hace scrollable para garantizar que el botón de exportación
         * a PDF sea accesible incluso en pantallas pequeñas.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            /**
             * Animación visual cuando aparece el hábito generado.
             */
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(400)) + slideInVertically(),
                exit = fadeOut(tween(300)) + slideOutVertically()
            ) {

                /**
                 * Tarjeta que muestra la información del hábito generado.
                 */
                Card(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        /**
                         * Si el hábito tiene una imagen asociada,
                         * se muestra en la parte superior de la tarjeta.
                         */
                        if (habito.imagenUrl.isNotBlank()) {

                            AsyncImage(
                                model = habito.imagenUrl,
                                contentDescription = habito.titulo,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )

                            Spacer(Modifier.height(12.dp))
                        }


                        /**
                         * Información del hábito generado.
                         */
                        Text(
                            habito.titulo,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(habito.descripcion)
                        Text("Categoría: ${habito.categoria}")
                        Text("Duración: ${habito.duracionMin} min")
                        Text("Dificultad: ${habito.dificultad}")
                    }
                }
            }


            Spacer(Modifier.height(16.dp))


            /**
             * Botones principales de interacción.
             */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                /**
                 * Genera un nuevo hábito aleatorio.
                 */
                Button(
                    onClick = {
                        viewModel.generarNuevoHabito()
                    }
                ) {

                    Text("Generar hábito aleatorio")
                }


                /**
                 * Guarda el hábito actualmente mostrado
                 * en la base de datos local.
                 */
                Button(
                    onClick = {
                        viewModel.insertarActual()
                    }
                ) {

                    Text("Guardar")
                }
            }


            Spacer(Modifier.height(16.dp))


            /**
             * Botón para exportar todos los hábitos guardados a PDF.
             *
             * Utiliza la clase PdfGenerator para generar el documento
             * y permitir compartirlo desde el dispositivo.
             */
            OutlinedButton(

                onClick = {

                    viewModel.obtenerTodosUnaVez { lista ->

                        try {

                            PdfGenerator.exportHabitsAsPdf(
                                context = context,
                                habits = lista
                            )

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }
                },

                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 12.dp)
            ) {

                Text("Exportar todos a PDF")
            }


            Spacer(Modifier.height(24.dp))
        }
    }
}