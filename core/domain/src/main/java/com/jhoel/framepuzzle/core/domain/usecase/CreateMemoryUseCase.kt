package com.jhoel.framepuzzle.core.domain.usecase

import com.jhoel.framepuzzle.core.domain.model.Memory
import com.jhoel.framepuzzle.core.domain.repository.MemoryRepository
import java.io.File

class CreateMemoryUseCase(
    private val memoryRepository: MemoryRepository,
    private val storageDir: File
) {
    suspend operator fun invoke(sourcePath: String): Memory {
        val memoryId = java.util.UUID.randomUUID().toString()
        val target = File(storageDir, "$memoryId.jpg")
        File(sourcePath).copyTo(target, overwrite = true)

        val memory = Memory(
            id = memoryId,
            title = "Recuerdo ${System.currentTimeMillis()}",
            originalImagePath = target.absolutePath,
            editedImagePath = null,
            createdDate = System.currentTimeMillis(),
            albumId = null,
            progress = 0f,
            favorite = false,
        )
        memoryRepository.create(memory)
        return memory
    }
}
