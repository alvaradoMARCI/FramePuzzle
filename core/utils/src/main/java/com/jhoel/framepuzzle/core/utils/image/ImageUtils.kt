package com.jhoel.framepuzzle.core.utils.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.File
import java.io.FileOutputStream

/**
 * Utilidades de imagen (manejo de Bitmap, guardado a archivo, decodificación eficiente).
 * FramePuzzle evita OOM decodificando con inSampleSize cuando es necesario.
 */
object ImageUtils {

    /**
     * Decodifica un archivo a Bitmap limitando memoria.
     * @param reqWidth ancho deseado; 0 = original.
     * @param reqHeight alto deseado; 0 = original.
     */
    fun decodeSampled(path: String, reqWidth: Int = 0, reqHeight: Int = 0): Bitmap? {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, options)

        if (reqWidth > 0 && reqHeight > 0) {
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        }
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /** Rota un bitmap (útil al capturar con CameraX y EXIF). */
    fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        if (degrees == 0f) return bitmap
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * Guarda un Bitmap a archivo JPEG. Devuelve la ruta absoluta.
     * @param quality 0-100 (FramePuzzle usa 92 por defecto: alta calidad sin exceso).
     */
    fun saveJpeg(bitmap: Bitmap, target: File, quality: Int = 92): File {
        target.parentFile?.mkdirs()
        FileOutputStream(target).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        return target
    }

    /** Guarda PNG (con transparencia, ej: marcos decorativos). */
    fun savePng(bitmap: Bitmap, target: File): File {
        target.parentFile?.mkdirs()
        FileOutputStream(target).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return target
    }
}
