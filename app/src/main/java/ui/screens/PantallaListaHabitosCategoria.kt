package com.example.habitos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.habitos.ui.viewmodel.HabitViewModel
import com.example.habitos.data.utils.HabitGenerator
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

/**
 * Pantalla que muestra la lista de hábitos disponibles dentro
 * de una categoría concreta.
 *
 * Esta pantalla permite al usuario explorar diferentes hábitos
 * organizados por categorías (salud, ejercicio, bienestar, etc.).
 *
 * Funcionalidades principales:
 *
 * - Generar una lista de hábitos según la categoría seleccionada.
 * - Mostrar información detallada de cada hábito (título, descripción e imagen).
 * - Permitir guardar un hábito en la base de datos local.
 * - Permitir navegar a la pantalla de hábitos guardados.
 *
 * Los hábitos mostrados se generan mediante la clase HabitGenerator,
 * que contiene listas predefinidas de hábitos para cada categoría.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaHabitosCategoria(
    categoria: String,
    viewModel: HabitViewModel,
    navController: NavController
) {

    /**
     * Contexto necesario para mostrar mensajes Toast.
     */
    val context = LocalContext.current

    /**
     * Generador de hábitos utilizado para crear
     * la lista correspondiente a la categoría seleccionada.
     */
    val generator = remember { HabitGenerator() }

    /**
     * Lista de hábitos generados para la categoría actual.
     * Se recalcula únicamente cuando cambia la categoría.
     */
    val lista = remember(categoria) {
        generator.generarHabitosPorCategoria(categoria)
    }

    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                /**
                 * El título muestra el nombre de la categoría seleccionada.
                 */
                title = { Text(categoria) },

                /**
                 * Botón de navegación para volver atrás.
                 */
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }

    ) { padding ->

        /**
         * Lista vertical que muestra los hábitos de la categoría.
         */
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),

            contentPadding = PaddingValues(vertical = 8.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(lista, key = { it.titulo }) { hab ->

                /**
                 * Cada hábito se muestra dentro de una tarjeta.
                 */
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        Modifier.padding(12.dp)
                    ) {

                        /**
                         * Si el hábito tiene imagen asociada
                         * se muestra en la parte superior de la tarjeta.
                         */
                        if (hab.imagenUrl.isNotBlank()) {

                            AsyncImage(
                                model = hab.imagenUrl,
                                contentDescription = hab.titulo,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
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

                        Spacer(Modifier.height(6.dp))

                        /**
                         * Descripción del hábito.
                         */
                        Text(
                            hab.descripcion,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(10.dp))

                        /**
                         * Botones de acción para el hábito.
                         */
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            /**
                             * Botón para guardar el hábito
                             * en la base de datos del usuario.
                             */
                            Button(
                                onClick = {

                                    val guardado = viewModel.insertarHabito(hab)

                                    if (!guardado) {

                                        Toast.makeText(
                                            context,
                                            "Este hábito ya está guardado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Text("Guardar hábito")
                            }

                            /**
                             * Botón para navegar a la pantalla
                             * donde se muestran los hábitos guardados.
                             */
                            OutlinedButton(
                                onClick = {
                                    navController.navigate("misHabitos")
                                }
                            ) {
                                Text("Ver guardados")
                            }
                        }
                    }
                }
            }
        }
    }
}