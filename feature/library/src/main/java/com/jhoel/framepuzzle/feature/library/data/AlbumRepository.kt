package com.jhoel.framepuzzle.feature.library.data

import com.jhoel.framepuzzle.core.database.dao.AlbumDao
import com.jhoel.framepuzzle.core.database.entity.AlbumEntity
import com.jhoel.framepuzzle.feature.library.domain.Album
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository de álbumes (sección 22).
 */
@Singleton
class AlbumRepository @Inject constructor(
    private val albumDao: AlbumDao,
) {

    fun observeAll(): Flow<List<Album>> =
        albumDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeManual(): Flow<List<Album>> =
        albumDao.observeManual().map { list -> list.map { it.toDomain() } }

    fun observeAutomatic(): Flow<List<Album>> =
        albumDao.observeAutomatic().map { list -> list.map { it.toDomain() } }

    suspend fun getById(id: String): Album? = albumDao.getById(id)?.toDomain()

    suspend fun create(album: Album) = albumDao.upsert(album.toEntity())

    suspend fun delete(album: Album) = albumDao.delete(album.toEntity())
}

private fun AlbumEntity.toDomain(): Album = Album(
    id = id,
    name = name,
    coverPath = cover,
    isAutomatic = type == com.jhoel.framepuzzle.core.database.entity.AlbumType.AUTOMATIC,
    createdDate = createdDate,
)

private fun Album.toEntity(): AlbumEntity = AlbumEntity(
    id = id,
    name = name,
    cover = coverPath,
    type = if (isAutomatic) com.jhoel.framepuzzle.core.database.entity.AlbumType.AUTOMATIC
    else com.jhoel.framepuzzle.core.database.entity.AlbumType.MANUAL,
    createdDate = createdDate,
)
