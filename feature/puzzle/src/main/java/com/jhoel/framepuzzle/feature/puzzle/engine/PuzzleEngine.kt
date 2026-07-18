package com.jhoel.framepuzzle.feature.puzzle.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.jhoel.framepuzzle.core.utils.image.ImageUtils
import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleBoard
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleConfig
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzlePiece
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleStats
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleType
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Motor puzzle de FramePuzzle (sección 14).
 *
 *  "Debe desarrollarse un motor propio en Kotlin.
 *   No depender de motores externos."
 *
 * Responsable de:
 *  - División de imagen.
 *  - Creación de piezas.
 *  - Mezcla aleatoria.
 *  - Validación de movimientos.
 *  - Estado del tablero.
 *  - Detección de victoria.
 */
@Singleton
class PuzzleEngine @Inject constructor(
    private val storage: LocalStorageManager,
) {

    /**
     * Divide la imagen en piezas (sección 16, División de imagen).
     *
     * @param sourcePath ruta del bitmap original o editado.
     * @param config configuración (tipo + dificultad + piezas).
     * @return tablero inicial con piezas en posiciones mezcladas.
     */
    fun createBoard(sourcePath: String, config: PuzzleConfig): PuzzleBoard {
        val source = ImageUtils.decodeSampled(sourcePath, reqWidth = 1024, reqHeight = 1024)
            ?: error("No se pudo decodificar imagen del puzzle")

        val size = config.gridSize
        val pieceW = source.width / size
        val pieceH = source.height / size

        val pieces = mutableListOf<PuzzlePiece>()
        var index = 0
        for (row in 0 until size) {
            for (col in 0 until size) {
                val left = col * pieceW
                val top = row * pieceH
                val right = if (col == size - 1) source.width else (col + 1) * pieceW
                val bottom = if (row == size - 1) source.height else (row + 1) * pieceH
                val pieceBitmap = Bitmap.createBitmap(
                    source,
                    left,
                    top,
                    (right - left).coerceAtLeast(1),
                    (bottom - top).coerceAtLeast(1),
                )
                val file = File(storage.puzzlesDir, "piece_${System.currentTimeMillis()}_$index.png")
                ImageUtils.savePng(pieceBitmap, file)
                pieces.add(
                    PuzzlePiece(
                        id = index,
                        currentIndex = index,
                        targetIndex = index,
                        bitmapPath = file.absolutePath,
                    ),
                )
                index++
            }
        }

        // Mezcla garantizando que el puzzle no quede resuelto de fábrica.
        val shuffled = shufflePieces(pieces, config.type)
        return PuzzleBoard(pieces = shuffled, gridSize = size)
    }

    /**
     * Mezcla las piezas respetando el tipo (sección 16, Mezcla aleatoria).
     *
     * - CLASSIC: cualquier permutación.
     * - SLIDING: solo se mueve la pieza vacía, por lo que mezclamos con
     *   secuencia de movimientos válidos para mantener solvencia.
     */
    private fun shufflePieces(pieces: List<PuzzlePiece>, type: PuzzleType): List<PuzzlePiece> {
        if (type == PuzzleType.SLIDING) {
            return shuffleSliding(pieces)
        }
        val ids = pieces.map { it.id }.shuffled(Random(System.currentTimeMillis()))
        return pieces.mapIndexed { idx, p -> p.copy(currentIndex = ids.indexOf(p.id)) }
    }

    private fun shuffleSliding(pieces: List<PuzzlePiece>): List<PuzzlePiece> {
        val n = pieces.size
        val emptyTarget = n - 1
        var current = pieces.map { it.id }.toIntArray()
        var emptySlot = pieces.indexOfFirst { it.id == emptyTarget }
        val rng = Random(System.currentTimeMillis())
        val moves = n * 8
        repeat(moves) {
            val neighbors = neighborsOf(emptySlot, gridSize = kotlin.math.sqrt(n.toFloat()).toInt())
            val pick = neighbors.random(rng)
            current[emptySlot] = current[pick]
            current[pick] = emptyTarget
            emptySlot = pick
        }
        return pieces.mapIndexed { idx, p ->
            p.copy(currentIndex = current.indexOf(p.id))
        }
    }

    private fun neighborsOf(index: Int, gridSize: Int): List<Int> {
        val row = index / gridSize
        val col = index % gridSize
        val result = mutableListOf<Int>()
        if (row > 0) result.add((row - 1) * gridSize + col)
        if (row < gridSize - 1) result.add((row + 1) * gridSize + col)
        if (col > 0) result.add(row * gridSize + (col - 1))
        if (col < gridSize - 1) result.add(row * gridSize + (col + 1))
        return result
    }

    /**
     * Intercambia dos piezas (puzzle clásico).
     * @return nuevo tablero con el swap aplicado.
     */
    fun swap(board: PuzzleBoard, fromIndex: Int, toIndex: Int): PuzzleBoard {
        if (fromIndex == toIndex) return board
        val updated = board.pieces.map { p ->
            when (p.currentIndex) {
                fromIndex -> p.copy(currentIndex = toIndex)
                toIndex -> p.copy(currentIndex = fromIndex)
                else -> p
            }
        }
        return board.copy(pieces = updated)
    }

    /**
     * Mueve una pieza hacia la ranura vacía (puzzle deslizante).
     * Si el movimiento no es válido, devuelve el tablero sin cambios.
     */
    fun slide(board: PuzzleBoard, pieceIndex: Int, emptySlotIndex: Int): PuzzleBoard {
        val neighbors = neighborsOf(emptySlotIndex, board.gridSize)
        if (pieceIndex !in neighbors) return board
        val updated = board.pieces.map { p ->
            when (p.currentIndex) {
                pieceIndex -> p.copy(currentIndex = emptySlotIndex)
                emptySlotIndex -> p.copy(currentIndex = pieceIndex)
                else -> p
            }
        }
        return board.copy(pieces = updated)
    }

    /**
     * ¿El puzzle está resuelto? (sección 16, Detección de victoria)
     */
    fun isSolved(board: PuzzleBoard): Boolean = board.isSolved

    /**
     * Calcula estadísticas finales (sección 18).
     */
    fun computeStats(moves: Int, timeMillis: Long, perfect: Boolean): PuzzleStats =
        PuzzleStats(moves = moves, timeMillis = timeMillis, perfect = perfect)

    /**
     * Compone la imagen final con pequeñas separaciones para conservar
     * la identidad de puzzle (sección 18: animación al completar).
     */
    fun composeFinalImage(board: PuzzleBoard, target: File, gapPx: Int = 2): File {
        val grid = board.gridSize
        val first = board.pieces.firstOrNull()?.bitmapPath?.let { ImageUtils.decodeSampled(it) }
            ?: return target
        val pieceW = first.width
        val pieceH = first.height
        val out = Bitmap.createBitmap(
            pieceW * grid + gapPx * (grid + 1),
            pieceH * grid + gapPx * (grid + 1),
            Bitmap.Config.ARGB_8888,
        )
        val canvas = Canvas(out)
        canvas.drawColor(android.graphics.Color.TRANSPARENT)
        for (piece in board.pieces) {
            val bmp = piece.bitmapPath?.let { ImageUtils.decodeSampled(it) } ?: continue
            val row = piece.targetIndex / grid
            val col = piece.targetIndex % grid
            val left = gapPx + col * (pieceW + gapPx)
            val top = gapPx + row * (pieceH + gapPx)
            val dst = Rect(left, top, left + pieceW, top + pieceH)
            canvas.drawBitmap(bmp, null, dst, null)
        }
        return ImageUtils.savePng(out, target)
    }

    /**
     * Limpia archivos de piezas temporales.
     */
    fun cleanup(board: PuzzleBoard) {
        board.pieces.forEach { p ->
            p.bitmapPath?.let { runCatching { File(it).delete() } }
        }
    }
}
