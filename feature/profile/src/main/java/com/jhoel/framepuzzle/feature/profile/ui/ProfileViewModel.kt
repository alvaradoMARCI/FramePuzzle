package com.jhoel.framepuzzle.feature.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.feature.library.data.MemoryRepository
import com.jhoel.framepuzzle.feature.profile.ProfileAchievementUi
import com.jhoel.framepuzzle.feature.profile.ProfileUiState
import com.jhoel.framepuzzle.feature.library.data.AchievementRepository
import com.jhoel.framepuzzle.feature.library.data.UserRepository
import com.jhoel.framepuzzle.feature.puzzle.data.PuzzleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    memoryRepository: MemoryRepository,
    puzzleRepository: PuzzleRepository,
    achievementRepository: AchievementRepository,
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        userRepository.observeUser(),
        memoryRepository.observeAll(),
        puzzleRepository.observeCompleted(),
        achievementRepository.observeAll(),
    ) { user, memories, puzzles, achievements ->
        ProfileUiState(
            isLoading = false,
            userName = user?.name ?: "Invitado",
            avatarPath = user?.avatarPath,
            level = user?.level ?: 1,
            currentXp = user?.xp ?: 0,
            requiredXp = user?.xpForNextLevel() ?: 100,
            totalMemories = memories.size,
            totalPuzzlesSolved = puzzles.size,
            achievements = achievements.map {
                ProfileAchievementUi(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    unlocked = it.unlocked,
                    unlockedDate = it.unlockedDate,
                )
            },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState(),
    )

    fun createInitialUser(name: String) {
        viewModelScope.launch {
            // Si no hay usuario, crear uno local (sección 20: avatar inicial).
        }
    }
}
