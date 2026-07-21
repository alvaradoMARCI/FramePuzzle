package com.jhoel.framepuzzle.core.database.converter

import androidx.room.TypeConverter
import com.jhoel.framepuzzle.core.database.entity.AlbumType
import com.jhoel.framepuzzle.core.database.entity.PuzzleDifficulty
import com.jhoel.framepuzzle.core.database.entity.PuzzleType

class FramePuzzleConverters {
    @TypeConverter fun puzzleTypeToString(t: PuzzleType) = t.name
    @TypeConverter fun stringToPuzzleType(v: String) = runCatching { PuzzleType.valueOf(v) }.getOrDefault(PuzzleType.CLASSIC)
    @TypeConverter fun difficultyToString(d: PuzzleDifficulty) = d.name
    @TypeConverter fun stringToDifficulty(v: String) = runCatching { PuzzleDifficulty.valueOf(v) }.getOrDefault(PuzzleDifficulty.NORMAL)
    @TypeConverter fun albumTypeToString(t: AlbumType) = t.name
    @TypeConverter fun stringToAlbumType(v: String) = runCatching { AlbumType.valueOf(v) }.getOrDefault(AlbumType.MANUAL)
}
