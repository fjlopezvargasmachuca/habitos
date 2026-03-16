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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.ui.viewmodel.ForumViewModel
import androidx.compose.material.icons.filled.PushPin

/**
 * Pantalla de listado de temas del foro.
 *
 * Esta pantalla muestra los diferentes temas disponibles dentro del
 * foro comunitario de la aplicación. Cada tema agrupa hilos de discusión
 * relacionados con una temática concreta.
 *
 * Funcionalidades disponibles en esta pantalla:
 *
 * - Visualizar la lista de temas del foro
 * - Acceder al contenido de un tema
 * - Crear nuevos temas (solo SUPERADMIN)
 * - Fijar o desfijar temas importantes (ADMIN y SUPERADMIN)
 * - Eliminar temas (ADMIN y SUPERADMIN)
 * - Editar temas existentes (ADMIN y SUPERADMIN)
 *
 * Los datos de los temas se obtienen desde ForumViewModel utilizando
 * StateFlow, lo que permite que la interfaz se actualice automáticamente
 * cuando cambian los datos en la base de datos.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTemasForo(
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel
) {

    /**
     * Lista de temas del foro obtenida desde el ViewModel.
     */
    val temas by forumViewModel.temas.collectAsState()

    /**
     * Usuario actualmente autenticado.
     * Se utiliza para comprobar permisos según su rol.
     */
    val user by authViewModel.usuarioActual.collectAsState()


    /**
     * Cuando la pantalla se muestra por primera vez se inicia
     * la escucha de los temas del foro.
     *
     * Esto permite recibir actualizaciones en tiempo real
     * cuando se crean, editan o eliminan temas.
     */
    LaunchedEffect(Unit) {
        forumViewModel.escucharTemas()
    }


    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("💬 Foro") },

                /**
                 * Botón de navegación para volver a la pantalla anterior.
                 */
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null
                        )
                    }
                },

                /**
                 * Acciones disponibles en la barra superior.
                 *
                 * Solo los usuarios con rol SUPERADMIN pueden
                 * crear nuevos temas dentro del foro.
                 */
                actions = {

                    if (user?.role == "SUPERADMIN") {

                        IconButton(
                            onClick = {
                                navController.navigate("foro/nuevo-tema")
                            }
                        ) {

                            Icon(
                                Icons.Filled.Add,
                                null
                            )
                        }
                    }
                }
            )
        }

    ) { padding ->


        /**
         * Lista vertical que muestra todos los temas del foro.
         *
         * LazyColumn permite renderizar únicamente los elementos
         * visibles en pantalla, mejorando el rendimiento cuando
         * hay muchos temas.
         */
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            /**
             * Se recorre la lista de temas y se genera una tarjeta
             * para cada uno de ellos.
             */
            items(temas) { tema ->


                /**
                 * Tarjeta que representa un tema del foro.
                 */
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .clickable {

                                /**
                                 * Al pulsar sobre el tema se navega a la pantalla
                                 * que muestra los hilos de discusión asociados.
                                 */
                                navController.navigate("foro/tema/${tema.id}")
                            }
                            .padding(16.dp)
                    ) {


                        // ───────────── CABECERA DEL TEMA ─────────────

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            /**
                             * Nombre del tema.
                             *
                             * Si el tema está fijado se muestra un icono
                             * de pin para destacarlo visualmente.
                             */
                            Text(
                                if (tema.fijado) "📌 ${tema.nombre}" else tema.nombre,
                                style = MaterialTheme.typography.titleMedium
                            )


                            /**
                             * Botón para fijar o desfijar el tema.
                             *
                             * Solo disponible para usuarios con rol
                             * ADMIN o SUPERADMIN.
                             */
                            if (user != null && user!!.role != "USER") {

                                IconButton(
                                    onClick = {
                                        forumViewModel.toggleFijarTema(tema, user!!)
                                    }
                                ) {

                                    Icon(
                                        Icons.Filled.PushPin,
                                        contentDescription = "Fijar tema"
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(4.dp))


                        /**
                         * Descripción del tema del foro.
                         */
                        Text(tema.descripcion)



                        // ───────────── ACCIONES DE MODERACIÓN ─────────────

                        /**
                         * Botón para eliminar el tema del foro.
                         *
                         * Solo disponible para ADMIN y SUPERADMIN.
                         */
                        if (user != null && user!!.role != "USER") {

                            Spacer(Modifier.height(8.dp))

                            TextButton(
                                onClick = {
                                    forumViewModel.borrarTema(tema.id)
                                }
                            ) {

                                Icon(
                                    Icons.Filled.Delete,
                                    null,
                                    tint = MaterialTheme.colorScheme.error
                                )

                                Spacer(Modifier.width(4.dp))

                                Text(
                                    "Eliminar tema",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }


                        // ───────────── EDITAR TEMA ─────────────

                        /**
                         * Permite editar el nombre y la descripción
                         * del tema seleccionado.
                         *
                         * Solo disponible para ADMIN y SUPERADMIN.
                         */
                        if (user != null && user!!.role != "USER") {

                            TextButton(
                                onClick = {

                                    navController.navigate(
                                        "foro/editar-tema/${tema.id}/${tema.nombre}/${tema.descripcion}"
                                    )
                                }
                            ) {

                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Editar"
                                )

                                Spacer(Modifier.width(4.dp))

                                Text("Editar tema")
                            }
                        }
                    }
                }
            }
        }
    }
}