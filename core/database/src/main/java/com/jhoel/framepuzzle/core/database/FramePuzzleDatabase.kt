package com.jhoel.framepuzzle.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jhoel.framepuzzle.core.database.converter.FramePuzzleConverters
import com.jhoel.framepuzzle.core.database.dao.AchievementDao
import com.jhoel.framepuzzle.core.database.dao.AlbumDao
import com.jhoel.framepuzzle.core.database.dao.MemoryDao
import com.jhoel.framepuzzle.core.database.dao.PuzzleDao
import com.jhoel.framepuzzle.core.database.dao.UserDao
import com.jhoel.framepuzzle.core.database.entity.AchievementEntity
import com.jhoel.framepuzzle.core.database.entity.AlbumEntity
import com.jhoel.framepuzzle.core.database.entity.MemoryEntity
import com.jhoel.framepuzzle.core.database.entity.PuzzleEntity
import com.jhoel.framepuzzle.core.database.entity.UserEntity

/**
 * Base de datos Room de FramePuzzle.
 *
 * Almacena la información estructurada del usuario y sus recuerdos
 * (sección 34 del FramePuzzle_Master_Document).
 *
 * Entidades:
 *  - User
 *  - Memory
 *  - Puzzle
 *  - Album
 *  - Achievement
 *
 * Las imágenes en sí se guardan como archivos (ver core:storage);
 * la DB solo guarda rutas relativas.
 */
@Database(
    entities = [
        UserEntity::class,
        MemoryEntity::class,
        PuzzleEntity::class,
        AlbumEntity::class,
        AchievementEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(FramePuzzleConverters::class)
abstract class FramePuzzleDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun memoryDao(): MemoryDao
    abstract fun puzzleDao(): PuzzleDao
    abstract fun albumDao(): AlbumDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        const val NAME = "framepuzzle.db"
    }
}
