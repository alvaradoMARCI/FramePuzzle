package com.jhoel.framepuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jhoel.framepuzzle.core.designsystem.theme.FramePuzzleTheme
import com.jhoel.framepuzzle.navigation.FramePuzzleNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * Actividad principal de FramePuzzle.
 *
 * Single-Activity architecture. La navegación vive en [FramePuzzleNavHost].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.value.toInt()),
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.value.toInt()),
        )
        setContent {
            FramePuzzleTheme(darkTheme = true) {
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
    }
}
