package com.example.habitos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.ui.viewmodel.AuthViewModel

/**
 * Pantalla de ranking de usuarios.
 *
 * Esta pantalla muestra la clasificación global de usuarios de la aplicación
 * en función de los puntos acumulados al completar hábitos saludables.
 *
 * Los usuarios se ordenan de mayor a menor puntuación, permitiendo visualizar
 * qué usuarios han obtenido más puntos dentro del sistema de gamificación.
 *
 * Información mostrada en el ranking:
 * - Nick del usuario
 * - Puntos acumulados
 *
 * La lista de usuarios se obtiene desde AuthViewModel utilizando StateFlow.
 * Esto permite que la interfaz se actualice automáticamente cuando cambian
 * los datos de los usuarios en la base de datos.
 *
 * Además, la pantalla incluye una barra superior con un botón de navegación
 * que permite volver a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRanking(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    /**
     * Lista de usuarios obtenida desde el ViewModel.
     *
     * collectAsState convierte el flujo de datos en un estado observable
     * para Jetpack Compose, permitiendo que la interfaz se recomponga
     * automáticamente cuando los datos cambian.
     */
    val usuarios by authViewModel.usuarios.collectAsState(initial = emptyList())


    /**
     * Se inicia la escucha de usuarios cuando la pantalla se muestra
     * por primera vez.
     *
     * Esta función normalmente conecta con la fuente de datos
     * (por ejemplo Firebase o base de datos local) para recibir
     * actualizaciones en tiempo real de los usuarios.
     */
    LaunchedEffect(Unit) {
        authViewModel.cargarRanking()
    }


    /**
     * Lista de usuarios ordenada por puntos de mayor a menor.
     *
     * Esto permite generar la clasificación del ranking
     * mostrando primero a los usuarios con mayor puntuación.
     */
    val ranking = usuarios.sortedByDescending { it.puntos }


    /**
     * Estructura principal de la pantalla utilizando Scaffold.
     *
     * Scaffold permite organizar la interfaz en diferentes áreas
     * como la barra superior y el contenido principal.
     */
    Scaffold(
        topBar = {

            TopAppBar(

                title = { Text("🏆 Ranking") },

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
         * Lista vertical de usuarios que forman parte del ranking.
         *
         * LazyColumn se utiliza para mostrar listas de forma eficiente,
         * renderizando únicamente los elementos visibles en pantalla.
         */
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            /**
             * Se recorre la lista de usuarios del ranking y se genera
             * una fila para cada uno de ellos.
             */
            itemsIndexed(ranking) { index, user ->


                /**
                 * Fila que representa un usuario dentro del ranking.
                 *
                 * Muestra el nick del usuario a la izquierda
                 * y sus puntos acumulados a la derecha.
                 */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),

                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Nick del usuario con posición en el ranking
                    Text("${index + 1}. ${user.nick}")

                    // Puntos acumulados por el usuario
                    Text("${user.puntos} pts")
                }
            }
        }
    }
}