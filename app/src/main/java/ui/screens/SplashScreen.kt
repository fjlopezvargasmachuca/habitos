package com.example.habitos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.habitos.R
import kotlinx.coroutines.delay

/**
 * Pantalla de inicio (Splash Screen) de la aplicación.
 *
 * Esta pantalla se muestra brevemente cuando el usuario abre la aplicación.
 * Su objetivo es mostrar el logotipo y el nombre de la app mientras se realiza
 * una pequeña espera antes de navegar a la pantalla principal.
 *
 * Funcionalidades de esta pantalla:
 * - Mostrar el logo de la aplicación
 * - Mostrar el nombre de la app
 * - Esperar un breve periodo de tiempo
 * - Navegar automáticamente a la pantalla de inicio
 *
 * La navegación se realiza mediante NavController una vez transcurrido
 * el tiempo definido en el delay.
 */
@Composable
fun SplashScreen(navController: NavController) {

    /**
     * Efecto que se ejecuta una sola vez cuando la pantalla entra en composición.
     *
     * Se utiliza para crear una pequeña pausa que permite mostrar
     * el logo de la aplicación antes de navegar a la pantalla principal.
     */
    LaunchedEffect(true) {

        /**
         * Pequeña espera para mostrar el splash screen.
         * En este caso se utiliza un retraso de 1.5 segundos.
         */
        delay(1500)

        /**
         * Navegación hacia la pantalla de inicio de la aplicación.
         *
         * popUpTo elimina la pantalla splash del stack de navegación
         * para evitar que el usuario pueda volver a ella utilizando
         * el botón de retroceso.
         */
        navController.navigate("inicio") {
            popUpTo("splash") { inclusive = true }
        }
    }


    /**
     * Contenedor principal de la pantalla.
     *
     * Box permite centrar el contenido tanto vertical como
     * horizontalmente dentro de la pantalla.
     */
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        /**
         * Columna que organiza verticalmente el logo y el
         * nombre de la aplicación.
         */
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /**
             * Imagen que muestra el logotipo de la aplicación.
             */
            Image(
                painter = painterResource(R.drawable.splash_icon),
                contentDescription = "Splash Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            /**
             * Nombre de la aplicación mostrado debajo del logotipo.
             */
            Text(
                "Gestor de Micro-Hábitos",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}