package com.jhoel.framepuzzle

import android.os.Bundle
import android.widget.TextView
import android.widget.ScrollView
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.jhoel.framepuzzle.core.designsystem.theme.FramePuzzleTheme
import com.jhoel.framepuzzle.core.storage.SettingsRepository
import com.jhoel.framepuzzle.core.storage.ThemeMode
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if Koin is started
        val koinStarted = try {
            org.koin.core.context.GlobalContext.getOrNull() != null
        } catch (e: Throwable) {
            false
        }

        if (!koinStarted) {
            // Koin failed - show error on plain Android view
            showPlainError("Koin no se inicializó correctamente.\n\nLa app no puede funcionar sin inyección de dependencias.")
            return
        }

        setContent {
            FramePuzzleContent()
        }
    }

    private fun showPlainError(message: String) {
        val scrollView = ScrollView(this)
        val textView = TextView(this)
        textView.text = message
        textView.setPadding(48, 48, 48, 48)
        textView.textSize = 16f
        scrollView.addView(textView)
        setContentView(scrollView)
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
        Surface(modifier = Modifier.fillMaxSize()) {
            com.jhoel.framepuzzle.navigation.FramePuzzleNavHost()
        }
    }
}

@Composable
private fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val sv = ScrollView(ctx)
                val tv = TextView(ctx)
                tv.text = message
                tv.setPadding(32, 32, 32, 32)
                tv.textSize = 14f
                sv.addView(tv)
                sv
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
