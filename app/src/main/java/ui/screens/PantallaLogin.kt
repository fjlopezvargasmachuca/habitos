package com.example.habitos.ui.screens

import androidx.compose.ui.res.painterResource
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.R
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.width

/**
 * Pantalla de inicio de sesión de la aplicación.
 *
 * Esta pantalla permite a los usuarios autenticarse en el sistema
 * utilizando dos métodos diferentes:
 *
 * - Inicio de sesión mediante email y contraseña.
 * - Inicio de sesión mediante cuenta de Google.
 *
 * Funcionalidades principales:
 *
 * - Introducir credenciales de acceso.
 * - Mostrar u ocultar la contraseña introducida.
 * - Mostrar mensajes de error si el login falla.
 * - Acceder al sistema mediante Google Sign-In.
 * - Redirigir al usuario a la pantalla de registro si no tiene cuenta.
 *
 * Una vez que el usuario se autentica correctamente,
 * la pantalla se cierra automáticamente y se vuelve
 * a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    /**
     * Estado que almacena el email introducido por el usuario.
     */
    var email by remember { mutableStateOf("") }

    /**
     * Estado que almacena la contraseña introducida.
     */
    var password by remember { mutableStateOf("") }

    /**
     * Controla si la contraseña se muestra visible o enmascarada.
     */
    var passVisible by remember { mutableStateOf(false) }

    /**
     * Usuario actualmente autenticado.
     */
    val usuario by authViewModel.usuarioActual.collectAsState()

    /**
     * Mensaje de error proporcionado por el ViewModel
     * cuando ocurre un fallo en la autenticación.
     */
    val error by authViewModel.mensajeError.collectAsState()

    /**
     * Launcher utilizado para iniciar el proceso
     * de autenticación con Google.
     */
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        authViewModel.loginConGoogle(result.data)
    }


    /**
     * Cuando el usuario se autentica correctamente,
     * se vuelve automáticamente a la pantalla anterior.
     */
    LaunchedEffect(usuario) {

        if (usuario != null) navController.popBackStack()
    }


    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("Iniciar sesión") },

                /**
                 * Botón para volver a la pantalla anterior.
                 */
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null
                        )
                    }
                }
            )
        }

    ) { padding ->

        /**
         * Contenedor principal del formulario de login.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),

            verticalArrangement = Arrangement.Center,

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /**
             * Campo de texto para introducir el email.
             */
            OutlinedTextField(

                value = email,

                onValueChange = { email = it },

                label = { Text("Email") },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))


            /**
             * Campo de texto para introducir la contraseña.
             * Incluye opción para mostrar u ocultar el contenido.
             */
            OutlinedTextField(

                value = password,

                onValueChange = { password = it },

                label = { Text("Contraseña") },

                visualTransformation =
                    if (passVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),

                trailingIcon = {

                    IconButton(
                        onClick = { passVisible = !passVisible }
                    ) {

                        Icon(
                            if (passVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,

                            null
                        )
                    }
                },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))


            /**
             * Si existe un error de autenticación se muestra en pantalla.
             */
            if (!error.isNullOrEmpty()) {

                Text(
                    error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }


            /**
             * Botón para iniciar sesión con email y contraseña.
             */
            Button(
                modifier = Modifier.fillMaxWidth(),

                onClick = {

                    authViewModel.login(
                        email.trim(),
                        password.trim()
                    )
                }
            ) {

                Text("Entrar")
            }


            /**
             * Botón para iniciar sesión mediante cuenta de Google.
             */
            OutlinedButton(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline
                ),

                onClick = {

                    googleLauncher.launch(
                        authViewModel.googleSignInIntent()
                    )
                }
            ) {

                /**
                 * Icono oficial de Google.
                 */
                Icon(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified
                )

                Spacer(Modifier.width(8.dp))

                Text("Entrar con Google")
            }

            Spacer(Modifier.height(8.dp))


            /**
             * Botón que permite navegar a la pantalla de registro.
             */
            TextButton(
                onClick = {
                    navController.navigate("registro")
                }
            ) {

                Text("Crear cuenta")
            }
        }
    }
}