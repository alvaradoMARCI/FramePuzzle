package com.jhoel.framepuzzle.feature.camera

import androidx.compose.runtime.Immutable

/**
 * Estado UI de la pantalla Cámara/Crear (sección 11 + 12).
 */
@Immutable
data class CameraUiState(
    val hasCameraPermission: Boolean = false,
    val hasGalleryPermission: Boolean = false,
    val isCapturing: Boolean = false,
    val capturedMemoryId: String? = null,
    val error: String? = null,
    val cameraSelector: CameraSelectorUi = CameraSelectorUi.BACK,
    val flashEnabled: Boolean = false,
)

enum class CameraSelectorUi { FRONT, BACK }
