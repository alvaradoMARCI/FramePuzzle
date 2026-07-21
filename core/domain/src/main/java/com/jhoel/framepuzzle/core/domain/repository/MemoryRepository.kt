package com.jhoel.framepuzzle.core.domain.repository

import com.jhoel.framepuzzle.core.domain.model.Memory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MemoryRepository {
    fun observeAll(): Flow<List<Memory>>
    suspend fun count(): Int
    suspend fun create(memory: Memory)
}

class MemoryRepositoryImpl(
    private val memoryDao: com.jhoel.framepuzzle.core.database.dao.MemoryDao
) : MemoryRepository {

    override fun observeAll(): Flow<List<Memory>> =
        memoryDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun count(): Int = memoryDao.count()

    override suspend fun create(memory: Memory) {
        memoryDao.upsert(memory.toEntity())
    }

    private fun com.jhoel.framepuzzle.core.database.entity.MemoryEntity.toDomain() = Memory(
        id = id, title = title, originalImagePath = originalImage,
        editedImagePath = editedImage, createdDate = createdDate,
        albumId = albumId, progress = progress, favorite = favorite
    )

    private fun Memory.toEntity() = com.jhoel.framepuzzle.core.database.entity.MemoryEntity(
        id = id, title = title, originalImage = originalImagePath,
        editedImage = editedImagePath, createdDate = createdDate,
        albumId = albumId, progress = progress, favorite = favorite
    )
}
