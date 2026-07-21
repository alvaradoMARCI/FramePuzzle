package com.jhoel.framepuzzle.core.database.converter

import androidx.room.TypeConverter
import com.jhoel.framepuzzle.core.database.entity.AlbumType
import com.jhoel.framepuzzle.core.database.entity.PuzzleDifficulty
import com.jhoel.framepuzzle.core.database.entity.PuzzleType

/**
 * Converters Room. Permiten persistir enums como String (legible en backups).
 */
class FramePuzzleConverters {

    @TypeConverter
    fun puzzleTypeToString(type: PuzzleType): String = type.name

    @TypeConverter
    fun stringToPuzzleType(value: String): PuzzleType =
        runCatching { PuzzleType.valueOf(value) }.getOrDefault(PuzzleType.CLASSIC)

    @TypeConverter
    fun puzzleDifficultyToString(d: PuzzleDifficulty): String = d.name

    @TypeConverter
    fun stringToPuzzleDifficulty(value: String): PuzzleDifficulty =
        runCatching { PuzzleDifficulty.valueOf(value) }.getOrDefault(PuzzleDifficulty.NORMAL)

    @TypeConverter
    fun albumTypeToString(t: AlbumType): String = t.name

    @TypeConverter
    fun stringToAlbumType(value: String): AlbumType =
        runCatching { AlbumType.valueOf(value) }.getOrDefault(AlbumType.MANUAL)
}
