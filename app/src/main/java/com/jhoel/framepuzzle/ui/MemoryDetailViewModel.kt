package com.jhoel.framepuzzle.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.feature.library.data.MemoryRepository
import com.jhoel.framepuzzle.feature.library.domain.Memory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Immutable
data class MemoryDetailUiState(
    val isLoading: Boolean = true,
    val memory: Memory? = null,
    val error: String? = null,
)

@HiltViewModel
class MemoryDetailViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoryDetailUiState())
    val uiState: StateFlow<MemoryDetailUiState> = _uiState.asStateFlow()

    fun load(memoryId: String) {
        viewModelScope.launch {
            val memory = withContext(Dispatchers.IO) {
                memoryRepository.getById(memoryId)
            }
            _uiState.update {
                if (memory == null) {
                    it.copy(isLoading = false, error = "Recuerdo no encontrado")
                } else {
                    it.copy(isLoading = false, memory = memory)
                }
            }
        }
    }

    fun toggleFavorite() {
        val current = _uiState.value.memory ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                memoryRepository.setFavorite(current.id, !current.favorite)
            }
            _uiState.update { st ->
                st.copy(memory = st.memory?.copy(favorite = !current.favorite))
            }
        }
    }
}
