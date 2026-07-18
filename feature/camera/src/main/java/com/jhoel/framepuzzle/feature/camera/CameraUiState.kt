package com.jhoel.framepuzzle.feature.camera

import androidx.compose.runtime.Immutable

/**
 * Estado UI de la pantalla Cámara/Crear (sección 11 + 12).
 *
 * Flujo:
 *   1. [pendingImagePath] == null: modo captura o galería.
 *   2. [pendingImagePath] != null: pantalla de confirmación.
 *      - confirmCapture() → crea el recuerdo y navega al editor.
 *      - discardCapture() → vuelve al modo captura.
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
    val pendingImagePath: String? = null,
)

enum class CameraSelectorUi { FRONT, BACK }
