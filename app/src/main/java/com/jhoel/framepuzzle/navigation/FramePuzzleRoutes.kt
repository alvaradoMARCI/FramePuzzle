package com.jhoel.framepuzzle.navigation

/**
 * Rutas de navegación de FramePuzzle (sección 7).
 *
 * Navegación principal:
 *  - Inicio
 *  - Crear (+)
 *  - Biblioteca
 *  - Perfil
 */
object FramePuzzleRoutes {

    // Bottom nav
    const val HOME = "home"
    const val CREATE = "create"
    const val LIBRARY = "library"
    const val PROFILE = "profile"

    // Editor + Puzzle
    const val EDITOR = "editor/{memoryId}"
    fun editor(memoryId: String) = "editor/$memoryId"

    const val PUZZLE = "puzzle/{memoryId}"
    fun puzzle(memoryId: String) = "puzzle/$memoryId"

    // Detalle
    const val MEMORY_DETAIL = "memory/{memoryId}"
    fun memoryDetail(memoryId: String) = "memory/$memoryId"

    const val ALBUM_DETAIL = "album/{albumId}"
    fun albumDetail(albumId: String) = "album/$albumId"

    // Settings + Security
    const val SETTINGS = "settings"
    const val SECURITY = "security"
    const val BACKUP = "backup"
    const val TRANSFER = "transfer"

    // Onboarding
    const val ONBOARDING = "onboarding"
    const val LOCK = "lock"
}
