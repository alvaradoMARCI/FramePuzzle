package com.jhoel.framepuzzle.feature.puzzle

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleBoard
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleConfig
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleDifficulty
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleType

@Immutable
data class PuzzleUiState(
    val isLoading: Boolean = true,
    val memoryId: String = "",
    val imagePath: String? = null,
    val title: String = "",
    val config: PuzzleConfig = PuzzleConfig(PuzzleType.SLIDING, PuzzleDifficulty.NORMAL),
    val board: PuzzleBoard? = null,
    val moves: Int = 0,
    val elapsedMillis: Long = 0L,
    val isCompleted: Boolean = false,
    val showCompletionAnimation: Boolean = false,
    val error: String? = null,
)
