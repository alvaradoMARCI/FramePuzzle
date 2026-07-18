package com.jhoel.framepuzzle.feature.puzzle

import com.google.common.truth.Truth.assertThat
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleBoard
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleConfig
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleDifficulty
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzlePiece
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleType
import org.junit.Test

/**
 * Tests del estado del tablero (no requieren Android ni archivos).
 *
 * El motor completo requiere Bitmap y se prueba con Robolectric en
 * una próxima iteración.
 */
class PuzzleBoardTest {

    @Test
    fun `tablero recien creado con piezas en orden esta resuelto`() {
        val pieces = (0 until 9).map { PuzzlePiece(id = it, currentIndex = it, targetIndex = it) }
        val board = PuzzleBoard(pieces = pieces, gridSize = 3)
        assertThat(board.isSolved).isTrue()
    }

    @Test
    fun `tablero con piezas desordenadas no esta resuelto`() {
        val pieces = (0 until 9).map { PuzzlePiece(id = it, currentIndex = (it + 1) % 9, targetIndex = it) }
        val board = PuzzleBoard(pieces = pieces, gridSize = 3)
        assertThat(board.isSolved).isFalse()
    }

    @Test
    fun `PuzzleConfig EASY tiene 9 piezas`() {
        val c = PuzzleConfig(PuzzleType.CLASSIC, PuzzleDifficulty.EASY)
        assertThat(c.totalPieces).isEqualTo(9)
        assertThat(c.gridSize).isEqualTo(3)
    }

    @Test
    fun `PuzzleConfig HARD tiene 36 piezas`() {
        val c = PuzzleConfig(PuzzleType.CLASSIC, PuzzleDifficulty.HARD)
        assertThat(c.totalPieces).isEqualTo(36)
    }

    @Test
    fun `PuzzleConfig CUSTOM respeta customPieces`() {
        val c = PuzzleConfig(PuzzleType.CLASSIC, PuzzleDifficulty.CUSTOM, customPieces = 25)
        assertThat(c.totalPieces).isEqualTo(25)
    }

    @Test
    fun `PuzzleConfig CUSTOM rechaza piezas fuera de rango`() {
        val c = PuzzleConfig(PuzzleType.CLASSIC, PuzzleDifficulty.CUSTOM, customPieces = 2)
        assertThat(c.totalPieces).isEqualTo(PuzzleDifficulty.CUSTOM.pieces)
    }
}
