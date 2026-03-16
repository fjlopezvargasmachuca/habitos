package com.example.habitos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.ui.viewmodel.ForumViewModel

/**
 * Pantalla que muestra los hilos pertenecientes a un tema del foro.
 *
 * Cada tema del foro agrupa diferentes hilos de discusión donde los usuarios
 * pueden publicar mensajes y debatir sobre hábitos saludables.
 *
 * Funcionalidades principales:
 *
 * - Mostrar todos los hilos asociados a un tema concreto.
 * - Permitir acceder al detalle de un hilo.
 * - Permitir crear un nuevo hilo (usuarios no baneados).
 * - Permitir editar un hilo (autor del hilo o SUPERADMIN).
 * - Permitir eliminar un hilo (solo SUPERADMIN).
 *
 * Los datos se obtienen desde el ForumViewModel mediante StateFlow,
 * permitiendo que la interfaz se actualice automáticamente cuando
 * cambian los datos del foro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHilosTema(
    topicId: String,
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel
) {

    /**
     * Lista de hilos pertenecientes al tema seleccionado.
     */
    val hilos by forumViewModel.hilos.collectAsState()

    /**
     * Usuario actualmente autenticado en la aplicación.
     */
    val user by authViewModel.usuarioActual.collectAsState()

    /**
     * Al abrir la pantalla se solicita al ViewModel que
     * empiece a escuchar los hilos del tema seleccionado.
     */
    LaunchedEffect(topicId) {
        forumViewModel.escucharHilos(topicId)
    }

    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text("🧵 Hilos del tema") },

                /**
                 * Botón para volver a la pantalla anterior.
                 */
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },

                /**
                 * Botón para crear un nuevo hilo.
                 * Solo aparece si el usuario está autenticado
                 * y no está baneado.
                 */
                actions = {
                    if (user != null && !user!!.baneado) {
                        IconButton(
                            onClick = {
                                navController.navigate("foro/tema/$topicId/nuevo-hilo")
                            }
                        ) {
                            Icon(Icons.Filled.Add, null)
                        }
                    }
                }
            )
        }

    ) { padding ->

        /**
         * Si no existen hilos en el tema se muestra
         * un mensaje informativo.
         */
        if (hilos.isEmpty()) {

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text("No hay hilos todavía 😶")
            }

        } else {

            /**
             * Lista vertical que muestra todos los hilos del tema.
             */
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                items(hilos) { hilo ->

                    /**
                     * Determina si el usuario tiene permiso para editar el hilo.
                     *
                     * Condiciones:
                     * - Ser SUPERADMIN
                     * - Ser el autor del hilo
                     */
                    val puedeEditar =
                        user != null &&
                                (user!!.role == "SUPERADMIN" || user!!.id == hilo.autorId)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .clickable {

                                    /**
                                     * Navega al detalle del hilo
                                     * para ver los mensajes.
                                     */
                                    navController.navigate("foro/hilo/${hilo.id}")
                                }
                                .padding(16.dp)
                        ) {

                            /**
                             * Título del hilo.
                             */
                            Text(
                                hilo.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(4.dp))

                            /**
                             * Autor del hilo.
                             */
                            Text(
                                "por ${hilo.autorNick}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(Modifier.height(8.dp))


                            /**
                             * Botón de edición del hilo.
                             * Disponible para el autor o SUPERADMIN.
                             */
                            if (puedeEditar) {

                                TextButton(
                                    onClick = {
                                        navController.navigate("foro/editar-hilo/${hilo.id}")
                                    }
                                ) {

                                    Icon(Icons.Filled.Edit, null)

                                    Spacer(Modifier.width(4.dp))

                                    Text("Editar")
                                }
                            }


                            /**
                             * Botón de eliminación del hilo.
                             * Solo disponible para SUPERADMIN.
                             */
                            if (user?.role == "SUPERADMIN") {

                                TextButton(
                                    onClick = {

                                        forumViewModel.borrarHilo(
                                            hiloId = hilo.id,
                                            topicId = topicId
                                        )
                                    }
                                ) {

                                    Icon(
                                        Icons.Filled.Delete,
                                        null,
                                        tint = MaterialTheme.colorScheme.error
                                    )

                                    Spacer(Modifier.width(4.dp))

                                    Text(
                                        "Eliminar",
                                        color = MaterialTheme.colorScheme.error
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