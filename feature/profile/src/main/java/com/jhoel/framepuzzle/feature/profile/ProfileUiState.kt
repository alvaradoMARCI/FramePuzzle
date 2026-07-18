package com.jhoel.framepuzzle.feature.profile

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val avatarPath: String? = null,
    val level: Int = 1,
    val currentXp: Int = 0,
    val requiredXp: Int = 100,
    val totalMemories: Int = 0,
    val totalPuzzlesSolved: Int = 0,
    val achievements: List<ProfileAchievementUi> = emptyList(),
)

@Immutable
data class ProfileAchievementUi(
    val id: String,
    val name: String,
    val description: String,
    val unlocked: Boolean,
    val unlockedDate: Long?,
)
