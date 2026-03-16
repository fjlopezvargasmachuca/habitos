package com.example.habitos.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast

/**
 * Pantalla de contacto de la aplicación.
 *
 * Esta pantalla permite al usuario enviar un correo electrónico
 * directamente al administrador de la aplicación utilizando
 * el cliente de correo instalado en el dispositivo.
 *
 * El usuario puede introducir:
 * - Un asunto para el correo.
 * - Un mensaje con la consulta o comentario.
 *
 * Al pulsar el botón "Enviar correo", se abrirá la aplicación
 * de correo configurada en el dispositivo con el asunto y
 * el mensaje ya rellenados automáticamente.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaContacto(navController: NavController) {

    /**
     * Contexto actual de la aplicación.
     *
     * Se utiliza para:
     * - Mostrar mensajes emergentes (Toast).
     * - Lanzar el intent que abre la aplicación de correo.
     */
    val context = LocalContext.current

    /**
     * Estado que almacena el asunto del correo
     * introducido por el usuario.
     */
    var asunto by remember { mutableStateOf("") }

    /**
     * Estado que almacena el mensaje que el usuario
     * desea enviar al administrador.
     */
    var mensaje by remember { mutableStateOf("") }

    /**
     * Estructura principal de la pantalla.
     *
     * Se utiliza un Scaffold para mantener una estructura
     * estándar con barra superior (TopAppBar) y contenido.
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
                title = { Text("Contacto") },

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
         * Contenedor principal de la pantalla.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /**
             * Texto informativo que indica al usuario
             * la finalidad de la pantalla.
             */
            Text(
                text = "Contacta con el administrador",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(16.dp))

            /**
             * Campo de texto donde el usuario introduce
             * el asunto del correo electrónico.
             */
            OutlinedTextField(
                value = asunto,
                onValueChange = { asunto = it },
                label = { Text("Asunto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            /**
             * Campo de texto donde el usuario escribe
             * el contenido del mensaje que quiere enviar.
             *
             * Se limita a un máximo de 6 líneas para mantener
             * una interfaz limpia y evitar que el campo crezca
             * excesivamente.
             */
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text("Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 6
            )

            Spacer(Modifier.height(20.dp))

            /**
             * Botón que permite enviar el correo al administrador.
             */
            Button(
                onClick = {

                    /**
                     * Evita enviar el correo si el mensaje está vacío.
                     * Se muestra un aviso al usuario mediante un Toast.
                     */
                    if (mensaje.isBlank()) {

                        Toast.makeText(
                            context,
                            "Escribe un mensaje antes de enviar",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    /**
                     * Construcción de la URI de tipo mailto.
                     *
                     * Incluye:
                     * - Dirección de correo del administrador
                     * - Asunto del mensaje
                     * - Cuerpo del mensaje
                     *
                     * Uri.encode se utiliza para evitar problemas
                     * con espacios o caracteres especiales.
                     */
                    val uri = Uri.parse(
                        "mailto:machuka@gmail.com" +
                                "?subject=" + Uri.encode(asunto) +
                                "&body=" + Uri.encode(mensaje)
                    )

                    /**
                     * Intent que abre el cliente de correo
                     * instalado en el dispositivo.
                     */
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = uri
                    }

                    /**
                     * Lanzamiento del cliente de correo.
                     */
                    context.startActivity(intent)
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                /**
                 * Texto mostrado dentro del botón.
                 */
                Text("Enviar correo")
            }
        }
    }
}
