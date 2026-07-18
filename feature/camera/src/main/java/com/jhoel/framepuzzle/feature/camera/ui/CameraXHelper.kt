package com.jhoel.framepuzzle.feature.camera.ui

import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import com.jhoel.framepuzzle.core.utils.log.FramePuzzleLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper para abstraer CameraX (sección 11).
 *
 * FramePuzzle usa CameraX porque ofrece:
 *  - Vista previa fluida.
 *  - Enfoque automático.
 *  - Control básico de captura.
 *  - Compatibilidad con cámaras frontal y trasera.
 *
 * IMPORTANTE: el [ImageCapture] devuelto por [startPreview] es el mismo que
 * debe usarse para capturar fotos. Si se crea un ImageCapture aparte, no
 * estará conectado a la cámara y la captura nunca se completará.
 */
@Singleton
class CameraXHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storage: LocalStorageManager,
) {

    private val executor: Executor by lazy { ContextCompat.getMainExecutor(context) }

    /**
     * Inicia la preview de CameraX en el [PreviewView] indicado.
     *
     * @return el [ImageCapture] vinculado al ciclo de vida, que debe usarse
     *   para llamar a [capture]. Devuelve null si CameraX no puede iniciarse.
     */
    fun startPreview(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        lensFacing: Int,
        flashMode: Int = ImageCapture.FLASH_MODE_AUTO,
    ): ImageCapture? {
        val capture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setFlashMode(flashMode)
            .build()

        val providerFuture = ProcessCameraProvider.getInstance(context)
        providerFuture.addListener({
            try {
                val provider = providerFuture.get()
                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }
                val selector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build()
                provider.unbindAll()
                provider.bindToLifecycle(lifecycleOwner, selector, preview, capture)
                FramePuzzleLogger.d(TAG, "Preview iniciada con lensFacing=$lensFacing")
            } catch (t: Throwable) {
                FramePuzzleLogger.e(TAG, "Error iniciando preview CameraX", t)
            }
        }, executor)

        return capture
    }

    /**
     * Captura una foto y la guarda en el almacenamiento interno FramePuzzle/original/.
     * @param onSaved ruta absoluta del archivo guardado.
     * @param onError mensaje de error.
     */
    fun capture(
        imageCapture: ImageCapture,
        fileName: String,
        onSaved: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        val target = File(storage.originalDir, "$fileName.jpg")
        val output = ImageCapture.OutputFileOptions.Builder(target).build()
        imageCapture.takePicture(
            output,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    FramePuzzleLogger.d(TAG, "Captura guardada: ${target.absolutePath}")
                    onSaved(target.absolutePath)
                }

                override fun onError(exception: ImageCaptureException) {
                    FramePuzzleLogger.e(TAG, "Error capturando foto", exception)
                    onError(exception.message ?: "Error capturando foto")
                }
            },
        )
    }

    companion object {
        private const val TAG = "CameraXHelper"

        const val LENS_BACK = CameraSelector.LENS_FACING_BACK
        const val LENS_FRONT = CameraSelector.LENS_FACING_FRONT
    }
}
