package com.jhoel.framepuzzle.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jhoel.framepuzzle.core.database.entity.PuzzleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PuzzleDao {

    @Query("SELECT * FROM puzzles WHERE memory_id = :memoryId ORDER BY created_date DESC")
    fun observeByMemory(memoryId: String): Flow<List<PuzzleEntity>>

    @Query("SELECT * FROM puzzles WHERE id = :id")
    suspend fun getById(id: String): PuzzleEntity?

    @Query("SELECT * FROM puzzles WHERE completed = 1 ORDER BY created_date DESC")
    fun observeCompleted(): Flow<List<PuzzleEntity>>

    @Query("SELECT COUNT(*) FROM puzzles WHERE completed = 1")
    suspend fun countCompleted(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(puzzle: PuzzleEntity)

    @Update
    suspend fun update(puzzle: PuzzleEntity)

    @Query("UPDATE puzzles SET completed = :completed, time_millis = :time, moves = :moves WHERE id = :id")
    suspend fun markCompleted(id: String, completed: Boolean, time: Long, moves: Int)

    @Delete
    suspend fun delete(puzzle: PuzzleEntity)

    @Query("DELETE FROM puzzles WHERE memory_id = :memoryId")
    suspend fun deleteByMemory(memoryId: String)
}
