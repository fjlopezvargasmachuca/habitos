package com.example.habitos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel
import android.widget.Toast
import androidx.compose.material.icons.filled.Edit

/**
 * Pantalla de perfil del usuario.
 *
 * Esta pantalla muestra la información del usuario autenticado
 * y permite acceder a distintas funcionalidades relacionadas
 * con su cuenta dentro de la aplicación.
 *
 * Funcionalidades disponibles:
 * - Acceso al foro
 * - Acceso al ranking de usuarios
 * - Moderación de usuarios (solo para moderadores o administradores)
 * - Cerrar sesión
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    /**
     * Usuario actualmente autenticado.
     * Se obtiene mediante StateFlow desde AuthViewModel.
     */
    val usuario by authViewModel.usuarioActual.collectAsState()

    /**
     * Si no hay usuario autenticado se redirige automáticamente
     * a la pantalla de login.
     */
    if (usuario == null) {
        LaunchedEffect(Unit) {
            navController.navigate("login")
        }
        return
    }

    /**
     * Se obtiene el usuario actual.
     */
    val user = usuario!!

    /**
     * Se cargan los usuarios para poder calcular
     * la posición dentro del ranking.
     */
    LaunchedEffect(Unit) {
        authViewModel.cargarRanking()
    }

    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     * Incluye la barra superior y el contenido del perfil.
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
                title = { Text("Mi perfil") },

                /**
                 * Botón para volver a la pantalla anterior.
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
         * Contenedor principal del contenido del perfil.
         * Se utiliza scroll vertical para asegurar que todo
         * el contenido sea accesible en pantallas pequeñas.
         */
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ───── CABECERA DEL PERFIL ─────

            /**
             * Mostrar el icono de nivel junto al nick del usuario.
             */
            Text(
                text = "${iconoNivel(user.puntos)} ${user.nick}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(8.dp))

            /**
             * Información básica del usuario.
             */
            Text(user.email)
            Text("📍 Provincia: ${user.provincia}")
            Text("👤 Rol: ${user.role}")
            Text("⭐ Puntos: ${user.puntos}")
            val usuarios by authViewModel.usuarios.collectAsState(initial = emptyList())

            val ranking = usuarios.sortedByDescending { it.puntos }

            val posicion = (ranking.indexOfFirst { it.id == user.id } + 1).coerceAtLeast(1)
            Text("🏆 Posición en ranking: #$posicion de ${ranking.size}")
            Spacer(Modifier.height(24.dp))

            // ───── BOTONES DE FUNCIONALIDAD ─────

            /**
             * Botón que permite acceder al foro de la aplicación.
             */
            Button(
                onClick = {

                    if (user.provincia.isBlank()) {

                        Toast.makeText(
                            navController.context,
                            "Completa tu provincia en el perfil antes de usar el foro",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        navController.navigate("foro")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Forum, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Acceder al foro")
            }

            Spacer(Modifier.height(12.dp))

            /**
             * Botón que abre el ranking de usuarios.
             * Permite ver la clasificación basada en puntos.
             */
            Button(
                onClick = { navController.navigate("ranking") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Ranking")
            }

            Spacer(Modifier.height(12.dp))
            /**
             * Botón que edita el perfil de usuarios.
             * Permite modificar los datos del usuario ya registrado.
             */
            Button(
                onClick = { navController.navigate("editarPerfil") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Editar perfil")
            }
            /**
             * Botón de moderación visible solo para usuarios
             * con rol distinto a "USER" (moderadores o administradores).
             */
            if (user.role != "USER") {
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { navController.navigate("moderacion") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Security, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Moderar usuarios")
                }
            }

            Spacer(Modifier.height(12.dp))

            /**
             * Botón para cerrar sesión.
             *
             * - Se llama al método logout del AuthViewModel.
             * - Se redirige al usuario a la pantalla de inicio.
             * - Se limpia la pila de navegación para evitar volver atrás.
             */
            Button(
                onClick = {

                    authViewModel.logout()

                    navController.navigate("inicio") {
                        popUpTo("inicio") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),

                /**
                 * Se utiliza el color de error del tema
                 * para destacar visualmente la acción de cerrar sesión.
                 */
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar sesión")
            }
        }
    }
}

/* ───── ICONO DE NIVEL ───── */

/**
 * Función que devuelve un icono representando el nivel del usuario
 * en función de los puntos acumulados.
 */
fun iconoNivel(puntos: Int): String =
    when {
        puntos >= 500 -> "👑"
        puntos >= 250 -> "🏆"
        puntos >= 150 -> "🥇"
        puntos >= 75 -> "🥈"
        puntos >= 25 -> "🥉"
        else -> "🌱"
    }