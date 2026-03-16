package com.example.habitos.data.utils

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import com.example.habitos.data.entities.HealthyHabit
import java.io.File
import java.io.FileOutputStream

/**
 * Exporta un PDF con TODOS los hábitos guardados.
 */
fun exportarYCompartirPDF_lista(
    context: Context,
    filesDir: File,
    lista: List<HealthyHabit>,
    packageName: String
) {
    try {
        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = pdf.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint().apply { textSize = 12f }

        var y = 50f

        canvas.drawText("Listado de Hábitos Guardados", 40f, y, paint)
        y += 30f

        lista.forEach { habito ->

            // Si no cabe en la página → nueva página
            if (y > 780) {
                pdf.finishPage(page)
                page = pdf.startPage(pageInfo)
                y = 50f
            }

            canvas.drawText("• ${habito.titulo}", 40f, y, paint); y += 20f
            canvas.drawText("  Descripción: ${habito.descripcion}", 40f, y, paint); y += 20f
            canvas.drawText("  Categoría: ${habito.categoria}", 40f, y, paint); y += 20f
            canvas.drawText("  Duración: ${habito.duracionMin} min", 40f, y, paint); y += 20f
            canvas.drawText("  Dificultad: ${habito.dificultad}", 40f, y, paint); y += 30f
        }

        pdf.finishPage(page)

        val file = File(filesDir, "todos_los_habitos.pdf")
        FileOutputStream(file).use { pdf.writeTo(it) }
        pdf.close()

        val uri = FileProvider.getUriForFile(context, "$packageName.fileprovider", file)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Compartir listado de hábitos"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}