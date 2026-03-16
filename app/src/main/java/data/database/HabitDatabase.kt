package com.example.habitos.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habitos.data.dao.*
import com.example.habitos.data.entities.*
import data.database.Converters

/**
 * Clase principal de la base de datos de la aplicación.
 *
 * HabitDatabase define la base de datos SQLite utilizada por la aplicación
 * mediante la librería Room. En esta clase se especifican todas las entidades
 * que forman parte del esquema de la base de datos, así como los DAO
 * (Data Access Object) que permiten interactuar con dichas tablas.
 *
 * Room genera automáticamente la implementación de esta base de datos
 * en tiempo de compilación.
 *
 * La base de datos utiliza también conversores personalizados definidos
 * en la clase Converters para poder almacenar tipos de datos complejos.
 */
@Database(
    entities = [
        User::class,
        HealthyHabit::class,
        CalendarHabit::class,
        ForumTopic::class,
        ForumThread::class,
        ForumMessage::class,
        ForumLike::class,
        ForumNotification::class,
        ScheduledHabit::class
    ],
    version = 29,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {

    /**
     * Devuelve el DAO encargado de gestionar las operaciones
     * relacionadas con los usuarios de la aplicación.
     */
    abstract fun userDao(): UserDao

    /**
     * Devuelve el DAO encargado de gestionar el catálogo
     * de hábitos saludables disponibles en la aplicación.
     */
    abstract fun habitDao(): HabitDao

    /**
     * Devuelve el DAO encargado de gestionar los hábitos
     * programados en el calendario del usuario.
     */
    abstract fun calendarDao(): CalendarDao

    /**
     * Devuelve el DAO encargado de gestionar las operaciones
     * relacionadas con el sistema de foro de la aplicación.
     */
    abstract fun forumDao(): ForumDao

    /**
     * Devuelve el DAO encargado de gestionar los hábitos
     * programados por los usuarios en fechas concretas.
     */
    abstract fun scheduledHabitDao(): ScheduledHabitDao

    /**
     * Devuelve el DAO encargado de gestionar las notificaciones
     * generadas dentro del foro.
     */
    abstract fun notificationDao(): NotificationDao

    companion object {

        /**
         * Instancia única (Singleton) de la base de datos.
         *
         * La anotación @Volatile garantiza que los cambios en la
         * referencia de INSTANCE sean visibles inmediatamente para todos los hilos.
         */
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        /**
         * Obtener la instancia única de la base de datos.
         *
         * @param context Contexto de la aplicación necesario
         * para crear la base de datos.
         *
         * @return Instancia única de HabitDatabase.
         */
        fun getDatabase(context: Context): HabitDatabase =
            INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit_database"
                )
                    /**
                     * Si la versión de la base de datos cambia y no existe
                     * una migración definida, se eliminarán las tablas
                     * existentes y se recrearán automáticamente.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}