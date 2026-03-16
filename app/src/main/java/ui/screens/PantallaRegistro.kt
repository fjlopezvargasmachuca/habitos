package com.example.habitos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import com.example.habitos.data.enums.Provincia
import android.util.Patterns
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Forum

/**
 * Pantalla de registro de usuario.
 *
 * Esta pantalla permite a un nuevo usuario crear una cuenta dentro
 * de la aplicación. El usuario debe introducir sus datos personales
 * y aceptar los términos y condiciones para completar el registro.
 *
 * Validaciones implementadas:
 * - El email debe coincidir con el email repetido
 * - La contraseña no puede estar vacía
 * - El usuario debe aceptar los términos y condiciones
 *
 * Cuando el registro se completa correctamente, el usuario se
 * autentica automáticamente y se vuelve a la pantalla anterior.
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val provincias = Provincia.values().toList()

    /**
     * Estados locales del formulario
     */
    var nombre by remember { mutableStateOf("") }
    var nick by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailRepetido by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }

    var provincia by remember { mutableStateOf("") }
    var provinciaExpanded by remember { mutableStateOf(false) }

    var aceptaTerminos by remember { mutableStateOf(false) }
    var mostrarTerminos by remember { mutableStateOf(false) }

    val mensajeError by authViewModel.mensajeError.collectAsState()
    val usuario by authViewModel.usuarioActual.collectAsState()

    /**
     * Lista de usuarios para comprobar nicks duplicados
     */
    val usuarios by authViewModel.usuarios.collectAsState(initial = emptyList())

    /**
     * Validación de email
     */
    val emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * Cuando el usuario se registra correctamente vuelve atrás
     */
    LaunchedEffect(usuario) {
        if (usuario != null) navController.popBackStack()
    }

    /**
     * DIÁLOGO TÉRMINOS
     */
    if (mostrarTerminos) {

        AlertDialog(
            onDismissRequest = { mostrarTerminos = false },

            confirmButton = {
                TextButton(onClick = { mostrarTerminos = false }) {
                    Text("Aceptar")
                }
            },

            title = { Text("Términos y Condiciones") },

            text = {
                Text(
                    """
                    1. Esta aplicación es un proyecto educativo.
                    2. Los datos se almacenan únicamente en el dispositivo.
                    3. El uso indebido del foro puede suponer el baneo del usuario.
                    4. El administrador se reserva el derecho de moderación.
                    """.trimIndent()
                )
            }
        )
    }

    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("Registro") },

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /**
             * NOMBRE
             */
            OutlinedTextField(
                nombre,
                { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            /**
             * NICK
             */
            OutlinedTextField(
                nick,
                { nick = it },
                label = { Text("Nick") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            /**
             * EMAIL
             */
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = email.isNotBlank() && !emailValido,
                modifier = Modifier.fillMaxWidth()
            )

            if (email.isNotBlank() && !emailValido) {
                Text(
                    "Formato de email inválido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(8.dp))

            /**
             * REPETIR EMAIL
             */
            OutlinedTextField(
                value = emailRepetido,
                onValueChange = { emailRepetido = it },
                label = { Text("Repite Email") },
                isError = email != emailRepetido,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            /**
             * CONTRASEÑA
             */
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },

                isError = pass.isEmpty() && pass.isNotEmpty(),

                visualTransformation =
                    if (passVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),

                trailingIcon = {

                    IconButton(onClick = { passVisible = !passVisible }) {

                        Icon(
                            if (passVisible) Icons.Filled.VisibilityOff
                            else Icons.Filled.Visibility,
                            null
                        )
                    }
                },

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            /**
             * PROVINCIA DESPLEGABLE
             */
            ExposedDropdownMenuBox(
                expanded = provinciaExpanded,
                onExpandedChange = { provinciaExpanded = !provinciaExpanded }
            ) {

                OutlinedTextField(
                    value = provincia,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Provincia") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(provinciaExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = provinciaExpanded,
                    onDismissRequest = { provinciaExpanded = false }
                ) {

                    provincias.forEach {

                        DropdownMenuItem(
                            text = { Text(it.nombre) },
                            onClick = {
                                provincia = it.nombre
                                provinciaExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            /**
             * TÉRMINOS
             */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Checkbox(
                    checked = aceptaTerminos,
                    onCheckedChange = { aceptaTerminos = it }
                )

                Text(
                    text = "Leer términos y condiciones",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { mostrarTerminos = true }
                )
            }

            Spacer(Modifier.height(12.dp))

            /**
             * MENSAJE ERROR
             */
            if (!mensajeError.isNullOrEmpty()) {
                Text(mensajeError!!, color = Color.Red)
            }

            /**
             * BOTÓN REGISTRO
             */
            Button(
                modifier = Modifier.fillMaxWidth(),

                enabled = pass.isNotBlank()
                        && email == emailRepetido
                        && aceptaTerminos
                        && provincia.isNotBlank()
                        && emailValido,

                onClick = {

                    val nickExiste = usuarios.any {
                        it.nick.equals(nick, ignoreCase = true)
                    }

                    if (nickExiste) {

                        Toast.makeText(
                            navController.context,
                            "Ese nick ya existe. Intenta con otro.",
                            Toast.LENGTH_LONG
                        ).show()

                        return@Button
                    }

                    authViewModel.registrar(
                        nombre = nombre,
                        nick = nick,
                        email = email,
                        password = pass,
                        provincia = provincia
                    )
                }
            ) {

                Text("Crear cuenta")
            }
        }
    }
}