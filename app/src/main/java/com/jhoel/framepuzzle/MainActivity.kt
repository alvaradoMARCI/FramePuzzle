package com.jhoel.framepuzzle

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.fragment.app.FragmentActivity
import com.jhoel.framepuzzle.core.designsystem.theme.FramePuzzleTheme
import com.jhoel.framepuzzle.core.storage.SettingsRepository
import com.jhoel.framepuzzle.core.storage.ThemeMode
import com.jhoel.framepuzzle.navigation.FramePuzzleNavHost
import org.koin.compose.koinInject

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FramePuzzleContent()
        }
    }
}

@Composable
private fun FramePuzzleContent() {
    val settingsRepository: SettingsRepository = koinInject()
    val themeMode by settingsRepository.themeMode.collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    FramePuzzleTheme(darkTheme = isDark) {
        com.jhoel.framepuzzle.navigation.FramePuzzleNavHost()
    }
}
