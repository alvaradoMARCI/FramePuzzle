package com.jhoel.framepuzzle.feature.editor.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import com.jhoel.framepuzzle.core.utils.image.ImageUtils
import com.jhoel.framepuzzle.feature.editor.domain.CropRect
import com.jhoel.framepuzzle.feature.editor.domain.EditorAdjustments
import com.jhoel.framepuzzle.feature.editor.domain.FramePuzzleFilter
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Procesador de imágenes del editor FramePuzzle (sección 13).
 *
 * Regla (sección 13 / 33 Editor Module):
 *  "Nunca modificar la imagen original."
 *
 * Este procesador siempre genera un nuevo archivo en edited/.
 * El original permanece intacto.
 */
@Singleton
class ImageProcessor @Inject constructor(
    private val storage: LocalStorageManager,
) {

    /**
     * Decodifica el bitmap original limitando memoria.
     * @param reqWidth 0 = original.
     */
    fun decodeSource(originalPath: String, reqWidth: Int = 0, reqHeight: Int = 0): Bitmap? =
        ImageUtils.decodeSampled(originalPath, reqWidth, reqHeight)

    /**
     * Aplica los ajustes y el filtro sobre la imagen ORIGINAL y devuelve
     * un bitmap NUEVO (sin guardarlo). Usado para preview en vivo.
     */
    fun applyToBitmap(
        source: Bitmap,
        adjustments: EditorAdjustments,
        filter: FramePuzzleFilter,
    ): Bitmap {
        var result = source

        // 1) Recorte (preview)
        adjustments.crop?.let { crop -> result = applyCrop(result, crop) }

        // 2) Rotación
        if (adjustments.rotationDegrees != 0) {
            result = ImageUtils.rotate(result, adjustments.rotationDegrees.toFloat())
        }

        // 3) Filtro (ColorMatrix)
        if (filter != FramePuzzleFilter.NONE) {
            result = applyFilter(result, filter)
        }

        // 4) Ajustes finos
        result = applyAdjustments(result, adjustments)
        return result
    }

    /**
     * Aplica los ajustes y el filtro sobre la imagen ORIGINAL y guarda
     * una nueva versión en edited/[memoryId]_edited.jpg.
     *
     * @return ruta absoluta del archivo editado.
     */
    fun applyAndSave(
        originalPath: String,
        memoryId: String,
        adjustments: EditorAdjustments,
        filter: FramePuzzleFilter,
    ): File {
        val source = ImageUtils.decodeSampled(originalPath, reqWidth = 2048, reqHeight = 2048)
            ?: error("No se pudo decodificar la imagen original")

        val result = applyToBitmap(source, adjustments, filter)

        val target = storage.newEditedFile(memoryId)
        return ImageUtils.saveJpeg(result, target, quality = 92)
    }

    private fun applyCrop(bitmap: Bitmap, crop: CropRect): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val left = (crop.left * w).toInt().coerceIn(0, w - 1)
        val top = (crop.top * h).toInt().coerceIn(0, h - 1)
        val right = (crop.right * w).toInt().coerceIn(left + 1, w)
        val bottom = (crop.bottom * h).toInt().coerceIn(top + 1, h)
        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    private fun applyFilter(bitmap: Bitmap, filter: FramePuzzleFilter): Bitmap {
        val matrix = when (filter) {
            FramePuzzleFilter.NONE -> ColorMatrix()
            FramePuzzleFilter.BLACK_AND_WHITE -> ColorMatrix().apply { setSaturation(0f) }
            FramePuzzleFilter.VINTAGE -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0.5f, 0.1f, 0f, 0f,
                    0.3f, 0.8f, 0.1f, 0f, 0f,
                    0.2f, 0.3f, 0.5f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f,
                ),
            )
            FramePuzzleFilter.NOSTALGIA -> ColorMatrix(
                floatArrayOf(
                    0.6f, 0.4f, 0.2f, 0f, 30f,
                    0.3f, 0.7f, 0.2f, 0f, 20f,
                    0.2f, 0.3f, 0.5f, 0f, 10f,
                    0f, 0f, 0f, 1f, 0f,
                ),
            )
            FramePuzzleFilter.CINEMATIC -> ColorMatrix(
                floatArrayOf(
                    0.9f, 0.1f, 0.1f, 0f, 0f,
                    0.1f, 0.9f, 0.1f, 0f, 0f,
                    0.1f, 0.1f, 0.9f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f,
                ),
            )
            FramePuzzleFilter.OLD_MEMORY -> ColorMatrix(
                floatArrayOf(
                    0.7f, 0.4f, 0.2f, 0f, 15f,
                    0.3f, 0.6f, 0.2f, 0f, 10f,
                    0.2f, 0.3f, 0.4f, 0f, 5f,
                    0f, 0f, 0f, 1f, 0f,
                ),
            )
        }
        return applyColorMatrix(bitmap, matrix)
    }

    private fun applyAdjustments(bitmap: Bitmap, a: EditorAdjustments): Bitmap {
        if (a.brightness == 0f && a.contrast == 0f && a.saturation == 0f &&
            a.temperature == 0f && a.exposure == 0f
        ) {
            return bitmap
        }

        val brightness = (a.brightness + a.exposure) * 80f
        val contrast = 1f + a.contrast
        val saturation = 1f + a.saturation
        val tempShift = a.temperature * 20f

        val cm = ColorMatrix()
        cm.setSaturation(saturation.coerceAtLeast(0f))

        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                contrast, 0f, 0f, 0f, brightness,
                0f, contrast, 0f, 0f, brightness,
                0f, 0f, contrast, 0f, brightness,
                0f, 0f, 0f, 1f, 0f,
            ),
        )
        val tempMatrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, tempShift,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, -tempShift,
                0f, 0f, 0f, 1f, 0f,
            ),
        )
        cm.postConcat(contrastMatrix)
        cm.postConcat(tempMatrix)
        return applyColorMatrix(bitmap, cm)
    }

    private fun applyColorMatrix(bitmap: Bitmap, matrix: ColorMatrix): Bitmap {
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(matrix) }
        val out = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        Canvas(out).drawBitmap(bitmap, 0f, 0f, paint)
        return out
    }
}
