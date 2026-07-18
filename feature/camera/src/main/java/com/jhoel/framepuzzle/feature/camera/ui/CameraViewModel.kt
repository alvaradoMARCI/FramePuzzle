package com.jhoel.framepuzzle.feature.camera.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.feature.camera.CameraSelectorUi
import com.jhoel.framepuzzle.feature.camera.CameraUiState
import com.jhoel.framepuzzle.feature.camera.domain.CreateMemoryFromImageUseCase
import com.jhoel.framepuzzle.core.utils.result.FramePuzzleResult
import com.jhoel.framepuzzle.core.utils.result.Failure
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla Crear/Cámara (sección 11).
 *
 * Maneja:
 *  - Estado de permisos.
 *  - Captura de imagen (CameraX).
 *  - Importación desde galería.
 *  - Creación del recuerdo.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val createMemoryUseCase: CreateMemoryFromImageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun setPermissions(camera: Boolean, gallery: Boolean) {
        _uiState.update { it.copy(hasCameraPermission = camera, hasGalleryPermission = gallery) }
    }

    fun flipCamera() {
        _uiState.update {
            it.copy(
                cameraSelector = if (it.cameraSelector == CameraSelectorUi.BACK) CameraSelectorUi.FRONT else CameraSelectorUi.BACK,
            )
        }
    }

    fun toggleFlash() {
        _uiState.update { it.copy(flashEnabled = !it.flashEnabled) }
    }

    fun setCapturing(capturing: Boolean) {
        _uiState.update { it.copy(isCapturing = capturing) }
    }

    /**
     * Crea un recuerdo a partir de una imagen capturada o importada.
     * Tras crear, expone el nuevo ID en el estado (la UI navega al editor).
     */
    fun createMemory(imagePath: String, title: String = "Recuerdo") {
        viewModelScope.launch {
            _uiState.update { it.copy(isCapturing = true, error = null) }
            when (val result = createMemoryUseCase(imagePath, title)) {
                is FramePuzzleResult.Success -> _uiState.update {
                    it.copy(isCapturing = false, capturedMemoryId = result.data.id)
                }
                is FramePuzzleResult.Failure -> _uiState.update {
                    it.copy(
                        isCapturing = false,
                        error = when (result.error) {
                            is Failure.PermissionDenied -> "Permiso denegado"
                            is Failure.StorageFull -> "Almacenamiento lleno"
                            is Failure.NotFound -> "Archivo no encontrado"
                            is Failure.InvalidFormat -> "Formato inválido"
                            is Failure.Unauthorized -> "No autorizado"
                            is Failure.NetworkUnavailable -> "Sin red"
                            is Failure.Unknown -> result.error.message ?: "No se pudo crear el recuerdo"
                        },
                    )
                }
            }
        }
    }

    fun consumeNavigation() {
        _uiState.update { it.copy(capturedMemoryId = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
