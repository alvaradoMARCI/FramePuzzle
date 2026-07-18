package com.jhoel.framepuzzle.feature.puzzle

import androidx.compose.runtime.Immutable
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleBoard
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleConfig
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleDifficulty
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleType
import com.jhoel.framepuzzle.feature.puzzle.engine.PuzzleEngine

@Immutable
data class PuzzleUiState(
    val isLoading: Boolean = true,
    val memoryId: String = "",
    val imagePath: String? = null,
    val title: String = "",
    val config: PuzzleConfig = PuzzleConfig(PuzzleType.CLASSIC, PuzzleDifficulty.NORMAL),
    val board: PuzzleBoard? = null,
    val moves: Int = 0,
    val elapsedMillis: Long = 0L,
    val isCompleted: Boolean = false,
    val showCompletionAnimation: Boolean = false,
    val error: String? = null,
)

/**
 * Resultado de un movimiento del usuario.
 */
sealed interface PuzzleMoveResult {
    data object Moved : PuzzleMoveResult
    data object Invalid : PuzzleMoveResult
    data object Solved : PuzzleMoveResult
}
