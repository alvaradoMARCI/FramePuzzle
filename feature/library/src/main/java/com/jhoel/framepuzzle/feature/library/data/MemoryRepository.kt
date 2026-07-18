package com.jhoel.framepuzzle.feature.library.data

import com.jhoel.framepuzzle.core.database.dao.MemoryDao
import com.jhoel.framepuzzle.core.database.entity.MemoryEntity
import com.jhoel.framepuzzle.feature.library.domain.Memory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository de recuerdos (sección 31, ejemplo MemoryRepository).
 *
 * Garantiza la regla: el recuerdo original nunca se modifica.
 * Las ediciones se guardan en `editedImagePath` (copia interna).
 */
@Singleton
class MemoryRepository @Inject constructor(
    private val memoryDao: MemoryDao,
) {

    fun observeAll(): Flow<List<Memory>> =
        memoryDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeFavorites(): Flow<List<Memory>> =
        memoryDao.observeFavorites().map { list -> list.map { it.toDomain() } }

    fun observeByAlbum(albumId: String): Flow<List<Memory>> =
        memoryDao.observeByAlbum(albumId).map { list -> list.map { it.toDomain() } }

    fun observeFeatured(): Flow<Memory?> =
        memoryDao.observeFeatured().map { it?.toDomain() }

    fun observeById(id: String): Flow<Memory?> =
        memoryDao.observeById(id).map { it?.toDomain() }

    suspend fun getById(id: String): Memory? = memoryDao.getById(id)?.toDomain()

    suspend fun create(memory: Memory) = memoryDao.upsert(memory.toEntity())

    suspend fun updateEditedImage(id: String, path: String?) =
        memoryDao.updateEditedImage(id, path)

    suspend fun setFavorite(id: String, favorite: Boolean) =
        memoryDao.setFavorite(id, favorite)

    suspend fun updateProgress(id: String, progress: Float) =
        memoryDao.updateProgress(id, progress)

    suspend fun delete(id: String) = memoryDao.deleteById(id)

    suspend fun count(): Int = memoryDao.count()
}

fun MemoryEntity.toDomain(): Memory = Memory(
    id = id,
    title = title,
    originalImagePath = originalImage,
    editedImagePath = editedImage,
    createdDate = createdDate,
    albumId = albumId,
    progress = progress,
    favorite = favorite,
)

fun Memory.toEntity(): MemoryEntity = MemoryEntity(
    id = id,
    title = title,
    originalImage = originalImagePath,
    editedImage = editedImagePath,
    createdDate = createdDate,
    albumId = albumId,
    progress = progress,
    favorite = favorite,
)
