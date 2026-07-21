package com.jhoel.framepuzzle.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jhoel.framepuzzle.core.database.converter.FramePuzzleConverters
import com.jhoel.framepuzzle.core.database.dao.*
import com.jhoel.framepuzzle.core.database.entity.*

@Database(
    entities = [
        UserEntity::class, MemoryEntity::class, PuzzleEntity::class,
        AlbumEntity::class, AchievementEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(FramePuzzleConverters::class)
abstract class FramePuzzleDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun memoryDao(): MemoryDao
    abstract fun puzzleDao(): PuzzleDao
    abstract fun albumDao(): AlbumDao
    abstract fun achievementDao(): AchievementDao
    companion object { const val NAME = "framepuzzle.db" }
}
