package com.jhoel.framepuzzle.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = FramePuzzleColors.BlueBright,
    onPrimary = Color(0xFF0F1117),
    secondary = FramePuzzleColors.Gold,
    onSecondary = Color(0xFF0F1117),
    background = FramePuzzleColors.DarkBackground,
    onBackground = FramePuzzleColors.DarkOnBackground,
    surface = FramePuzzleColors.DarkSurface,
    onSurface = FramePuzzleColors.DarkOnSurface,
    surfaceVariant = FramePuzzleColors.DarkSurfaceVariant,
    onSurfaceVariant = FramePuzzleColors.DarkOnSurfaceVariant,
    outline = FramePuzzleColors.DarkOutline,
    error = FramePuzzleColors.Error,
)

private val LightScheme = lightColorScheme(
    primary = FramePuzzleColors.BlueModern,
    onPrimary = Color.White,
    secondary = FramePuzzleColors.Gold,
    onSecondary = Color.White,
    background = FramePuzzleColors.LightBackground,
    onBackground = FramePuzzleColors.LightOnBackground,
    surface = FramePuzzleColors.LightSurface,
    onSurface = FramePuzzleColors.LightOnSurface,
    surfaceVariant = FramePuzzleColors.LightSurfaceVariant,
    onSurfaceVariant = FramePuzzleColors.LightOnSurfaceVariant,
    outline = FramePuzzleColors.LightOutline,
    error = FramePuzzleColors.Error,
)

@Composable
fun FramePuzzleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = FramePuzzleTypography,
        shapes = FramePuzzleShapes,
        content = content,
    )
}
