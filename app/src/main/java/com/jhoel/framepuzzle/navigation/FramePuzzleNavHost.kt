package com.jhoel.framepuzzle.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
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
import com.jhoel.framepuzzle.feature.camera.CameraScreen
import com.jhoel.framepuzzle.feature.library.LibraryScreen
import com.jhoel.framepuzzle.feature.profile.ProfileScreen
import com.jhoel.framepuzzle.feature.settings.SettingsScreen
import com.jhoel.framepuzzle.ui.HomeScreen
import com.jhoel.framepuzzle.ui.OnboardingScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val CREATE = "create"
    const val LIBRARY = "library"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
}

enum class TopDestination(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME(Routes.HOME, "Inicio", Icons.Rounded.Home),
    LIBRARY(Routes.LIBRARY, "Biblioteca", Icons.Rounded.GridView),
    CREATE(Routes.CREATE, "Crear", Icons.Rounded.AddCircle),
    PROFILE(Routes.PROFILE, "Perfil", Icons.Rounded.Person),
}

@Composable
fun FramePuzzleNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = TopDestination.entries.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    TopDestination.entries.forEach { dest ->
                        val selected = backStackEntry?.destination?.hierarchy?.any { it.route == dest.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(dest.icon, contentDescription = dest.label) },
                            label = { Text(dest.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.ONBOARDING,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(onDone = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                })
            }
            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToCreate = { navController.navigate(Routes.CREATE) }
                )
            }
            composable(Routes.CREATE) {
                CameraScreen(
                    onMemoryCreated = {
                        navController.navigate(Routes.LIBRARY) {
                            popUpTo(Routes.CREATE) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.LIBRARY) {
                LibraryScreen()
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    onSettings = { navController.navigate(Routes.SETTINGS) }
                )
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
