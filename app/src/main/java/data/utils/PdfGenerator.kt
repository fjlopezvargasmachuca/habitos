package com.example.habitos.data.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.habitos.data.entities.HealthyHabit
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Utilidad encargada de generar documentos PDF con la lista de hábitos
 * almacenados en la aplicación.
 *
 * Esta clase implementa dos métodos diferentes de generación de PDF:
 *
 * 1. **PDF simple (iText)** → generación rápida de documentos con texto.
 * 2. **PDF avanzado (PdfDocument)** → generación manual con control total
 *    sobre el diseño, paginación y formato del contenido.
 *
 * Esta funcionalidad permite que el usuario pueda exportar y compartir
 * sus hábitos saludables fuera de la aplicación, por ejemplo:
 *
 * - guardar un registro personal
 * - enviarlo por correo o mensajería
 * - imprimirlo
 */
object PdfGenerator {

    // ───────────────── PDF SIMPLE (iText) ─────────────────

    /**
     * Genera un PDF simple utilizando la librería **iText**.
     *
     * Este método crea un documento con una lista básica de hábitos
     * mostrando su título y descripción.
     *
     * El archivo generado se guarda en el directorio de documentos
     * de la aplicación.
     *
     * @param context Contexto de la aplicación.
     * @param lista Lista de hábitos que se incluirán en el PDF.
     * @return Archivo PDF generado.
     */
    fun generarPDF(
        context: Context,
        lista: List<HealthyHabit>
    ): File {

        val carpeta = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val archivo = File(carpeta, "mis_habitos_${System.currentTimeMillis()}.pdf")

        val documento = Document()
        PdfWriter.getInstance(documento, FileOutputStream(archivo))

        documento.open()

        // Título del documento
        documento.add(Paragraph("LISTA DE HÁBITOS\n\n"))

        // Se añaden todos los hábitos
        lista.forEach {
            documento.add(
                Paragraph("- ${it.titulo}: ${it.descripcion}")
            )
        }

        documento.close()

        return archivo
    }


    // ───────────────── PDF AVANZADO (PdfDocument) ─────────────────

    /**
     * Genera un PDF más avanzado utilizando la clase nativa `PdfDocument`.
     *
     * Este método permite un mayor control sobre el diseño del documento:
     * - tamaño de página
     * - estilos de texto
     * - paginación automática
     *
     * Una vez generado el documento, se abre un menú para compartirlo
     * mediante otras aplicaciones (correo, WhatsApp, almacenamiento, etc.).
     *
     * @param context Contexto de la aplicación.
     * @param habits Lista de hábitos a exportar.
     */
    fun exportHabitsAsPdf(
        context: Context,
        habits: List<HealthyHabit>
    ) {

        // Se verifica que existan hábitos para exportar
        if (habits.isEmpty()) {
            Toast.makeText(
                context,
                "No hay hábitos para exportar",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val pdf = PdfDocument()

        // Tamaño estándar A4 en puntos
        val pageWidth = 595
        val pageHeight = 842

        // Estilo para el título
        val titlePaint = Paint().apply {
            textSize = 16f
            isFakeBoldText = true
        }

        // Estilo para el texto normal
        val textPaint = Paint().apply {
            textSize = 12f
        }

        var pageNumber = 1
        var pageInfo =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()

        var page = pdf.startPage(pageInfo)
        var canvas = page.canvas

        // Posición vertical del contenido
        var y = 40f

        /**
         * Función auxiliar para crear una nueva página
         * cuando el contenido supera el tamaño de la actual.
         */
        fun nuevaPagina() {

            pdf.finishPage(page)

            pageNumber++

            pageInfo =
                PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()

            page = pdf.startPage(pageInfo)
            canvas = page.canvas

            y = 40f
        }

        // Título del documento
        canvas.drawText(
            "Listado de hábitos guardados",
            40f,
            y,
            titlePaint
        )

        y += 30f

        // Se recorren todos los hábitos
        for (habit in habits) {

            // Si se alcanza el final de la página se crea una nueva
            if (y > pageHeight - 80) {
                nuevaPagina()
            }

            canvas.drawText("• ${habit.titulo}", 40f, y, textPaint)
            y += 18f

            canvas.drawText("   ${habit.descripcion}", 40f, y, textPaint)
            y += 16f

            canvas.drawText("   Categoría: ${habit.categoria}", 40f, y, textPaint)
            y += 16f

            canvas.drawText("   Duración: ${habit.duracionMin} min", 40f, y, textPaint)
            y += 16f

            canvas.drawText("   Dificultad: ${habit.dificultad}", 40f, y, textPaint)
            y += 22f
        }

        pdf.finishPage(page)

        try {

            // Archivo temporal en caché
            val file = File(
                context.cacheDir,
                "habitos_${System.currentTimeMillis()}.pdf"
            )

            pdf.writeTo(file.outputStream())
            pdf.close()

            /**
             * Se obtiene un URI seguro mediante FileProvider
             * para compartir el archivo con otras aplicaciones.
             */
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            // Intent para compartir el PDF
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(
                Intent.createChooser(
                    sendIntent,
                    "Compartir hábitos (PDF)"
                )
            )

        } catch (e: IOException) {

            pdf.close()

            Toast.makeText(
                context,
                "Error al generar el PDF",
                Toast.LENGTH_SHORT
            ).show()

            e.printStackTrace()
        }
    }
}