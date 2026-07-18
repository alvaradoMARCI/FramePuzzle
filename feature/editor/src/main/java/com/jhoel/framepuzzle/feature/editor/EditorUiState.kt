package com.jhoel.framepuzzle.feature.editor

import androidx.compose.runtime.Immutable
import com.jhoel.framepuzzle.feature.editor.domain.EditorAdjustments
import com.jhoel.framepuzzle.feature.editor.domain.FramePuzzleFilter
import com.jhoel.framepuzzle.feature.editor.domain.VisualElement

@Immutable
data class EditorUiState(
    val isLoading: Boolean = true,
    val originalPath: String? = null,
    val editedPath: String? = null,
    val title: String = "",
    val adjustments: EditorAdjustments = EditorAdjustments(),
    val filter: FramePuzzleFilter = FramePuzzleFilter.NONE,
    val visualElements: List<VisualElement> = emptyList(),
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
)
