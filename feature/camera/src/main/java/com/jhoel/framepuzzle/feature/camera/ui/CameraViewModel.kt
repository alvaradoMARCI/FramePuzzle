package com.jhoel.framepuzzle.feature.camera.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.feature.camera.CameraSelectorUi
import com.jhoel.framepuzzle.feature.camera.CameraUiState
import com.jhoel.framepuzzle.feature.camera.domain.CreateMemoryFromImageUseCase
import com.jhoel.framepuzzle.core.utils.result.Failure
import com.jhoel.framepuzzle.core.utils.result.FramePuzzleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel de la pantalla Crear/Cámara (sección 11).
 *
 * Maneja:
 *  - Estado de permisos.
 *  - Captura de imagen (CameraX) o selección desde galería.
 *  - Pantalla de confirmación de la imagen.
 *  - Creación del recuerdo (solo al confirmar).
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
                cameraSelector = if (it.cameraSelector == CameraSelectorUi.BACK) CameraSelectorUi.FRONT
                else CameraSelectorUi.BACK,
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
     * Se llama cuando CameraX captura una foto correctamente.
     * Muestra la pantalla de confirmación.
     */
    fun onImageCaptured(path: String) {
        _uiState.update { it.copy(pendingImagePath = path, isCapturing = false) }
    }

    /**
     * Se llama cuando el usuario elige una imagen de la galería.
     * Muestra la pantalla de confirmación.
     */
    fun onImagePicked(path: String) {
        _uiState.update { it.copy(pendingImagePath = path) }
    }

    /**
     * Descarta la imagen pendiente y vuelve al modo captura.
     */
    fun discardCapture() {
        _uiState.update { it.copy(pendingImagePath = null) }
    }

    /**
     * Confirma la imagen pendiente: crea el recuerdo y navega al editor.
     */
    fun confirmCapture() {
        val pending = _uiState.value.pendingImagePath ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isCapturing = true, error = null) }
            val result = withContext(Dispatchers.IO) {
                createMemoryUseCase(pending, title = "Recuerdo")
            }
            when (result) {
                is FramePuzzleResult.Success -> _uiState.update {
                    it.copy(isCapturing = false, capturedMemoryId = result.data.id, pendingImagePath = null)
                }
                is FramePuzzleResult.Failed -> _uiState.update {
                    val message = when (val err = result.error) {
                        is Failure.PermissionDenied -> "Permiso denegado"
                        is Failure.StorageFull -> "Almacenamiento lleno"
                        is Failure.NotFound -> "Archivo no encontrado"
                        is Failure.InvalidFormat -> "Formato inválido"
                        is Failure.Unauthorized -> "No autorizado"
                        is Failure.NetworkUnavailable -> "Sin red"
                        is Failure.Unknown -> err.message ?: "No se pudo crear el recuerdo"
                    }
                    it.copy(isCapturing = false, error = message)
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
