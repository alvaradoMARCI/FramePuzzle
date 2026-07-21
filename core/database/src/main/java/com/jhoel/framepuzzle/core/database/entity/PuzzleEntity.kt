package com.jhoel.framepuzzle.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Puzzle.
 *
 * Un recuerdo puede generar varios puzzles (diferentes tipos o dificultades).
 *
 * Campos según FramePuzzle_Master_Document (sección 34, Entidad Puzzle):
 *  - id, memoryId, difficulty, pieces, completed, time, moves
 */
@Entity(
    tableName = "puzzles",
    foreignKeys = [
        ForeignKey(
            entity = MemoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["memory_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("memory_id")],
)
data class PuzzleEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "memory_id") val memoryId: String,
    @ColumnInfo(name = "type") val type: PuzzleType,
    @ColumnInfo(name = "difficulty") val difficulty: PuzzleDifficulty,
    @ColumnInfo(name = "pieces") val pieces: Int,
    @ColumnInfo(name = "completed") val completed: Boolean,
    @ColumnInfo(name = "time_millis") val time: Long,
    @ColumnInfo(name = "moves") val moves: Int,
    @ColumnInfo(name = "created_date") val createdDate: Long,
)

/** Tipos de puzzle (sección 15). */
enum class PuzzleType {
    CLASSIC,
    SLIDING,
}

/** Niveles de dificultad (sección 17). */
enum class PuzzleDifficulty {
    EASY,
    NORMAL,
    HARD,
    CUSTOM,
}
