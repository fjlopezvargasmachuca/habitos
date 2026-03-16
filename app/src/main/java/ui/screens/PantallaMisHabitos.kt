package com.example.habitos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.habitos.ui.viewmodel.HabitViewModel

/**
 * Pantalla que muestra los hábitos guardados por el usuario.
 *
 * Esta pantalla permite al usuario visualizar todos los hábitos que ha
 * guardado previamente en la aplicación. Desde aquí puede consultar la
 * información de cada hábito y eliminar aquellos que ya no desee mantener.
 *
 * Funcionalidades principales:
 *
 * - Mostrar la lista de hábitos almacenados en la base de datos local.
 * - Mostrar información detallada de cada hábito:
 *      - imagen
 *      - título
 *      - categoría
 *      - duración estimada
 *      - dificultad
 * - Permitir eliminar hábitos guardados.
 *
 * Los datos se obtienen desde el HabitViewModel mediante StateFlow,
 * lo que permite que la interfaz se actualice automáticamente cuando
 * se añaden o eliminan hábitos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMisHabitos(
    navController: NavController,
    habitViewModel: HabitViewModel
) {

    /**
     * Esquema de colores activo del tema de la aplicación.
     */
    val colorScheme = MaterialTheme.colorScheme

    /**
     * Lista de hábitos guardados por el usuario.
     */
    val listaHabitos by habitViewModel.listaHabitos.collectAsState()

    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("Mis hábitos guardados") },

                /**
                 * Botón de navegación para volver a la pantalla anterior.
                 */
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }

    ) { padding ->

        /**
         * Contenedor principal de la pantalla.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(padding)
                .padding(16.dp)
        ) {

            /**
             * Si el usuario aún no ha guardado hábitos
             * se muestra un mensaje informativo.
             */
            if (listaHabitos.isEmpty()) {

                Text(
                    "No tienes hábitos guardados aún.",
                    style = MaterialTheme.typography.bodyLarge
                )

            } else {

                /**
                 * Lista de hábitos guardados.
                 */
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(listaHabitos, key = { it.id }) { hab ->

                        /**
                         * Cada hábito se representa mediante una tarjeta.
                         */
                        Card(
                            modifier = Modifier.fillMaxWidth(),

                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.secondaryContainer
                            ),

                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),

                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                /**
                                 * Información principal del hábito.
                                 */
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    /**
                                     * Si el hábito tiene imagen asociada
                                     * se muestra en la parte superior.
                                     */
                                    if (hab.imagenUrl.isNotBlank()) {

                                        AsyncImage(
                                            model = hab.imagenUrl,
                                            contentDescription = hab.titulo,

                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(140.dp)
                                        )

                                        Spacer(Modifier.height(8.dp))
                                    }

                                    /**
                                     * Título del hábito.
                                     */
                                    Text(
                                        hab.titulo,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(Modifier.height(4.dp))

                                    /**
                                     * Información adicional del hábito.
                                     */
                                    Text("Categoría: ${hab.categoria}")
                                    Text("Duración: ${hab.duracionMin} min")
                                    Text("Dificultad: ${hab.dificultad}")
                                }


                                /**
                                 * Botón para eliminar el hábito guardado.
                                 */
                                IconButton(
                                    onClick = {

                                        habitViewModel.borrarHabito(hab.id)
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}