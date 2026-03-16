package com.example.habitos.data.utils

import android.content.Context
import android.os.Environment
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utilidad encargada de generar un informe PDF con el progreso del usuario.
 *
 * Este documento permite exportar un resumen del perfil del usuario
 * incluyendo información como:
 *
 * - nombre y email
 * - puntos acumulados
 * - nivel alcanzado
 * - hábitos completados
 * - porcentaje de efectividad
 *
 */
object PdfGeneratorPerfil {

    /**
     * Generar un archivo PDF con el progreso del usuario.
     *
     * @param context Contexto de la aplicación necesario para acceder al almacenamiento.
     * @param nombre Nombre del usuario.
     * @param email Email del usuario.
     * @param puntos Puntos acumulados por el usuario.
     * @param nivel Nivel o insignia alcanzada.
     * @param completados Número de hábitos completados.
     * @param totales Número total de hábitos registrados.
     * @param porcentaje Porcentaje de efectividad calculado.
     *
     * @return Archivo PDF generado o `null` si ocurre algún error.
     */
    fun exportarProgreso(
        context: Context,
        nombre: String,
        email: String,
        puntos: Int,
        nivel: String,
        completados: Int,
        totales: Int,
        porcentaje: Int
    ): File? {

        return try {

            // Carpeta donde se almacenarán los reportes generados
            val carpeta =
                File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "reportes")

            // Si la carpeta no existe se crea automáticamente
            if (!carpeta.exists()) carpeta.mkdirs()

            // Fecha actual para identificar el archivo
            val fecha = SimpleDateFormat(
                "dd-MM-yyyy HH:mm",
                Locale.getDefault()
            ).format(Date())

            val archivo = File(carpeta, "progreso_$fecha.pdf")

            // Creación del documento PDF
            val documento = Document()
            PdfWriter.getInstance(documento, FileOutputStream(archivo))

            documento.open()

            // ───── CONTENIDO DEL DOCUMENTO ─────

            documento.add(Paragraph("REPORTE DE PROGRESO"))
            documento.add(Paragraph(" "))

            documento.add(Paragraph("Usuario: $nombre"))
            documento.add(Paragraph("Email: $email"))
            documento.add(Paragraph("Fecha de exportación: $fecha"))
            documento.add(Paragraph(" "))

            documento.add(Paragraph("Puntos acumulados: $puntos"))
            documento.add(Paragraph("Nivel: $nivel"))
            documento.add(Paragraph(" "))

            documento.add(Paragraph("Hábitos completados: $completados"))
            documento.add(Paragraph("Hábitos totales: $totales"))
            documento.add(Paragraph("Efectividad: $porcentaje%"))
            documento.add(Paragraph(" "))

            documento.add(Paragraph("Gracias por usar la aplicación."))

            documento.close()

            // Devuelve el archivo generado
            archivo

        } catch (e: Exception) {

            // En caso de error se registra la excepción
            e.printStackTrace()

            null
        }
    }
}