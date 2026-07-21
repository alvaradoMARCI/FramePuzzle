package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "puzzles", indices = [Index("memory_id")])
data class PuzzleEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "memory_id") val memoryId: String,
    val type: PuzzleType,
    val difficulty: PuzzleDifficulty,
    val pieces: Int,
    val completed: Boolean,
    @ColumnInfo(name = "time_millis") val timeMillis: Long,
    val moves: Int,
    @ColumnInfo(name = "created_date") val createdDate: Long,
)
enum class PuzzleType { CLASSIC, SLIDING }
enum class PuzzleDifficulty { EASY, NORMAL, HARD, CUSTOM }
