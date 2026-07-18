package com.jhoel.framepuzzle.feature.editor.domain

/**
 * Ajustes básicos del editor (sección 13, Herramientas básicas).
 *
 * Cada ajuste está normalizado en [-1, 1] o [0, 1] para que el render
 * sea independiente del motor.
 */
data class EditorAdjustments(
    val brightness: Float = 0f,   // [-1, 1]
    val contrast: Float = 0f,     // [-1, 1]
    val saturation: Float = 0f,   // [-1, 1]
    val temperature: Float = 0f,  // [-1, 1] (-frío, +cálido)
    val exposure: Float = 0f,     // [-1, 1]
    val rotationDegrees: Int = 0, // 0, 90, 180, 270
    val crop: CropRect? = null,
)

/**
 * Rectángulo de recorte (normalizado 0..1).
 */
data class CropRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
)

/**
 * Filtros propios de FramePuzzle (sección 13, Filtros propios).
 *
 * Cada filtro es una receta de ColorMatrix reproducible y offline.
 * No dependen de servicios externos.
 */
enum class FramePuzzleFilter(val display: String) {
    NONE("Original"),
    VINTAGE("Vintage"),
    NOSTALGIA("Nostalgia"),
    CINEMATIC("Cinemático"),
    BLACK_AND_WHITE("Blanco y negro"),
    OLD_MEMORY("Recuerdo antiguo"),
}

/**
 * Elementos visuales (sección 13, Elementos visuales):
 *  - Marcos, textos, decoraciones, firmas personales.
 */
sealed interface VisualElement {
    val id: String

    data class Frame(
        override val id: String,
        val frameType: FrameType,
    ) : VisualElement

    data class TextOverlay(
        override val id: String,
        val text: String,
        val x: Float,
        val y: Float,
        val fontSize: Float,
        val colorHex: String,
    ) : VisualElement

    data class Signature(
        override val id: String,
        val svgPath: String,
        val x: Float,
        val y: Float,
        val scale: Float,
    ) : VisualElement
}

enum class FrameType { CLASSIC, GOLD, POLAROID, MINIMAL }
