package com.example.habitos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.ui.viewmodel.ForumViewModel

/**
 * Pantalla utilizada para editar el título de un hilo del foro.
 *
 * Permite al usuario modificar el nombre de un hilo previamente creado.
 * Esta funcionalidad suele estar restringida al autor del hilo o a usuarios
 * con permisos de administración.
 *
 * Funcionalidades principales:
 *
 * - Cargar el título actual del hilo.
 * - Permitir modificar el título mediante un campo de texto.
 * - Validar que el nuevo título no esté vacío.
 * - Guardar los cambios en la base de datos mediante el ForumViewModel.
 * - Volver automáticamente a la pantalla anterior tras guardar los cambios.
 *
 * Si el usuario no está autenticado, se le redirige automáticamente
 * a la pantalla anterior para evitar accesos no autorizados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarHilo(
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel,
    hiloId: Int,
    tituloInicial: String
) {

    /**
     * Usuario actualmente autenticado en la aplicación.
     * Se obtiene mediante el StateFlow del AuthViewModel.
     */
    val user by authViewModel.usuarioActual.collectAsState()

    /**
     * Estado que almacena el nuevo título del hilo.
     * Inicialmente contiene el título original recibido como parámetro.
     */
    var titulo by remember { mutableStateOf(tituloInicial) }

    /**
     * Mensaje de error que se mostrará si la validación del título falla.
     */
    var error by remember { mutableStateOf<String?>(null) }


    /**
     * Control de acceso:
     * Si no hay usuario autenticado se vuelve automáticamente
     * a la pantalla anterior.
     */
    if (user == null) {

        LaunchedEffect(Unit) {
            navController.popBackStack()
        }

        return
    }


    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     * Incluye barra superior y contenido principal.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("✏️ Editar hilo") },

                /**
                 * Botón de navegación para volver atrás.
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
         * Contenedor vertical donde se organizan
         * los elementos de la interfaz.
         */
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            /**
             * Campo de texto que permite modificar el título del hilo.
             */
            OutlinedTextField(

                value = titulo,

                onValueChange = {

                    titulo = it

                    // Si el usuario modifica el texto se limpia el error
                    error = null
                },

                label = { Text("Título del hilo") },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))


            /**
             * Si existe un error de validación se muestra en pantalla.
             */
            error?.let {

                Text(
                    it,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(8.dp))
            }


            /**
             * Botón encargado de guardar los cambios realizados.
             */
            Button(
                onClick = {

                    /**
                     * Validación: el título no puede estar vacío.
                     */
                    if (titulo.isBlank()) {

                        error = "El título no puede estar vacío"

                        return@Button
                    }

                    /**
                     * Se llama al ViewModel para actualizar el título del hilo.
                     */
                    forumViewModel.editarHilo(
                        hiloId = hiloId,
                        nuevoTitulo = titulo,
                        user = user!!
                    )

                    /**
                     * Tras guardar los cambios se vuelve a la pantalla anterior.
                     */
                    navController.popBackStack()
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Guardar cambios")
            }
        }
    }
}
