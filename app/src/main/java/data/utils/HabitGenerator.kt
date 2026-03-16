package com.example.habitos.data.utils

import com.example.habitos.data.entities.HealthyHabit

/**
 * Clase encargada de generar hábitos predefinidos dentro de la aplicación.
 *
 * La clase incluye:
 * - un conjunto de categorías disponibles
 * - métodos para generar hábitos según categoría
 * - generación de hábitos aleatorios
 *
 * Cada hábito generado incluye información básica como título,
 * descripción, categoría e imagen asociada.
 */
class HabitGenerator {

    /**
     * Lista de categorías disponibles para generar hábitos.
     *
     * Estas categorías se utilizan para organizar los hábitos
     * dentro de la aplicación y facilitar su búsqueda.
     */
    val CATEGORIAS = listOf(
        "Salud",
        "Ejercicio",
        "Productividad",
        "Estudio",
        "Bienestar",
        "Nutrición",
        "Sueño",
        "Organización"
    ).distinct()

    /**
     * Generar una lista de hábitos predefinidos para una categoría concreta.
     *
     * @param cat Categoría de hábitos solicitada.
     * @return Lista de hábitos asociados a la categoría.
     */
    fun generarHabitosPorCategoria(cat: String): List<HealthyHabit> {

        return when (cat) {

            "Salud" -> listOf(
                crear(
                    "Beber 2L de agua",
                    "Mantén una hidratación adecuada.",
                    cat,
                    "https://images.pexels.com/photos/416528/pexels-photo-416528.jpeg?w=800"
                ),
                crear(
                    "Tomar fruta fresca",
                    "Incluye una pieza de fruta a diario.",
                    cat,
                    "https://images.unsplash.com/photo-1610832958506-aa56368176cf?w=800"
                ),
                crear(
                    "Control del estrés",
                    "5 minutos de respiración profunda.",
                    cat,
                    "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800"
                )
            )

            "Ejercicio" -> listOf(
                crear(
                    "Caminar 20 minutos",
                    "Mantente activo de forma sencilla.",
                    cat,
                    "https://images.unsplash.com/photo-1476480862126-209bfaa8edc8?w=800"
                ),
                crear(
                    "Hacer estiramientos",
                    "Mejora la flexibilidad.",
                    cat,
                    "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=800"
                ),
                crear(
                    "Entrenamiento HIIT",
                    "Ejercicio intenso de corta duración.",
                    cat,
                    "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800"
                )
            )

            "Productividad" -> listOf(
                crear(
                    "Planificar el día",
                    "Escribe tus prioridades.",
                    cat,
                    "https://images.unsplash.com/photo-1506784242126-2a0b0b89c56a?w=800"
                ),
                crear(
                    "Ordenar escritorio",
                    "Un espacio limpio mejora tu enfoque.",
                    cat,
                    "https://images.unsplash.com/photo-1492724441997-5dc865305da7?w=800"
                ),
                crear(
                    "Revisar correos",
                    "Mantén tu bandeja al día.",
                    cat,
                    "https://images.unsplash.com/photo-1557200134-90327ee9fafa?w=800"
                )
            )

            "Estudio" -> listOf(
                crear(
                    "Estudiar 20 minutos",
                    "Pequeñas sesiones mejoran la retención.",
                    cat,
                    "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?w=800"
                ),
                crear(
                    "Repasar apuntes",
                    "Refuerza conocimientos adquiridos.",
                    cat,
                    "https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=800"
                ),
                crear(
                    "Leer 10 minutos",
                    "Lectura de comprensión diaria.",
                    cat,
                    "https://images.unsplash.com/photo-1519682337058-a94d519337bc?w=800"
                )
            )

            "Bienestar" -> listOf(
                crear(
                    "Meditar 10 min",
                    "Reduce estrés y mejora tu foco.",
                    cat,
                    "https://images.unsplash.com/photo-1528319725582-ddc096101511?w=800"
                ),
                crear(
                    "Salir a respirar aire fresco",
                    "Despeja la mente.",
                    cat,
                    "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=800"
                ),
                crear(
                    "Agradecimiento diario",
                    "Escribe 3 cosas buenas del día.",
                    cat,
                    "https://images.unsplash.com/photo-1499209974431-9dddcece7f88?w=800"
                )
            )

            "Nutrición" -> listOf(
                crear(
                    "Evitar azúcar añadida hoy",
                    "Reduce picos de insulina y mejora energía.",
                    cat,
                    "https://images.unsplash.com/photo-1490474418585-ba9bad8fd0ea?w=800"
                ),
                crear(
                    "Preparar comida casera",
                    "Una comida saludable hecha por ti.",
                    cat,
                    "https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?w=800"
                ),
                crear(
                    "Añadir verduras a una comida",
                    "Refuerza la fibra y vitaminas.",
                    cat,
                    "https://images.unsplash.com/photo-1543353071-087092ec393a?w=800"
                )
            )

            "Sueño" -> listOf(
                crear(
                    "Acostarse temprano",
                    "Intenta dormir antes de las 23:00.",
                    cat,
                    "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=800"
                ),
                crear(
                    "Evitar pantallas 30 min antes",
                    "Ayuda a mejorar tu descanso.",
                    cat,
                    "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800"
                ),
                crear(
                    "Rutina relajante",
                    "Estiramientos suaves antes de dormir.",
                    cat,
                    "https://images.unsplash.com/photo-1545205597-3d9d02c29597?w=800"
                )
            )

            "Organización" -> listOf(
                crear(
                    "Organizar armario",
                    "Reduce el desorden visual.",
                    cat,
                    "https://images.pexels.com/photos/3315286/pexels-photo-3315286.jpeg?w=800"
                ),
                crear(
                    "Ordenar documentos",
                    "Evita perder información importante.",
                    cat,
                    "https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=800"
                ),
                crear(
                    "Hacer lista de compras",
                    "Planifica lo necesario de tu hogar.",
                    cat,
                    "https://images.unsplash.com/photo-1542838132-92c53300491e?w=800"
                )
            )

            else -> emptyList()
        }
    }

    /**
     * Crear un objeto `HealthyHabit` a partir de los datos proporcionados.
     */
    private fun crear(
        titulo: String,
        descripcion: String,
        cat: String,
        imagen: String
    ): HealthyHabit {

        val h = HealthyHabit(
            userId = 0, // ← añadido
            titulo = titulo,
            descripcion = descripcion,
            categoria = cat,
            duracionMin = 10,
            dificultad = "Fácil"
        )

        // La imagen se asigna manualmente, ya que el campo está marcado con @Ignore
        h.imagenUrl = imagen

        return h
    }
    /**
     * Generar un nuevo hábito aleatorio.
     *
     * @param categoriaForzada Categoría opcional para limitar la generación.
     * @return Hábito generado aleatoriamente.
     */
    fun nuevoHabito(categoriaForzada: String? = null): HealthyHabit {

        val categoria = categoriaForzada ?: CATEGORIAS.random()

        val lista = generarHabitosPorCategoria(categoria)

        return if (lista.isNotEmpty()) lista.random() else crear(
            "Hábito aleatorio",
            "Descripción genérica",
            categoria,
            "https://images.unsplash.com/photo-1524995997946-a1c2e315a42f"
        )
    }
}