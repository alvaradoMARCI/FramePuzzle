package com.jhoel.framepuzzle.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Destinos de la bottom bar de FramePuzzle (sección 7).
 *
 * El botón central "+" es el Crear (acción principal del producto).
 */
enum class FramePuzzleTopDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    HOME(
        route = FramePuzzleRoutes.HOME,
        label = "Inicio",
        selectedIcon = Icons.Rounded.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    LIBRARY(
        route = FramePuzzleRoutes.LIBRARY,
        label = "Biblioteca",
        selectedIcon = Icons.Rounded.GridView,
        unselectedIcon = Icons.Outlined.GridView,
    ),
    CREATE(
        route = FramePuzzleRoutes.CREATE,
        label = "Crear",
        selectedIcon = Icons.Rounded.AddCircle,
        unselectedIcon = Icons.Outlined.AddCircle,
    ),
    PROFILE(
        route = FramePuzzleRoutes.PROFILE,
        label = "Perfil",
        selectedIcon = Icons.Rounded.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
}
