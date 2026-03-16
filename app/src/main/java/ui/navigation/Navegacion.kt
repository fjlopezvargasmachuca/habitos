package com.example.habitos.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitos.ui.screens.*
import com.example.habitos.ui.viewmodel.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.habitos.data.database.HabitDatabase
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.example.habitos.data.repository.ForumRepository
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ui.screens.PantallaEditarPerfil

/**
 * Composable principal encargado de gestionar la navegación de la aplicación.
 *
 * Este componente define el sistema de rutas entre pantallas utilizando
 * la librería Navigation Compose de Jetpack.
 *
 * Dentro de esta función se configuran:
 *
 * - El controlador de navegación (`NavController`)
 * - La creación de los distintos `ViewModel`
 * - El `NavHost` que contiene todas las rutas de la aplicación
 *
 */
@Composable
fun Navegacion() {

    /**
     * Controlador principal de navegación de la aplicación.
     * Permite cambiar entre pantallas utilizando rutas.
     */
    val navController: NavHostController = rememberNavController()

    /**
     * Contexto actual de la aplicación, necesario para acceder
     * a recursos del sistema como la base de datos.
     */
    val context = LocalContext.current


    // ───────────────── VIEWMODELS ─────────────────

    /**
     * ViewModel encargado de la autenticación y gestión de usuarios.
     */
    val authViewModel: AuthViewModel = viewModel()

    /**
     * ViewModel encargado de gestionar los hábitos disponibles
     * dentro de la aplicación.
     */
    val habitViewModel: HabitViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                return HabitViewModel(
                    application = context.applicationContext as Application,
                    authViewModel = authViewModel
                ) as T
            }
        }
    )


    // ───────────────── BASE DE DATOS ─────────────────

    /**
     * Instancia de la base de datos Room utilizada por la aplicación.
     */
    val db = HabitDatabase.getDatabase(context)


    /**
     * ViewModel del foro.
     *
     * Se crea mediante un Factory para poder inyectar el repositorio
     * que utiliza el DAO del foro.
     */
    val forumViewModel: ForumViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val forumDao = db.forumDao()

                val repository = ForumRepository(forumDao)

                return ForumViewModel(repository) as T
            }
        }
    )


    // ───────────────── CALENDAR VIEWMODEL ─────────────────

    /**
     * ViewModel encargado de la lógica del calendario de hábitos.
     *
     * Se crea con un Factory porque necesita:
     * - acceso al DAO del calendario
     * - acceso al AuthViewModel para gestionar puntos de usuario
     */
    val calendarViewModel: CalendarViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {

            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                return CalendarViewModel(
                    calendarDao = db.calendarDao(),
                    authViewModel = authViewModel
                ) as T
            }
        }
    )


    /**
     * Estado actual del usuario autenticado.
     * Se utiliza para permitir o restringir el acceso a ciertas pantallas.
     */
    val usuarioActual = authViewModel.usuarioActual.collectAsState().value


    // ───────────────── NAV HOST ─────────────────

    /**
     * Contenedor principal de navegación.
     *
     * Aquí se definen todas las rutas disponibles en la aplicación.
     */
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {


        // ───────────────── PANTALLAS PRINCIPALES ─────────────────

        composable("inicio") {
            PantallaInicio(navController, authViewModel)
        }

        composable("login") {
            PantallaLogin(navController, authViewModel)
        }

        composable("registro") {
            PantallaRegistro(navController, authViewModel)
        }
        composable("editarPerfil") {
            PantallaEditarPerfil(navController, authViewModel)
        }
        composable("perfil") {
            PantallaPerfil(navController, authViewModel)
        }
        composable("contacto") {
            PantallaContacto(navController)
        }


        // ───────────────── GESTIÓN DE HÁBITOS ─────────────────

        composable("misHabitos") {
            PantallaMisHabitos(navController, habitViewModel)
        }

        composable("nuevo") {
            PantallaNuevoHabito(habitViewModel, navController)
        }

        composable("categorias") {
            PantallaCategorias(navController)
        }

        /**
         * Pantalla que muestra los hábitos pertenecientes
         * a una categoría concreta.
         */
        composable("categoria/{cat}") {

            val cat = it.arguments?.getString("cat") ?: ""

            PantallaListaHabitosCategoria(
                cat,
                habitViewModel,
                navController
            )
        }


        // ───────────────── CALENDARIO ─────────────────

        /**
         * Pantalla del calendario.
         * Solo accesible para usuarios autenticados.
         */
        composable("calendario") {

            if (usuarioActual == null) {

                navController.navigate("login")

            } else {

                PantallaCalendario(navController, calendarViewModel)
            }
        }

        /**
         * Pantalla que muestra los hábitos de un día concreto.
         */
        composable("calendario/dia/{fecha}") {

            val fecha = it.arguments?.getString("fecha") ?: ""

            PantallaDiaHabitos(
                navController = navController,
                calendarViewModel = calendarViewModel,
                habitViewModel = habitViewModel,
                authViewModel = authViewModel,
                fechaKey = fecha
            )
        }


        // ───────────────── FORO ─────────────────

        composable("foro") {
            PantallaTemasForo(navController, forumViewModel, authViewModel)
        }

        composable("foro/tema/{topicId}") {

            PantallaHilosTema(
                topicId = it.arguments!!.getString("topicId")!!,
                navController = navController,
                forumViewModel = forumViewModel,
                authViewModel = authViewModel
            )
        }

        composable("foro/tema/{topicId}/nuevo-hilo") {

            PantallaCrearHilo(
                navController = navController,
                forumViewModel = forumViewModel,
                authViewModel = authViewModel,
                topicId = it.arguments!!.getString("topicId")!!
            )
        }

        composable("foro/hilo/{threadId}") {

            PantallaForoDetalle(
                threadId = it.arguments!!.getString("threadId")!!,
                navController = navController,
                forumViewModel = forumViewModel,
                authViewModel = authViewModel
            )
        }

        composable("foro/nuevo-tema") {
            PantallaCrearTema(navController, forumViewModel, authViewModel)
        }


        // ───────────────── MODERACIÓN Y RANKING ─────────────────

        composable("moderacion") {
            PantallaModeracionUsuarios(navController, authViewModel)
        }

        composable("ranking") {
            PantallaRanking(navController, authViewModel)
        }


        // ───────────────── EDICIÓN DE HILOS ─────────────────

        composable("foro/editar-hilo/{id}") { backStack ->

            val hiloId = backStack.arguments!!.getString("id")!!.toInt()

            val hilos by forumViewModel.hilos.collectAsState()

            val hilo = hilos.firstOrNull { it.id == hiloId }

            if (hilo != null) {

                PantallaEditarHilo(
                    navController = navController,
                    forumViewModel = forumViewModel,
                    authViewModel = authViewModel,
                    hiloId = hilo.id,
                    tituloInicial = hilo.titulo
                )
            }
        }


        // ───────────────── EDICIÓN DE TEMAS ─────────────────

        composable(
            "foro/editar-tema/{id}/{nombre}/{descripcion}"
        ) { backStack ->

            val temaId = backStack.arguments!!.getString("id")!!.toInt()

            val nombre = backStack.arguments!!.getString("nombre")!!

            val descripcion = backStack.arguments!!.getString("descripcion")!!

            PantallaEditarTema(
                navController = navController,
                forumViewModel = forumViewModel,
                authViewModel = authViewModel,
                temaId = temaId,
                nombreInicial = nombre,
                descripcionInicial = descripcion
            )
        }

    }
}

