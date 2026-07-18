package com.jhoel.framepuzzle.feature.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.feature.library.LibraryMemoryUi
import com.jhoel.framepuzzle.feature.library.LibraryTab
import com.jhoel.framepuzzle.feature.library.LibraryUiState
import com.jhoel.framepuzzle.feature.library.data.AlbumRepository
import com.jhoel.framepuzzle.feature.library.data.MemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val memoryRepository: MemoryRepository,
    private val albumRepository: AlbumRepository,
) : ViewModel() {

    private val _tab = MutableStateFlow(LibraryTab.MEMORIES)
    val tab = _tab.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val uiState: StateFlow<LibraryUiState> = combine(
        _tab,
        _query,
        memoryRepository.observeAll(),
        albumRepository.observeAll(),
        memoryRepository.observeFavorites(),
    ) { tab, query, memories, albums, favorites ->
        val filtered = when (tab) {
            LibraryTab.MEMORIES -> memories
            LibraryTab.FAVORITES -> favorites
            else -> memories
        }.filter { it.title.contains(query, ignoreCase = true) }
            .map {
                LibraryMemoryUi(
                    id = it.id,
                    title = it.title,
                    thumbnailPath = it.editedImagePath ?: it.originalImagePath,
                    favorite = it.favorite,
                    createdDate = it.createdDate,
                    progress = it.progress,
                )
            }
        LibraryUiState(
            isLoading = false,
            memories = filtered,
            albums = albums.map {
                com.jhoel.framepuzzle.feature.library.LibraryAlbumUi(
                    id = it.id,
                    name = it.name,
                    coverPath = it.coverPath,
                    isAutomatic = it.isAutomatic,
                    memoryCount = 0,
                )
            },
            selectedTab = tab,
            query = query,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LibraryUiState(),
    )

    fun setTab(tab: LibraryTab) {
        _tab.update { tab }
    }

    fun setQuery(q: String) {
        _query.update { q }
    }

    fun toggleFavorite(memoryId: String, currentFavorite: Boolean) {
        viewModelScope.launch {
            memoryRepository.setFavorite(memoryId, !currentFavorite)
        }
    }
}
