package com.example.habitos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.ui.viewmodel.ForumViewModel

/**
 * Pantalla utilizada para editar un tema existente del foro.
 *
 * Los temas representan las categorías principales dentro del sistema
 * de foro de la aplicación. Desde esta pantalla se permite modificar:
 *
 * - el nombre del tema
 * - la descripción asociada al tema
 *
 * Control de acceso:
 * - Solo los usuarios con rol ADMIN o SUPERADMIN pueden realizar
 *   modificaciones en los temas del foro.
 * - Los usuarios normales (USER) no tienen permisos para editar temas.
 *
 * Funcionalidades:
 * - Mostrar los datos actuales del tema.
 * - Permitir editar nombre y descripción.
 * - Validar que los campos obligatorios no estén vacíos.
 * - Guardar los cambios en la base de datos mediante ForumViewModel.
 * - Volver automáticamente a la pantalla anterior tras guardar los cambios.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarTema(
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel,
    temaId: Int,
    nombreInicial: String,
    descripcionInicial: String
) {

    /**
     * Usuario actualmente autenticado en la aplicación.
     * Se obtiene desde el AuthViewModel mediante StateFlow.
     */
    val user by authViewModel.usuarioActual.collectAsState()

    /**
     * Estado que almacena el nombre del tema a editar.
     * Se inicializa con el nombre actual del tema.
     */
    var nombre by remember { mutableStateOf(nombreInicial) }

    /**
     * Estado que almacena la descripción del tema.
     * Se inicializa con la descripción existente.
     */
    var descripcion by remember { mutableStateOf(descripcionInicial) }

    /**
     * Variable para mostrar errores de validación en la interfaz.
     */
    var error by remember { mutableStateOf<String?>(null) }


    /**
     * Control de seguridad de acceso.
     *
     * Solo los usuarios con rol ADMIN o SUPERADMIN
     * pueden acceder a esta funcionalidad.
     *
     * Si el usuario no cumple los permisos,
     * se vuelve automáticamente a la pantalla anterior.
     */
    if (user == null || user!!.role == "USER") {

        LaunchedEffect(Unit) {
            navController.popBackStack()
        }

        return
    }


    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     * Incluye la barra superior y el contenido de edición.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("✏️ Editar tema") },

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
         * Contenedor principal donde se organizan
         * los elementos del formulario de edición.
         */
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            /**
             * Campo de texto para editar el nombre del tema.
             */
            OutlinedTextField(

                value = nombre,

                onValueChange = {

                    nombre = it

                    // Se limpia el error si el usuario modifica el texto
                    error = null
                },

                label = { Text("Nombre del tema") },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))


            /**
             * Campo de texto para editar la descripción del tema.
             */
            OutlinedTextField(

                value = descripcion,

                onValueChange = {

                    descripcion = it

                    error = null
                },

                label = { Text("Descripción") },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))


            /**
             * Si existe un error de validación se muestra en pantalla.
             */
            if (error != null) {

                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(8.dp))
            }


            /**
             * Botón que guarda los cambios realizados en el tema.
             */
            Button(

                onClick = {

                    /**
                     * Validación de campos obligatorios.
                     */
                    if (nombre.isBlank() || descripcion.isBlank()) {

                        error = "Todos los campos son obligatorios"

                        return@Button
                    }

                    /**
                     * Se actualiza el tema mediante el ForumViewModel.
                     */
                    forumViewModel.editarTema(
                        id = temaId,
                        nombre = nombre,
                        descripcion = descripcion
                    )

                    /**
                     * Tras guardar los cambios se vuelve
                     * a la pantalla anterior.
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
