package com.jhoel.framepuzzle.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jhoel.framepuzzle.core.database.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Query("SELECT * FROM albums ORDER BY created_date DESC")
    fun observeAll(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM albums WHERE type = 'MANUAL' ORDER BY created_date DESC")
    fun observeManual(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM albums WHERE type = 'AUTOMATIC' ORDER BY name ASC")
    fun observeAutomatic(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getById(id: String): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(album: AlbumEntity)

    @Update
    suspend fun update(album: AlbumEntity)

    @Delete
    suspend fun delete(album: AlbumEntity)
}
