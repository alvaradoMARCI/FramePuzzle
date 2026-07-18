package com.jhoel.framepuzzle.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.jhoel.framepuzzle.feature.camera.CameraScreen
import com.jhoel.framepuzzle.feature.editor.EditorScreen
import com.jhoel.framepuzzle.feature.library.LibraryScreen
import com.jhoel.framepuzzle.feature.profile.ProfileScreen
import com.jhoel.framepuzzle.feature.profile.SecurityScreen
import com.jhoel.framepuzzle.feature.puzzle.PuzzleScreen
import com.jhoel.framepuzzle.feature.backup.BackupScreen
import com.jhoel.framepuzzle.feature.transfer.TransferScreen
import com.jhoel.framepuzzle.feature.settings.SettingsScreen
import com.jhoel.framepuzzle.ui.HomeScreen

/**
 * NavHost de FramePuzzle.
 *
 * Single-Activity architecture:
 *  - Bottom bar visible en las 4 pantallas principales.
 *  - En rutas internas (editor, puzzle, detalle) la bottom bar se oculta.
 */
@Composable
fun FramePuzzleNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = FramePuzzleTopDestination.entries.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    FramePuzzleTopDestination.entries.forEach { dest ->
                        val selected = backStackEntry?.destination?.hierarchy?.any { it.route == dest.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                    contentDescription = dest.label,
                                )
                            },
                            label = { Text(dest.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FramePuzzleRoutes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(FramePuzzleRoutes.HOME) {
                HomeScreen(
                    onQuickCreate = { navController.navigate(FramePuzzleRoutes.CREATE) },
                    onMemoryClick = { id -> navController.navigate(FramePuzzleRoutes.memoryDetail(id)) },
                )
            }
            composable(FramePuzzleRoutes.CREATE) {
                CameraScreen(
                    onMemoryCreated = { memoryId ->
                        navController.navigate(FramePuzzleRoutes.editor(memoryId)) {
                            popUpTo(FramePuzzleRoutes.CREATE) { inclusive = true }
                        }
                    },
                )
            }
            composable(FramePuzzleRoutes.LIBRARY) {
                LibraryScreen(
                    onMemoryClick = { id -> navController.navigate(FramePuzzleRoutes.memoryDetail(id)) },
                )
            }
            composable(FramePuzzleRoutes.PROFILE) {
                ProfileScreen(
                    onSettings = { navController.navigate(FramePuzzleRoutes.SETTINGS) },
                    onSecurity = { navController.navigate(FramePuzzleRoutes.SECURITY) },
                    onBackup = { navController.navigate(FramePuzzleRoutes.BACKUP) },
                    onTransfer = { navController.navigate(FramePuzzleRoutes.TRANSFER) },
                )
            }
            composable(
                route = FramePuzzleRoutes.EDITOR,
                arguments = listOf(navArgument("memoryId") { type = NavType.StringType }),
            ) { backStack ->
                val memoryId = backStack.arguments?.getString("memoryId").orEmpty()
                EditorScreen(
                    memoryId = memoryId,
                    onDone = { navController.navigate(FramePuzzleRoutes.puzzle(memoryId)) {
                        popUpTo(FramePuzzleRoutes.EDITOR) { inclusive = true }
                    } },
                )
            }
            composable(
                route = FramePuzzleRoutes.PUZZLE,
                arguments = listOf(navArgument("memoryId") { type = NavType.StringType }),
            ) { backStack ->
                val memoryId = backStack.arguments?.getString("memoryId").orEmpty()
                PuzzleScreen(
                    memoryId = memoryId,
                    onCompleted = { navController.navigate(FramePuzzleRoutes.HOME) {
                        popUpTo(FramePuzzleRoutes.HOME) { inclusive = true }
                    } },
                )
            }
            composable(FramePuzzleRoutes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
            composable(FramePuzzleRoutes.SECURITY) {
                SecurityScreen(onBack = { navController.popBackStack() })
            }
            composable(FramePuzzleRoutes.BACKUP) {
                BackupScreen(onBack = { navController.popBackStack() })
            }
            composable(FramePuzzleRoutes.TRANSFER) {
                TransferScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
