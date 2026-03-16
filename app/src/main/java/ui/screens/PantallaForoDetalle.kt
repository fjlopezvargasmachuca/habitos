package com.example.habitos.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.ui.viewmodel.ForumViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla que muestra el detalle de un hilo del foro.
 *
 * En esta pantalla se visualizan todos los mensajes pertenecientes
 * a un hilo concreto del foro y se permite interactuar con ellos.
 *
 * Funcionalidades principales:
 *
 * - Mostrar la lista de mensajes del hilo seleccionado.
 * - Permitir a los usuarios enviar nuevos mensajes.
 * - Permitir dar "like" a los mensajes publicados.
 * - Mostrar el número total de likes de cada mensaje.
 * - Aplicar una animación visual al pulsar el botón de like.
 *
 * Los mensajes se obtienen desde el ForumViewModel mediante StateFlow,
 * lo que permite que la interfaz se actualice automáticamente cuando
 * se añaden nuevos mensajes o cambian los likes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaForoDetalle(
    threadId: String,
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel
) {

    /**
     * Lista de mensajes del hilo actual.
     */
    val mensajes by forumViewModel.mensajes.collectAsState()

    /**
     * Usuario actualmente autenticado en la aplicación.
     */
    val user by authViewModel.usuarioActual.collectAsState()

    /**
     * Texto del nuevo mensaje que el usuario está escribiendo.
     */
    var texto by remember { mutableStateOf("") }

    /**
     * CoroutineScope utilizado para ejecutar animaciones
     * y operaciones asincrónicas.
     */
    val scope = rememberCoroutineScope()


    /**
     * Al cargar la pantalla se solicita al ViewModel que
     * empiece a escuchar los mensajes del hilo seleccionado.
     */
    LaunchedEffect(threadId) {
        forumViewModel.escucharMensajes(threadId)
    }


    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("💬 Mensajes") },

                /**
                 * Botón para volver a la pantalla anterior.
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

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            /**
             * Lista de mensajes del hilo.
             * Se muestra en un LazyColumn para optimizar
             * el rendimiento cuando hay muchos mensajes.
             */
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                items(mensajes) { msg ->

                    /**
                     * Estado local que indica si el usuario actual
                     * ha dado "like" al mensaje.
                     */
                    var liked by remember(msg.id) { mutableStateOf(false) }

                    /**
                     * Animación utilizada para crear un efecto
                     * "bounce" al pulsar el icono de like.
                     */
                    val scale = remember { Animatable(1f) }


                    /**
                     * Se comprueba si el usuario actual ha dado like
                     * al mensaje cuando se carga o actualiza.
                     */
                    LaunchedEffect(msg.likes, user?.id) {

                        val currentUser = user ?: return@LaunchedEffect

                        forumViewModel.usuarioHaDadoLike(
                            mensajeId = msg.id,
                            userId = currentUser.id
                        ) { result ->
                            liked = result
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        Column(Modifier.padding(12.dp)) {

                            /**
                             * Información del autor del mensaje.
                             */
                            Text(
                                text = "${msg.autorNick} (${msg.autorProvincia})",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(Modifier.height(4.dp))


                            /**
                             * Contenido del mensaje.
                             */
                            Text(msg.contenido)

                            Spacer(Modifier.height(8.dp))


                            /**
                             * Sección de likes del mensaje.
                             */
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    imageVector =
                                        if (liked)
                                            Icons.Filled.Favorite
                                        else
                                            Icons.Outlined.FavoriteBorder,

                                    contentDescription = "Like",

                                    tint = MaterialTheme.colorScheme.error,

                                    modifier = Modifier
                                        .size(22.dp)

                                        /**
                                         * Aplicación de la animación
                                         * de escala al icono.
                                         */
                                        .graphicsLayer {
                                            scaleX = scale.value
                                            scaleY = scale.value
                                        }

                                        /**
                                         * Acción al pulsar el icono.
                                         */
                                        .clickable(enabled = user != null) {

                                            scope.launch {

                                                scale.snapTo(1f)

                                                scale.animateTo(
                                                    1.4f,
                                                    animationSpec = spring(
                                                        dampingRatio = 0.4f
                                                    )
                                                )

                                                scale.animateTo(1f)
                                            }

                                            /**
                                             * Se alterna el estado del like
                                             * en la base de datos.
                                             */
                                            forumViewModel.toggleLike(
                                                mensajeId = msg.id,
                                                userId = user!!.id,
                                                threadId = threadId
                                            )
                                        }
                                )

                                Spacer(Modifier.width(6.dp))

                                /**
                                 * Número total de likes del mensaje.
                                 */
                                Text(
                                    text = msg.likes.toString(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))


            /**
             * Campo de texto para escribir un nuevo mensaje.
             */
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Escribe un mensaje") }
            )

            Spacer(Modifier.height(8.dp))


            /**
             * Botón para enviar el mensaje al hilo.
             */
            Button(
                onClick = {

                    user?.let {

                        forumViewModel.enviarMensaje(
                            threadId,
                            texto,
                            it
                        )

                        texto = ""
                    }
                },

                enabled = texto.isNotBlank() && user != null,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Enviar")
            }
        }
    }
}