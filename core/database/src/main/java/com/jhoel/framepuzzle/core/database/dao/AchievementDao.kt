package com.jhoel.framepuzzle.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jhoel.framepuzzle.core.database.entity.AchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements ORDER BY unlocked DESC, name ASC")
    fun observeAll(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE unlocked = 1 ORDER BY unlocked_date DESC")
    fun observeUnlocked(): Flow<List<AchievementEntity>>

    @Query("SELECT COUNT(*) FROM achievements WHERE unlocked = 1")
    suspend fun countUnlocked(): Int

    @Query("SELECT * FROM achievements WHERE id = :id")
    suspend fun getById(id: String): AchievementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(achievement: AchievementEntity)

    @Update
    suspend fun update(achievement: AchievementEntity)

    @Query("UPDATE achievements SET unlocked = 1, unlocked_date = :date WHERE id = :id")
    suspend fun unlock(id: String, date: Long)
}
