package com.example.habitos.data.entities

/**
 * Enumeración que representa las diferentes categorías de hábitos
 * disponibles dentro de la aplicación.
 *
 * Cada valor del enum corresponde a un tipo de hábito saludable
 * que el usuario puede crear o seguir dentro del sistema.
 */
enum class Categoria(

    /**
     * Nombre utilizado para representar la categoría dentro de la base de datos.
     */
    val dbName: String
) {

    /**
     * Categoría relacionada con la actividad física y el cuidado del cuerpo.
     */
    SALUD_FISICA("salud_fisica"),

    /**
     * Categoría centrada en el bienestar psicológico y el equilibrio emocional.
     */
    BIENESTAR_MENTAL("bienestar_mental"),

    /**
     * Categoría relacionada con hábitos de alimentación y nutrición saludable.
     */
    NUTRICION("nutricion"),

    /**
     * Categoría enfocada en mejorar la eficiencia personal y la gestión del tiempo.
     */
    PRODUCTIVIDAD("productividad"),

    /**
     * Categoría relacionada con el descanso y la recuperación física y mental.
     */
    DESCANSO("descanso");
}