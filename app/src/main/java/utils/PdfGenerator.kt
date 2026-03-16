package com.example.habitos.util

import android.content.Context
import android.os.Environment
import com.example.habitos.data.entities.HealthyHabit
import java.io.File
import java.io.FileOutputStream
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.util.*

/**
 * Utilidad encargada de generar un documento PDF con la lista
 * de hábitos almacenados en la aplicación.
 *
 * Este objeto permite exportar los hábitos del usuario a un
 * archivo PDF que posteriormente puede ser compartido o guardado
 * en el dispositivo.
 *
 * La generación del documento se realiza utilizando la librería
 * iText para crear y escribir contenido dentro del archivo PDF.
 *
 * El archivo generado se guarda dentro del directorio de documentos
 * de la aplicación en el almacenamiento externo del dispositivo.
 */
object PdfGenerator {

    /**
     * Genera un archivo PDF que contiene la lista de hábitos
     * proporcionada como parámetro.
     *
     * Parámetros:
     * - context → contexto de la aplicación necesario para acceder
     *             al almacenamiento del dispositivo
     * - lista → lista de hábitos que se incluirán en el documento
     *
     * Retorna:
     * - Un objeto File que representa el archivo PDF generado.
     */
    fun generarPDF(context: Context, lista: List<HealthyHabit>): File {

        /**
         * Carpeta donde se almacenará el archivo PDF generado.
         * Se utiliza el directorio de documentos específico
         * de la aplicación.
         */
        val carpeta = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        /**
         * Creación del archivo PDF con un nombre único basado
         * en la marca de tiempo actual.
         */
        val archivo = File(
            carpeta,
            "mis_habitos_${System.currentTimeMillis()}.pdf"
        )


        /**
         * Creación del documento PDF.
         */
        val documento = Document()

        /**
         * Inicialización del escritor que se encargará de
         * guardar el contenido del documento en el archivo.
         */
        PdfWriter.getInstance(
            documento,
            FileOutputStream(archivo)
        )


        /**
         * Apertura del documento para comenzar a escribir contenido.
         */
        documento.open()


        /**
         * Título principal del documento.
         */
        documento.add(
            Paragraph("LISTA DE HÁBITOS\n\n")
        )


        /**
         * Se recorre la lista de hábitos y se añade cada uno
         * como una línea dentro del documento.
         */
        lista.forEach {

            documento.add(
                Paragraph("- ${it.titulo}: ${it.descripcion}")
            )
        }


        /**
         * Cierre del documento una vez finalizada la escritura.
         */
        documento.close()

        /**
         * Se devuelve el archivo generado para poder
         * compartirlo o utilizarlo posteriormente.
         */
        return archivo
    }
}