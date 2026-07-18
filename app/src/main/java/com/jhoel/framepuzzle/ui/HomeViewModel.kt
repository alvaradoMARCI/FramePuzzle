package com.jhoel.framepuzzle.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.feature.profile.HomeUiState
import com.jhoel.framepuzzle.feature.profile.FeaturedMemoryUi
import com.jhoel.framepuzzle.feature.library.data.UserRepository
import com.jhoel.framepuzzle.feature.library.data.MemoryRepository
import com.jhoel.framepuzzle.feature.puzzle.data.PuzzleRepository
import com.jhoel.framepuzzle.feature.library.data.AchievementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel de la pantalla Home (sección 7: Inicio).
 *
 * Combina datos de usuario, recuerdos, puzzles completados y logros.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    memoryRepository: MemoryRepository,
    puzzleRepository: PuzzleRepository,
    achievementRepository: AchievementRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        userRepository.observeUser(),
        memoryRepository.observeAll(),
        puzzleRepository.observeCompleted(),
        achievementRepository.observeUnlocked(),
    ) { user, memories, puzzles, achievements ->
        val featured = memories.firstOrNull()
        HomeUiState(
            isLogged = user != null,
            userName = user?.name ?: "",
            avatarPath = user?.avatarPath,
            level = user?.level ?: 1,
            currentXp = user?.xp ?: 0,
            requiredXp = user?.xpForNextLevel() ?: 100,
            featuredMemory = featured?.let {
                FeaturedMemoryUi(
                    id = it.id,
                    title = it.title,
                    originalImagePath = it.originalImagePath,
                    editedImagePath = it.editedImagePath,
                    progress = it.progress,
                )
            },
            totalMemories = memories.size,
            totalPuzzlesCompleted = puzzles.size,
            totalAchievements = achievements.size,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )
}
