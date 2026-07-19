package com.jhoel.framepuzzle

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jhoel.framepuzzle.core.designsystem.theme.FramePuzzleTheme
import com.jhoel.framepuzzle.feature.settings.ThemeMode
import com.jhoel.framepuzzle.feature.settings.ui.SettingsViewModel
import com.jhoel.framepuzzle.navigation.FramePuzzleNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de FramePuzzle.
 *
 * Single-Activity architecture. La navegación vive en [FramePuzzleNavHost].
 *
 * El tema (claro/oscuro) se aplica según:
 *   1. Configuración del usuario (SettingsViewModel).
 *   2. Si es SYSTEM, sigue la configuración de Android.
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.value.toInt()),
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.value.toInt()),
        )
        setContent {
            FramePuzzleApp()
        }
    }
}

@Composable
private fun FramePuzzleApp(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val themeMode by settingsViewModel.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    FramePuzzleTheme(darkTheme = isDark) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            color = MaterialTheme.colorScheme.background,
        ) {
            FramePuzzleNavHost()
        }
    }
}
