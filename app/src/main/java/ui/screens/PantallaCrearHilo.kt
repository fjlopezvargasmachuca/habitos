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
 * Pantalla que permite al usuario crear un nuevo hilo dentro de un tema del foro.
 *
 * Esta pantalla forma parte del sistema de foro de la aplicación y permite
 * a los usuarios autenticados crear nuevas conversaciones dentro de un tema
 * concreto identificado por `topicId`.
 *
 * Funcionalidades principales:
 * - Introducir el título del nuevo hilo.
 * - Validar que el título no esté vacío.
 * - Crear el hilo utilizando el ForumViewModel.
 * - Volver automáticamente a la pantalla anterior tras crearlo.
 *
 * Restricciones:
 * - Si el usuario no está autenticado o está baneado, no puede acceder
 *   a esta pantalla y se le redirige automáticamente atrás.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearHilo(
    navController: NavController,
    forumViewModel: ForumViewModel,
    authViewModel: AuthViewModel,
    topicId: String
) {

    /**
     * Usuario actualmente autenticado.
     * Se obtiene mediante StateFlow desde AuthViewModel.
     */
    val user by authViewModel.usuarioActual.collectAsState()

    /**
     * Estado que almacena el título introducido
     * por el usuario para el nuevo hilo.
     */
    var titulo by remember { mutableStateOf("") }

    /**
     * Estado que almacena posibles mensajes de error
     * en la validación del formulario.
     */
    var error by remember { mutableStateOf<String?>(null) }

    /**
     * Si el usuario no está autenticado o está baneado,
     * se vuelve automáticamente a la pantalla anterior.
     */
    if (user == null || user!!.baneado) {
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
                title = { Text("➕ Crear hilo") },

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
         * Contenedor principal donde se organiza
         * el formulario para crear el nuevo hilo.
         */
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            /**
             * Campo de texto donde el usuario introduce
             * el título del nuevo hilo.
             */
            OutlinedTextField(
                value = titulo,
                onValueChange = {

                    /**
                     * Actualiza el título conforme escribe el usuario
                     * y limpia posibles mensajes de error.
                     */
                    titulo = it
                    error = null
                },
                label = { Text("Título del hilo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            /**
             * Si existe un error de validación,
             * se muestra al usuario en color rojo.
             */
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            /**
             * Botón que permite crear el nuevo hilo.
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
                     * Se llama al ViewModel para crear el nuevo hilo
                     * dentro del tema correspondiente.
                     */
                    forumViewModel.crearHilo(
                        topicId = topicId,
                        titulo = titulo,
                        user = user!!
                    )

                    /**
                     * Una vez creado el hilo, se vuelve a la pantalla anterior.
                     */
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                /**
                 * Texto mostrado dentro del botón.
                 */
                Text("Crear hilo")
            }
        }
    }
}