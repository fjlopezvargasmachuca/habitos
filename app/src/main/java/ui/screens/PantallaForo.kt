package com.example.habitos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.ui.viewmodel.ForumViewModel

/**
 * Pantalla principal del foro de la aplicación.
 *
 * En esta pantalla se muestran todos los temas disponibles del foro,
 * que funcionan como categorías de discusión donde los usuarios
 * pueden crear hilos y participar en conversaciones.
 *
 * Funcionalidades principales:
 *
 * - Cargar y mostrar los temas del foro almacenados en la base de datos.
 * - Permitir navegar hacia los hilos de cada tema.
 * - Escuchar cambios en los temas mediante StateFlow.
 *
 * La información se obtiene desde el ForumViewModel, que actúa como
 * intermediario entre la interfaz de usuario y la capa de datos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaForo(
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel
) {

    /**
     * Lista de temas del foro.
     *
     * Se obtiene como un flujo reactivo (StateFlow) desde el ViewModel,
     * por lo que la interfaz se actualizará automáticamente si cambian
     * los datos en la base de datos.
     */
    val temas by forumViewModel.temas.collectAsState()

    /**
     * Al abrir la pantalla se solicita al ViewModel que empiece
     * a escuchar los temas del foro desde la base de datos.
     */
    LaunchedEffect(Unit) {
        forumViewModel.escucharTemas()
    }

    /**
     * Estructura principal de la pantalla utilizando Scaffold,
     * que incluye la barra superior y el contenido principal.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("💬 Foro") },

                /**
                 * Botón de navegación que permite volver
                 * a la pantalla anterior.
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
         * Lista vertical que muestra todos los temas disponibles.
         */
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            /**
             * Cada elemento representa un tema del foro.
             */
            items(temas) { tema ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)

                        /**
                         * Al pulsar un tema se navega a la pantalla
                         * que muestra los hilos asociados a ese tema.
                         */
                        .clickable {

                            navController.navigate("foro/tema/${tema.id}")
                        }
                ) {

                    Column(Modifier.padding(12.dp)) {

                        /**
                         * Nombre del tema del foro.
                         */
                        Text(
                            text = tema.nombre,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(4.dp))

                        /**
                         * Breve descripción del tema.
                         */
                        Text(
                            text = tema.descripcion,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}