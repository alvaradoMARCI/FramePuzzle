package com.jhoel.framepuzzle.feature.library

import androidx.compose.runtime.Immutable

/**
 * Estado UI de la pantalla Biblioteca.
 */
@Immutable
data class LibraryUiState(
    val isLoading: Boolean = true,
    val memories: List<LibraryMemoryUi> = emptyList(),
    val albums: List<LibraryAlbumUi> = emptyList(),
    val selectedTab: LibraryTab = LibraryTab.MEMORIES,
    val query: String = "",
)

@Immutable
data class LibraryMemoryUi(
    val id: String,
    val title: String,
    val thumbnailPath: String,
    val favorite: Boolean,
    val createdDate: Long,
    val progress: Float,
)

@Immutable
data class LibraryAlbumUi(
    val id: String,
    val name: String,
    val coverPath: String?,
    val isAutomatic: Boolean,
    val memoryCount: Int,
)

enum class LibraryTab(val label: String) {
    MEMORIES("Recuerdos"),
    ALBUMS("Álbumes"),
    HISTORY("Historial"),
    FAVORITES("Favoritos"),
}
