package com.example.habitos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel

/**
 * Pantalla de moderación de usuarios del sistema.
 *
 * Esta pantalla permite a los usuarios con privilegios de administración
 * gestionar el estado y rol de otros usuarios registrados en la aplicación.
 *
 * Funcionalidades disponibles:
 *
 * - Visualizar todos los usuarios registrados en el sistema.
 * - Banear o desbanear usuarios.
 * - Cambiar roles de usuario (USER ↔ ADMIN).
 *
 * Restricciones de seguridad implementadas:
 *
 * - El SUPERADMIN nunca aparece en la lista (no puede ser modificado).
 * - Un usuario no puede moderarse a sí mismo.
 * - Los ADMIN solo pueden banear usuarios con rol USER.
 * - Solo el SUPERADMIN puede cambiar roles.
 *
 * Los datos de usuarios se obtienen desde AuthViewModel mediante StateFlow,
 * permitiendo que la interfaz se actualice automáticamente cuando cambia
 * el estado de algún usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaModeracionUsuarios(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    /**
     * Lista completa de usuarios obtenida desde el ViewModel.
     */
    val usuarios by authViewModel.usuarios.collectAsState(initial = emptyList())

    /**
     * Usuario actualmente autenticado.
     */
    val usuarioActual by authViewModel.usuarioActual.collectAsState()

    /**
     * Al cargar la pantalla se comienza a escuchar
     * los cambios en la lista de usuarios.
     */
    LaunchedEffect(Unit) {

        authViewModel.escucharUsuarios()
    }


    /**
     * Estructura principal de la pantalla mediante Scaffold.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("🛡 Moderación de usuarios") },

                /**
                 * Botón de navegación para volver atrás.
                 */
                navigationIcon = {

                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver"
                        )
                    }
                }
            )
        }

    ) { padding ->


        /**
         * Lista de usuarios mostrados en formato de tarjetas.
         */
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            items(

                usuarios.filter { user ->

                    /**
                     * Filtrado de seguridad:
                     *
                     * - No mostrar al SUPERADMIN.
                     * - Evitar que el usuario actual pueda moderarse a sí mismo.
                     */
                    user.role != "SUPERADMIN" &&
                            user.id != usuarioActual?.id
                },

                key = { it.id }   // ← CORRECCIÓN: clave estable para evitar desapariciones en recomposición

            ) { user ->


                /**
                 * Tarjeta visual que representa a un usuario.
                 */
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {

                    Column(Modifier.padding(12.dp)) {

                        /**
                         * Nick del usuario.
                         */
                        Text(
                            text = user.nick,
                            style = MaterialTheme.typography.titleMedium
                        )

                        /**
                         * Información del rol y estado de baneo.
                         */
                        Text(
                            text = "Rol: ${user.role} · " +
                                    if (user.baneado)
                                        "🚫 Baneado"
                                    else
                                        "✅ Activo",

                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(Modifier.height(8.dp))


                        /**
                         * Zona de acciones administrativas.
                         */
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            /* ───────────── BAN / DESBAN ───────────── */

                            /**
                             * Determina si el usuario actual tiene permisos
                             * para banear a este usuario.
                             */
                            val puedeBanear = when (usuarioActual?.role) {

                                "SUPERADMIN" -> true

                                "ADMIN" -> user.role == "USER"

                                else -> false
                            }

                            /**
                             * Botón para banear o desbanear al usuario.
                             */
                            if (puedeBanear) {

                                if (user.baneado) {

                                    Button(
                                        onClick = {

                                            authViewModel.desbanearUsuario(user.id)
                                        }
                                    ) {

                                        Text("Desbanear")
                                    }

                                } else {

                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        ),

                                        onClick = {

                                            authViewModel.banearUsuario(user.id)
                                        }
                                    ) {

                                        Text("Banear")
                                    }
                                }
                            }


                            /* ───────────── CAMBIO DE ROL ───────────── */

                            /**
                             * Solo el SUPERADMIN puede modificar roles.
                             */
                            if (usuarioActual?.role == "SUPERADMIN") {

                                when (user.role) {

                                    /**
                                     * Promocionar usuario a ADMIN.
                                     */
                                    "USER" -> OutlinedButton(

                                        onClick = {

                                            authViewModel.cambiarRol(
                                                user.id,
                                                "ADMIN"
                                            )
                                        }
                                    ) {

                                        Text("Hacer ADMIN")
                                    }

                                    /**
                                     * Degradar ADMIN a USER.
                                     */
                                    "ADMIN" -> OutlinedButton(

                                        onClick = {

                                            authViewModel.cambiarRol(
                                                user.id,
                                                "USER"
                                            )
                                        }
                                    ) {

                                        Text("Quitar ADMIN")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}