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
 * Pantalla que permite crear un nuevo tema dentro del foro.
 *
 * Esta funcionalidad está restringida únicamente a usuarios con
 * rol de SUPERADMIN, ya que los temas representan las categorías
 * principales del foro.
 *
 * Desde esta pantalla el administrador puede:
 * - Definir el nombre del tema.
 * - Añadir una descripción explicativa.
 * - Crear el nuevo tema dentro del sistema del foro.
 *
 * Si un usuario no autorizado intenta acceder a esta pantalla,
 * será redirigido automáticamente a la pantalla anterior.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearTema(
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel
) {

    /**
     * Usuario actualmente autenticado.
     * Se obtiene mediante StateFlow desde AuthViewModel.
     */
    val user by authViewModel.usuarioActual.collectAsState()

    /**
     * Estado que almacena el nombre del tema que se desea crear.
     */
    var nombre by remember { mutableStateOf("") }

    /**
     * Estado que almacena la descripción del tema.
     */
    var descripcion by remember { mutableStateOf("") }

    /**
     * Estado que almacena posibles mensajes de error
     * en la validación del formulario.
     */
    var error by remember { mutableStateOf<String?>(null) }

    /**
     * Control de acceso:
     *
     * Solo los usuarios con rol "SUPERADMIN" pueden crear temas.
     * Si el usuario no cumple esta condición, se vuelve automáticamente
     * a la pantalla anterior.
     */
    if (user == null || user!!.role != "SUPERADMIN") {

        LaunchedEffect(Unit) {
            navController.popBackStack()
        }

        return
    }

    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     * Incluye la barra superior y el contenido principal.
     */
    Scaffold(
        topBar = {

            /**
             * Barra superior de navegación.
             */
            TopAppBar(

                /**
                 * Título de la pantalla.
                 */
                title = { Text("➕ Crear tema") },

                /**
                 * Botón de navegación para volver a la pantalla anterior.
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
         * Contenedor principal donde se organiza
         * el formulario de creación del tema.
         */
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            /**
             * Campo de texto donde se introduce
             * el nombre del nuevo tema del foro.
             */
            OutlinedTextField(
                value = nombre,
                onValueChange = {

                    /**
                     * Actualizar el nombre conforme escribe el usuario y limpia posibles errores.
                     */
                    nombre = it
                    error = null
                },
                label = { Text("Nombre del tema") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            /**
             * Campo de texto donde se introduce
             * la descripción del tema.
             */
            OutlinedTextField(
                value = descripcion,
                onValueChange = {

                    /**
                     * Actualizar la descripción introducida y elimina mensajes de error previos.
                     */
                    descripcion = it
                    error = null
                },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            /**
             * Si existe un error de validación del formulario,
             * se muestra al usuario en color rojo.
             */
            if (error != null) {

                Text(error!!, color = MaterialTheme.colorScheme.error)

                Spacer(Modifier.height(8.dp))
            }

            /**
             * Botón que permite crear el nuevo tema del foro.
             */
            Button(
                onClick = {

                    /**
                     * Validación del formulario:
                     * Todos los campos deben estar rellenados.
                     */
                    if (nombre.isBlank() || descripcion.isBlank()) {

                        error = "Todos los campos son obligatorios"

                        return@Button
                    }

                    /**
                     * Se llama al ViewModel para crear
                     * el nuevo tema en el sistema del foro.
                     */
                    forumViewModel.crearTema(nombre, descripcion)

                    /**
                     * Una vez creado el tema,
                     * se vuelve automáticamente a la pantalla anterior.
                     */
                    navController.popBackStack()
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                /**
                 * Texto mostrado dentro del botón.
                 */
                Text("Crear tema")
            }
        }
    }
}