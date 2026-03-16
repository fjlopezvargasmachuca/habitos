📱 Hábitos Saludables – Android Habit Tracker

Aplicación Android desarrollada como proyecto final del ciclo Desarrollo de Aplicaciones Multiplataforma (DAM).
La aplicación permite gestionar hábitos saludables, realizar seguimiento diario mediante calendario, interactuar con otros usuarios en un foro comunitario y utilizar gamificación mediante sistema de puntos y ranking.

El objetivo del proyecto es demostrar el desarrollo de una aplicación Android moderna aplicando arquitectura MVVM, persistencia de datos local y diseño de interfaces con Jetpack Compose.

🚀 Funcionalidades principales
👤 Sistema de autenticación

La aplicación permite diferentes formas de acceso y control de usuarios.

Registro de usuarios mediante email y contraseña

Inicio de sesión mediante Google Sign-In

Gestión de perfil de usuario

Sistema de roles y permisos

Roles disponibles:

Usuario invitado

Usuario registrado

Administrador

Superadministrador

Cada rol dispone de distintos niveles de acceso dentro de la aplicación.

🌱 Gestión de hábitos saludables

La funcionalidad principal de la aplicación consiste en ayudar al usuario a crear y mantener hábitos saludables.

Características:

Generación automática de hábitos aleatorios

Creación y almacenamiento de hábitos personalizados

Organización de hábitos por categorías

Consulta de hábitos guardados

Exportación de hábitos a documentos PDF

Las categorías disponibles incluyen:

Salud física

Bienestar mental

Nutrición

Productividad

Descanso

📆 Calendario de hábitos

El calendario permite realizar un seguimiento diario del progreso del usuario.

Funciones disponibles:

Visualización de hábitos asociados a cada día

Registro del estado del hábito (pendiente, realizado o fallido)

Seguimiento del progreso personal

Control de hábitos realizados a lo largo del tiempo

Este sistema se integra con un sistema de gamificación, donde el usuario obtiene o pierde puntos según el cumplimiento de sus hábitos.

🏆 Sistema de gamificación

La aplicación incorpora un sistema de motivación basado en puntos.

Características:

Ganancia de puntos al completar hábitos

Pérdida de puntos si los hábitos no se cumplen

Ranking global de usuarios

Visualización de posición en la clasificación

Este sistema busca fomentar la constancia y el compromiso del usuario con sus objetivos.

💬 Foro comunitario

La aplicación incluye un foro donde los usuarios pueden compartir experiencias relacionadas con hábitos saludables.

Funcionalidades:

Creación de temas de discusión

Creación de hilos dentro de cada tema

Publicación de mensajes

Sistema de likes en mensajes

Moderación de contenidos según el rol del usuario

Funciones administrativas:

Creación, edición y eliminación de temas

Moderación de hilos

Baneo y desbaneo de usuarios

📄 Exportación de datos

Los usuarios pueden exportar su información de hábitos para utilizarla fuera de la aplicación.

Funciones disponibles:

Generación de PDF con hábitos seleccionados

Compartir el documento generado desde el dispositivo

🧱 Arquitectura del proyecto

La aplicación sigue el patrón MVVM (Model–View–ViewModel) junto con el patrón Repository, lo que permite separar correctamente la lógica de negocio, la interfaz y el acceso a datos.

Estructura principal del proyecto:

com.example.habitos

data
 ├── dao            # Acceso a base de datos Room
 ├── entities       # Entidades del modelo de datos
 ├── repository     # Capa de acceso a datos
 ├── database       # Configuración de Room
 └── utils          # Utilidades (PDF, generadores, helpers)

ui
 ├── screens        # Pantallas desarrolladas con Jetpack Compose
 ├── viewmodel      # ViewModels de la arquitectura MVVM
 ├── theme          # Tema y estilos Material 3
 └── navigation     # Sistema de navegación entre pantallas

MainActivity.kt

Esta estructura permite:

Separación de responsabilidades

Código más mantenible

Mayor facilidad para ampliar funcionalidades en el futuro.

🛠️ Tecnologías utilizadas

El proyecto ha sido desarrollado utilizando tecnologías modernas del ecosistema Android.

Kotlin

Jetpack Compose

Room Database

StateFlow

Material Design 3

MVVM Architecture

Google Sign-In

PDF Generation (iText)

Android Navigation

📦 Instalación

Para ejecutar el proyecto:

Clonar el repositorio

git clone https://github.com/fjlopezvargasmachuca/habitos.git

Abrir el proyecto en Android Studio

Sincronizar las dependencias de Gradle

Ejecutar la aplicación en:

Emulador Android

Dispositivo físico

Requisitos mínimos:

Android 7.0 (API 24) o superior

🎓 Proyecto DAM

Este proyecto ha sido desarrollado como Proyecto Final del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

El desarrollo del proyecto ha permitido aplicar conocimientos relacionados con:

Arquitectura de aplicaciones Android

Persistencia de datos

Diseño de interfaces modernas

Control de roles y permisos

Desarrollo de funcionalidades complejas

👨‍💻 Autor

Francisco Javier López Vargas-Machuca
Proyecto final – Desarrollo de Aplicaciones Multiplataforma (DAM)
Año: 2026
