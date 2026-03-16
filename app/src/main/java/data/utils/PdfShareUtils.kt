package com.example.habitos.data.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import com.example.habitos.data.entities.HealthyHabit

/**
 * Utilidad encargada de generar y compartir un documento PDF con los hábitos del usuario.
 *
 * Esta clase combina dos funcionalidades en un solo proceso:
 *
 * 1. Generación de un documento PDF con la lista de hábitos.
 * 2. Apertura automática del menú de compartir de Android para enviar el archivo
 *    mediante otras aplicaciones (correo, mensajería, almacenamiento en la nube, etc.).
 *
 * El PDF se genera utilizando la API nativa de Android `PdfDocument` y posteriormente
 * se comparte mediante un `Intent.ACTION_SEND` usando `FileProvider` para garantizar
 * un acceso seguro al archivo.
 */
object PdfShareUtils {

    /**
     * Generar un documento PDF con la lista de hábitos proporcionada
     *
     * @param context Contexto de la aplicación necesario para acceder al sistema de archivos
     *                y lanzar el menú de compartir.
     * @param habits Lista de hábitos que se incluirán en el documento PDF.
     */
    fun exportAndShareHabitsPdf(context: Context, habits: List<HealthyHabit>) {

        try {

            // ───── 1. CREACIÓN DEL DOCUMENTO PDF ─────

            val pdf = PdfDocument()

            // Tamaño aproximado de una página A4 en puntos
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()

            var page = pdf.startPage(pageInfo)

            val canvas = page.canvas

            // Configuración básica del texto
            val paint = Paint().apply { textSize = 14f }

            var y = 50f

            // Título del documento
            canvas.drawText("Listado de Hábitos Guardados", 40f, y, paint)
            y += 40f

            // Se recorren todos los hábitos para incluirlos en el PDF
            habits.forEach { h ->

                // Si se alcanza el final de la página se crea una nueva
                if (y > 780f) {

                    pdf.finishPage(page)

                    page = pdf.startPage(pageInfo)

                    y = 50f
                }

                // Información de cada hábito
                canvas.drawText("• ${h.titulo}", 40f, y, paint)
                y += 20

                canvas.drawText("  Categoría: ${h.categoria}", 40f, y, paint)
                y += 20

                canvas.drawText("  Duración: ${h.duracionMin} min", 40f, y, paint)
                y += 20

                canvas.drawText("  Dificultad: ${h.dificultad}", 40f, y, paint)
                y += 30
            }

            pdf.finishPage(page)


            // ───── 2. GUARDAR EL ARCHIVO PDF ─────

            // El archivo se almacena dentro del directorio interno de la app
            val pdfFile = File(context.filesDir, "habitos_compartidos.pdf")

            FileOutputStream(pdfFile).use { output ->
                pdf.writeTo(output)
            }

            pdf.close()


            // ───── 3. GENERAR URI SEGURA CON FILEPROVIDER ─────

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )


            // ───── 4. ABRIR MENÚ DE COMPARTIR ─────

            val intent = Intent(Intent.ACTION_SEND).apply {

                type = "application/pdf"

                putExtra(Intent.EXTRA_STREAM, uri)

                // Permite que otras apps puedan leer el archivo temporalmente
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(
                Intent.createChooser(intent, "Compartir PDF de Hábitos")
            )

        } catch (e: Exception) {

            // Registro del error en caso de fallo
            e.printStackTrace()
        }
    }
}