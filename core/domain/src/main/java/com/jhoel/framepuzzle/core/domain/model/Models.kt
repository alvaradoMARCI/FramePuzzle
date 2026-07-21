package com.jhoel.framepuzzle.core.domain.model

data class User(
    val id: String,
    val name: String,
    val avatarPath: String?,
    val level: Int,
    val xp: Int,
    val createdDate: Long,
)

data class Memory(
    val id: String,
    val title: String,
    val originalImagePath: String,
    val editedImagePath: String?,
    val createdDate: Long,
    val albumId: String?,
    val progress: Float,
    val favorite: Boolean,
)

data class Puzzle(
    val id: String,
    val memoryId: String,
    val type: PuzzleType,
    val difficulty: PuzzleDifficulty,
    val pieces: Int,
    val completed: Boolean,
    val timeMillis: Long,
    val moves: Int,
    val createdDate: Long,
)

data class Album(
    val id: String,
    val name: String,
    val coverPath: String?,
    val isAutomatic: Boolean,
    val createdDate: Long,
)

data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val unlocked: Boolean,
    val unlockedDate: Long?,
    val xpReward: Int,
)

enum class PuzzleType { CLASSIC, SLIDING }
enum class PuzzleDifficulty(val pieces: Int) {
    EASY(9), NORMAL(16), HARD(36), CUSTOM(16)
}
