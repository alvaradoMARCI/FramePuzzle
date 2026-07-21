package com.jhoel.framepuzzle.feature.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.core.domain.model.Memory
import com.jhoel.framepuzzle.core.domain.usecase.CreateMemoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(
    private val createMemoryUseCase: CreateMemoryUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state: StateFlow<CameraState> = _state.asStateFlow()

    fun onImageReady(path: String) {
        _state.value = _state.value.copy(pendingPath = path)
    }

    fun discard() {
        _state.value = _state.value.copy(pendingPath = null, error = null)
    }

    fun confirm() {
        val path = _state.value.pendingPath ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isCreating = true, error = null)
            try {
                val memory = createMemoryUseCase(path)
                _state.value = _state.value.copy(isCreating = false, pendingPath = null, createdMemoryId = memory.id)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isCreating = false, error = e.message ?: "Error al crear recuerdo")
            }
        }
    }

    fun consumeCreated() {
        _state.value = _state.value.copy(createdMemoryId = null)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}

data class CameraState(
    val pendingPath: String? = null,
    val isCreating: Boolean = false,
    val createdMemoryId: String? = null,
    val error: String? = null,
)
