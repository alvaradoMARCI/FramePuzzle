package com.jhoel.framepuzzle.feature.profile

import androidx.compose.runtime.Immutable

/**
 * Estado UI de la pantalla Home.
 *
 * Incluye: avatar, nivel, XP, recuerdo destacado, accesos rápidos y stats.
 */
@Immutable
data class HomeUiState(
    val isLogged: Boolean = false,
    val userName: String = "",
    val avatarPath: String? = null,
    val level: Int = 1,
    val currentXp: Int = 0,
    val requiredXp: Int = 100,
    val featuredMemory: FeaturedMemoryUi? = null,
    val totalMemories: Int = 0,
    val totalPuzzlesCompleted: Int = 0,
    val totalAchievements: Int = 0,
)

@Immutable
data class FeaturedMemoryUi(
    val id: String,
    val title: String,
    val originalImagePath: String,
    val editedImagePath: String?,
    val progress: Float,
)
