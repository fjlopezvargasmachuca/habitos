package com.example.habitos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.R
import com.example.habitos.ui.viewmodel.AuthViewModel

/**
 * Pantalla principal de la aplicación.
 *
 * Esta pantalla actúa como punto de entrada a las funcionalidades
 * principales del sistema de hábitos saludables.
 *
 * Desde aquí el usuario puede:
 *
 * - Generar un hábito aleatorio.
 * - Explorar hábitos por categorías.
 * - Acceder al calendario de hábitos (si está autenticado).
 * - Consultar sus hábitos guardados (si está autenticado).
 * - Acceder a la pantalla de perfil o login.
 *
 * Además, cuando el usuario inicia sesión se muestra una animación
 * de bienvenida personalizada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    /**
     * Esquema de colores activo del tema de la aplicación.
     */
    val colorScheme = MaterialTheme.colorScheme

    /**
     * Usuario actualmente autenticado.
     * Se obtiene mediante StateFlow desde AuthViewModel.
     */
    val usuario by authViewModel.usuarioActual.collectAsState()

    /**
     * Estado interno que controla si se muestra
     * la animación de bienvenida al usuario.
     */
    var mostrarBienvenida by remember { mutableStateOf(false) }

    /**
     * Cuando cambia el usuario (login) se activa
     * la animación de bienvenida.
     */
    LaunchedEffect(usuario?.id) {
        if (usuario != null) {
            mostrarBienvenida = true
        }
    }

    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("Hábitos Saludables") },

                /**
                 * Botón de acceso al perfil o login.
                 *
                 * Se añade texto "Login:" para que sea más claro
                 * para el usuario que el icono permite iniciar sesión.
                 */
                actions = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = if (usuario == null) "Login:" else "Perfil:",
                            color = colorScheme.onPrimary
                        )

                        IconButton(
                            onClick = {
                                if (usuario == null) {
                                    navController.navigate("login")
                                } else {
                                    navController.navigate("perfil")
                                }
                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil / Login",
                                tint = colorScheme.onPrimary
                            )
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary,
                    titleContentColor = colorScheme.onPrimary
                )
            )
        }

    ) { paddingValues ->

        /**
         * Contenedor principal de la pantalla.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            /**
             * Bloque único de texto superior.
             * Garantiza que el espaciado sea idéntico
             * tanto si el usuario está autenticado
             * como si no lo está.
             */
            if (usuario != null) {

                AnimatedVisibility(
                    visible = mostrarBienvenida,
                    enter = fadeIn(tween(800)) +
                            slideInVertically(
                                tween(600),
                                initialOffsetY = { -40 }
                            ),
                    exit = fadeOut(tween(600)) +
                            slideOutVertically(
                                tween(600),
                                targetOffsetY = { -40 }
                            )
                ) {

                    Text(
                        text = "¡Bienvenido, ${usuario!!.displayName}!",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    )
                }

            } else {

                Text(
                    text = "Empieza a mejorar tus hábitos diarios",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colorScheme.primary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            /**
             * Logo de la aplicación.
             */
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier.size(170.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { navController.navigate("nuevo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generar hábito aleatorio")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("categorias") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(Icons.Default.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver categorías")
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (usuario != null) {

                Button(
                    onClick = { navController.navigate("calendario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Calendario de hábitos")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            /**
             * Este botón ahora está disponible
             * tanto para usuarios registrados
             * como para usuarios sin registrar.
             */
            Button(
                onClick = { navController.navigate("misHabitos") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(Icons.Default.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mis hábitos guardados")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("contacto") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(Icons.Default.Email, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contacto")
            }

            Text(
                text = "© 2026 - Proyecto DAM Francisco Javier López",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}