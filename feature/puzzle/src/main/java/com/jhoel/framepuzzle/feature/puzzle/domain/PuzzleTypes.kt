package com.jhoel.framepuzzle.feature.puzzle.domain

import android.graphics.Bitmap

/**
 * Tipos de puzzle (sección 15).
 */
enum class PuzzleType { CLASSIC, SLIDING }

/**
 * Niveles de dificultad (sección 17).
 *
 *  - Fácil: menos piezas, mayor ayuda visual.
 *  - Normal: cantidad equilibrada.
 *  - Difícil: más piezas, menos ayudas.
 *  - Personalizado: usuario elige piezas, tiempo, ayudas.
 */
enum class PuzzleDifficulty(val pieces: Int, val showPreview: Boolean, val allowHints: Boolean) {
    EASY(pieces = 9, showPreview = true, allowHints = true),
    NORMAL(pieces = 16, showPreview = true, allowHints = true),
    HARD(pieces = 36, showPreview = false, allowHints = false),
    CUSTOM(pieces = 16, showPreview = true, allowHints = true),
}

/**
 * Configuración del puzzle. Define el tipo y la dificultad.
 */
data class PuzzleConfig(
    val type: PuzzleType,
    val difficulty: PuzzleDifficulty,
    val customPieces: Int? = null,
) {
    val totalPieces: Int
        get() = customPieces?.takeIf { it in 4..144 } ?: difficulty.pieces

    /** Columnas / filas del tablero cuadrado aproximado. */
    val gridSize: Int
        get() {
            val total = totalPieces
            val cols = kotlin.math.ceil(kotlin.math.sqrt(total.toFloat())).toInt()
            return cols.coerceAtLeast(2)
        }
}

/**
 * Estado del tablero durante el juego (sección 16).
 *
 * El tablero guarda:
 *  - pieces: lista de piezas con posición actual y objetivo.
 *  - gridSize: columnas / filas (tablero cuadrado).
 *  - sourceBitmap: atlas único del cual las piezas extraen su región.
 *    Evita guardar PNGs individuales (causaba ANR por I/O en Main Thread).
 *  - pieceWidth / pieceHeight: tamaño de cada pieza en el atlas.
 *  - emptySlotIndex: índice de la ranura vacía (solo SLIDING).
 */
data class PuzzleBoard(
    val pieces: List<PuzzlePiece>,
    val gridSize: Int,
    val sourceBitmap: Bitmap? = null,
    val pieceWidth: Int = 0,
    val pieceHeight: Int = 0,
    val emptySlotIndex: Int = -1,
) {
    val isSolved: Boolean
        get() = pieces.isNotEmpty() && pieces.all { it.isCorrect }

    /**
     * Mapa slot → pieza, precomputado para que el render sea O(1) por slot.
     * Evita firstOrNull { it.currentIndex == slot } que era O(n) por slot.
     */
    val pieceAtSlot: Map<Int, PuzzlePiece> by lazy {
        pieces.associateBy { it.currentIndex }
    }
}

/**
 * Pieza individual del puzzle (sección 16).
 */
data class PuzzlePiece(
    val id: Int,
    val currentIndex: Int,
    val targetIndex: Int,
    val bitmapPath: String? = null,
) {
    val isCorrect: Boolean get() = currentIndex == targetIndex
}

/**
 * Estadísticas de un puzzle resuelto (sección 18).
 */
data class PuzzleStats(
    val moves: Int,
    val timeMillis: Long,
    val perfect: Boolean,
)
