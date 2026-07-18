package com.jhoel.framepuzzle.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jhoel.framepuzzle.core.database.entity.MemoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {

    @Query("SELECT * FROM memories ORDER BY created_date DESC")
    fun observeAll(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE favorite = 1 ORDER BY created_date DESC")
    fun observeFavorites(): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE album_id = :albumId ORDER BY created_date DESC")
    fun observeByAlbum(albumId: String): Flow<List<MemoryEntity>>

    @Query("SELECT * FROM memories WHERE id = :id")
    suspend fun getById(id: String): MemoryEntity?

    @Query("SELECT * FROM memories WHERE id = :id")
    fun observeById(id: String): Flow<MemoryEntity?>

    @Query("SELECT * FROM memories ORDER BY created_date DESC LIMIT 1")
    fun observeFeatured(): Flow<MemoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(memory: MemoryEntity)

    @Update
    suspend fun update(memory: MemoryEntity)

    @Query("UPDATE memories SET edited_image = :path WHERE id = :id")
    suspend fun updateEditedImage(id: String, path: String?)

    @Query("UPDATE memories SET favorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: String, favorite: Boolean)

    @Query("UPDATE memories SET progress = :progress WHERE id = :id")
    suspend fun updateProgress(id: String, progress: Float)

    @Delete
    suspend fun delete(memory: MemoryEntity)

    @Query("DELETE FROM memories WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM memories")
    suspend fun count(): Int
}
