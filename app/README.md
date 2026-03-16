# 📱 Hábitos Saludables – Proyecto DAM

Aplicación Android desarrollada como **Proyecto Final del Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma (DAM)**.

La aplicación permite gestionar **hábitos saludables**, organizar rutinas, consultar un **calendario**, y participar en un **foro interno** con hilos y mensajes, todo con persistencia local mediante base de datos.

---

## 🎯 Objetivo del proyecto

El objetivo principal de este proyecto es demostrar la adquisición de competencias en:

- Desarrollo de aplicaciones Android modernas
- Arquitectura limpia y separación de responsabilidades
- Uso de bases de datos locales
- Programación asíncrona
- Diseño de interfaces con Jetpack Compose

Además, el proyecto sirve como **portfolio personal** para demostrar habilidades técnicas ante profesores o empresas.

---

## 🧩 Funcionalidades principales

- 🔐 **Autenticación de usuarios**
- 🧠 **Gestión de hábitos saludables**
- 📅 **Calendario de hábitos**
- 🧵 **Foro interno** con:
    - Temas
    - Hilos
    - Mensajes
- 👤 Roles de usuario (USER / SUPERADMIN)
- 🗑️ Edición y eliminación de temas e hilos según permisos
- 💾 Persistencia de datos local

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Arquitectura:** MVVM
- **Base de datos:** Room
- **Asincronía:** Coroutines + StateFlow
- **Navegación:** Navigation Compose
- **Gestión de dependencias:** Gradle (Version Catalog)
- **IDE:** Android Studio

---

## 🧱 Arquitectura del proyecto

El proyecto sigue el patrón **MVVM**:

- **UI (screens):** Pantallas Compose
- **ViewModel:** Lógica de presentación y estados
- **Repository:** Abstracción del acceso a datos
- **DAO / Room:** Persistencia local

Esto permite:
- Código más mantenible
- Separación clara de responsabilidades
- Mejor escalabilidad

---

## 📂 Estructura del proyecto

com.example.habitos
├── data
│ ├── dao
│ ├── entities
│ ├── repository
│ └── HabitDatabase.kt
├── ui
│ ├── screens
│ ├── viewmodel
│ └── navigation
├── theme
└── MainActivity.kt

---

## ▶️ Cómo ejecutar el proyecto

1. Clonar o descargar el repositorio
2. Abrir el proyecto con **Android Studio**
3. Esperar a que Gradle sincronice
4. Ejecutar en:
    - Emulador Android
    - Dispositivo físico (USB)

**Requisitos mínimos:**
- Android Studio actualizado
- SDK mínimo: API 24

---

## 📚 Aprendizajes adquiridos

- Uso real de Room con relaciones entre tablas
- Gestión de estado con StateFlow en Compose
- Navegación entre pantallas
- Control de permisos y roles de usuario
- Manejo de errores y depuración
- Organización de un proyecto Android profesional

---

## 🚀 Estado del proyecto

✅ Funcional  
🔧 En mejora continua  
📦 Pensado como proyecto académico y portfolio

---

## 👨‍💻 Autor

**Francisco Javier López Vargas-Machuca**  
Estudiante de Desarrollo de Aplicaciones Multiplataforma (DAM)

---

## 📜 Licencia

Proyecto desarrollado con fines **educativos y demostrativos**.