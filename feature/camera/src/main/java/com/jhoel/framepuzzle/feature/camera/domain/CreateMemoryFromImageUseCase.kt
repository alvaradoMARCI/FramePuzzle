package com.jhoel.framepuzzle.feature.camera.domain

import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import com.jhoel.framepuzzle.core.utils.result.Failure
import com.jhoel.framepuzzle.core.utils.result.FramePuzzleResult
import com.jhoel.framepuzzle.core.utils.result.framePuzzleRun
import com.jhoel.framepuzzle.feature.library.data.MemoryRepository
import com.jhoel.framepuzzle.feature.library.domain.Memory
import java.io.File
import javax.inject.Inject

/**
 * Caso de uso: crear un nuevo recuerdo desde una imagen capturada o importada.
 *
 * Regla (sección 9): el recuerdo original nunca debe modificarse.
 * Esta función crea la copia interna y deja editedImagePath=null.
 *
 * Flujo (sección 10):
 *   Tomar foto / Elegir de galería
 *     ↓
 *   Copia interna a original/
 *     ↓
 *   Memory inicial (editedImage=null, progress=0, favorite=false)
 */
class CreateMemoryFromImageUseCase @Inject constructor(
    private val storage: LocalStorageManager,
    private val memoryRepository: MemoryRepository,
) {

    suspend operator fun invoke(
        sourcePath: String,
        title: String,
    ): FramePuzzleResult<Memory> = framePuzzleRun {
        require(sourcePath.isNotBlank()) { "sourcePath vacía" }
        val sourceFile = File(sourcePath)
        require(sourceFile.exists()) { "Archivo no existe: $sourcePath" }

        val memoryId = java.util.UUID.randomUUID().toString()
        val target = storage.newOriginalFile(memoryId)

        // Copia interna (no se toca el archivo original del usuario)
        sourceFile.copyTo(target, overwrite = true)

        val memory = Memory(
            id = memoryId,
            title = title.trim().ifBlank { "Recuerdo ${System.currentTimeMillis()}" },
            originalImagePath = target.absolutePath, // ruta absoluta para Coil
            editedImagePath = null,
            createdDate = System.currentTimeMillis(),
            albumId = null,
            progress = 0f,
            favorite = false,
        )
        memoryRepository.create(memory)
        memory
    }
}
