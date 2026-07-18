package com.jhoel.framepuzzle.feature.editor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.feature.editor.EditorUiState
import com.jhoel.framepuzzle.feature.editor.data.ImageProcessor
import com.jhoel.framepuzzle.feature.editor.domain.CropRect
import com.jhoel.framepuzzle.feature.editor.domain.EditorAdjustments
import com.jhoel.framepuzzle.feature.editor.domain.FramePuzzleFilter
import com.jhoel.framepuzzle.feature.library.data.MemoryRepository
import com.jhoel.framepuzzle.feature.library.data.UserRepository
import com.jhoel.framepuzzle.feature.library.domain.XpEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del Editor (sección 13).
 *
 * Garantiza el flujo no destructivo:
 *   Original → Ediciones → Puzzle → Experiencia final.
 */
@HiltViewModel
class EditorViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository,
    private val imageProcessor: ImageProcessor,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    fun load(memoryId: String) {
        viewModelScope.launch {
            val memory = memoryRepository.getById(memoryId) ?: run {
                _uiState.update { it.copy(isLoading = false, error = "Recuerdo no encontrado") }
                return@launch
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    originalPath = memory.originalImagePath,
                    editedPath = memory.editedImagePath,
                    title = memory.title,
                )
            }
        }
    }

    fun updateAdjustments(transform: (EditorAdjustments) -> EditorAdjustments) {
        _uiState.update { it.copy(adjustments = transform(it.adjustments)) }
    }

    fun setFilter(filter: FramePuzzleFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun rotate90() = updateAdjustments {
        it.copy(rotationDegrees = (it.rotationDegrees + 90) % 360)
    }

    fun setCrop(crop: CropRect?) = updateAdjustments { it.copy(crop = crop) }

    fun reset() {
        _uiState.update {
            it.copy(adjustments = EditorAdjustments(), filter = FramePuzzleFilter.NONE)
        }
    }

    /**
     * Aplica las ediciones sobre el original y guarda en edited/.
     * El original nunca se toca.
     */
    fun save(memoryId: String) {
        val original = _uiState.value.originalPath ?: return
        _uiState.update { it.copy(isSaving = true) }
        viewModelScope.launch {
            try {
                val saved = imageProcessor.applyAndSave(
                    originalPath = original,
                    memoryId = memoryId,
                    adjustments = _uiState.value.adjustments,
                    filter = _uiState.value.filter,
                )
                memoryRepository.updateEditedImage(memoryId, saved.absolutePath)
                // XP por editar (sección 19)
                val user = userRepository.getCurrent()
                if (user != null) {
                    userRepository.addXp(XpEvent.MEMORY_EDITED, user)
                }
                _uiState.update { it.copy(isSaving = false, editedPath = saved.absolutePath, saved = true) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(isSaving = false, error = t.message ?: "Error al guardar") }
            }
        }
    }

    fun consumeSaved() {
        _uiState.update { it.copy(saved = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
