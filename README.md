# 📱 Hábitos Saludables – App Android (DAM)

Aplicación Android desarrollada como proyecto final del ciclo **Desarrollo de Aplicaciones Multiplataforma (DAM)**.  
Permite gestionar hábitos saludables, calendario, foro comunitario y exportación de datos en PDF.

---

## 🚀 Funcionalidades principales

### 👤 Autenticación
- Registro y login de usuarios
- Roles: USER, ADMIN, SUPERADMIN
- Gestión de perfil de usuario

### 🌱 Hábitos saludables
- Generación de hábitos aleatorios
- Guardado y listado de hábitos
- Clasificación por categorías
- Exportación de hábitos a PDF

### 📆 Calendario
- Visualización de hábitos por día
- Seguimiento del progreso diario
- Asociación de hábitos a fechas

### 💬 Foro comunitario
- Temas del foro (crear, editar, fijar, eliminar)
- Hilos dentro de cada tema
- Mensajes en cada hilo
- Moderación según rol de usuario

### 📄 Exportación PDF
- Generación de PDF con lista de hábitos
- Compartir PDF desde la aplicación

---

## 🧱 Arquitectura del proyecto

El proyecto sigue una arquitectura **MVVM + Repository**.

com.example.habitos
	 data
		dao # DAOs de Room
		entities # Entidades de base de datos
		repository # Repositorios
		database # HabitDatabase
		utils # Utilidades (PDF, converters, etc.)

	ui
		screens # Pantallas Compose
		viewmodel # ViewModels
		theme # Tema Material 3
		navigation # Navegación

	MainActivity.kt

---

## 🛠️ Tecnologías usadas

- **Kotlin**
- **Jetpack Compose**
- **Room Database**
- **StateFlow**
- **Material 3**
- **iText PDF**
- **MVVM**
- **Android SDK**

---

## 🔐 Roles y permisos

| Rol          | Permisos |
|--------------|----------|
| USER         | Crear hábitos, escribir mensajes |
| ADMIN        | Moderar foro |
| SUPERADMIN   | Gestión completa (temas, hilos, usuarios) |

---

## 📦 Instalación

1. Clonar el repositorio
2. Abrir con Android Studio
3. Ejecutar en emulador o dispositivo físico
4. Mínimo SDK: **24**

---

## 🎓 Proyecto DAM

Este proyecto demuestra:
- Uso de arquitectura limpia
- Persistencia de datos local
- Gestión de estados
- Navegación avanzada
- Buenas prácticas en Android moderno

---

## 👨‍💻 Autor

Proyecto desarrollado por **Francisco Javier López Vargas-Machuca**  
Ciclo: **Desarrollo de Aplicaciones Multiplataforma (DAM)**  
Año: **2026**