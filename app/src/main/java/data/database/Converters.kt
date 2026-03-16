package data.database

import androidx.room.TypeConverter

/**
 * Clase de conversores utilizada por Room para transformar tipos de datos
 * complejos que SQLite no puede almacenar directamente.
 *
 * SQLite solo admite tipos primitivos como:
 * - INTEGER
 * - TEXT
 * - REAL
 * - BLOB
 *
 * Sin embargo, en la aplicación pueden existir estructuras más complejas
 * como colecciones (por ejemplo, Set<Int>). Para poder almacenarlas en la
 * base de datos, Room permite definir conversores personalizados mediante
 * la anotación @TypeConverter.
 *
 * En este caso, se convierte un conjunto de enteros (Set<Int>) a una cadena
 * de texto separada por comas y viceversa.
 */
class Converters {

    /**
     * Convierte un conjunto de enteros (Set<Int>) en una cadena de texto.
     *
     * Este método se ejecuta automáticamente cuando Room necesita guardar
     * un Set<Int> en la base de datos.
     *
     * @param value Conjunto de enteros que se desea almacenar.
     * @return Cadena de texto con los valores separados por comas.
     */
    @TypeConverter
    fun fromSet(value: Set<Int>): String =
        value.joinToString(",")

    /**
     * Convierte una cadena de texto almacenada en la base de datos
     * en un conjunto de enteros (Set<Int>).
     *
     * Si la cadena está vacía o en blanco, se devuelve un conjunto vacío.
     *
     * @param value Cadena almacenada en la base de datos.
     * @return Conjunto de enteros reconstruido a partir del texto.
     */
    @TypeConverter
    fun toSet(value: String): Set<Int> =
        if (value.isBlank()) emptySet()
        else value.split(",").map { it.toInt() }.toSet()
}