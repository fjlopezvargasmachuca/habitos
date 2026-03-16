package com.example.habitos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.data.utils.HabitGenerator

/**
 * Pantalla que muestra todas las categorías disponibles de hábitos.
 *
 * Esta pantalla permite al usuario seleccionar una categoría concreta
 * para visualizar los hábitos recomendados asociados a ella.
 *
 * Cada categoría se representa mediante un botón con un icono
 * representativo para mejorar la experiencia visual del usuario.
 *
 * Al seleccionar una categoría se navega a la pantalla que muestra
 * los hábitos disponibles para esa categoría concreta.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCategorias(navController: NavController) {

    /**
     * Listar de categorías disponibles generadas por HabitGenerator.
     */
    val categorias = HabitGenerator().CATEGORIAS

    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                /**
                 * Título de la pantalla.
                 */
                title = { Text("Categorías") },

                /**
                 * Botón de navegación que permite volver
                 * a la pantalla anterior.
                 */
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        /**
         * Contenedor principal de la pantalla.
         *
         * Se utiliza un Column con scroll vertical para permitir
         * visualizar todas las categorías incluso en pantallas
         * con menor altura.
         */
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {

            /**
             * Texto introductorio que indica al usuario
             * que debe seleccionar una categoría.
             */
            Text(
                text = "Selecciona una categoría:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(12.dp))

            /**
             * Se recorre la lista de categorías para generar
             * dinámicamente un botón para cada una de ellas.
             */
            categorias.forEach { cat ->

                /**
                 * Selección del icono asociado a cada categoría.
                 *
                 * Se utilizan iconos representativos para mejorar
                 * la comprensión visual del tipo de hábito.
                 */
                val icono = when (cat.lowercase()) {

                    "salud" -> Icons.Filled.Favorite
                    "ejercicio" -> Icons.AutoMirrored.Filled.DirectionsRun
                    "bienestar" -> Icons.Filled.Spa
                    "nutrición" -> Icons.Filled.Restaurant
                    "sueño" -> Icons.Filled.Bedtime
                    "productividad" -> Icons.AutoMirrored.Filled.TrendingUp
                    "organización" -> Icons.Filled.Folder
                    "estudio" -> Icons.Filled.School

                    /**
                     * Icono genérico utilizado si la categoría
                     * no coincide con ninguna de las anteriores.
                     */
                    else -> Icons.Filled.Category
                }

                /**
                 * Botón que representa la categoría.
                 *
                 * Al pulsarlo se navega a la pantalla que
                 * muestra los hábitos correspondientes
                 * a la categoría seleccionada.
                 */
                Button(

                    onClick = {

                        /**
                         * Navegación a la pantalla de hábitos
                         * pasando la categoría seleccionada
                         * como parámetro en la ruta.
                         */
                        navController.navigate("categoria/$cat")
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    /**
                     * Icono representativo de la categoría.
                     */
                    Icon(icono, contentDescription = null)

                    Spacer(Modifier.width(8.dp))

                    /**
                     * Nombre de la categoría.
                     */
                    Text(cat)
                }
            }
        }
    }
}

