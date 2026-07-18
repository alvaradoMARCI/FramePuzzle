package com.jhoel.framepuzzle.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Esquema oscuro premium de FramePuzzle (predeterminado).
 * Fondo azul oscuro casi negro, dorado como único acento.
 */
private val FramePuzzleDarkColorScheme = darkColorScheme(
    primary = FramePuzzleColors.Gold,
    onPrimary = Color(0xFF0B1220),
    primaryContainer = FramePuzzleColors.GoldDeep,
    onPrimaryContainer = FramePuzzleColors.GoldBright,
    secondary = FramePuzzleColors.GoldBright,
    onSecondary = Color(0xFF0B1220),
    tertiary = FramePuzzleColors.Gold,
    onTertiary = Color(0xFF0B1220),
    background = FramePuzzleColors.DarkBackground,
    onBackground = FramePuzzleColors.DarkOnBackground,
    surface = FramePuzzleColors.DarkSurface,
    onSurface = FramePuzzleColors.DarkOnSurface,
    surfaceVariant = FramePuzzleColors.DarkSurfaceVariant,
    onSurfaceVariant = FramePuzzleColors.DarkOnSurfaceVariant,
    outline = FramePuzzleColors.DarkOutline,
    error = FramePuzzleColors.Error,
    onError = Color.White,
)

/**
 * Esquema claro de FramePuzzle.
 */
private val FramePuzzleLightColorScheme = lightColorScheme(
    primary = FramePuzzleColors.GoldDeep,
    onPrimary = Color.White,
    primaryContainer = FramePuzzleColors.GoldBright,
    onPrimaryContainer = Color(0xFF0B1220),
    secondary = FramePuzzleColors.GoldDeep,
    onSecondary = Color.White,
    tertiary = FramePuzzleColors.GoldDeep,
    onTertiary = Color.White,
    background = FramePuzzleColors.LightBackground,
    onBackground = FramePuzzleColors.LightOnBackground,
    surface = FramePuzzleColors.LightSurface,
    onSurface = FramePuzzleColors.LightOnSurface,
    surfaceVariant = FramePuzzleColors.LightSurfaceVariant,
    onSurfaceVariant = FramePuzzleColors.LightOnSurfaceVariant,
    outline = FramePuzzleColors.LightOutline,
    error = FramePuzzleColors.Error,
    onError = Color.White,
)

/** CompositionLocal para exponer dimensión extra (gradientes dorados, etc.). */
val LocalFramePuzzleExtraColors = staticCompositionLocalOf { FramePuzzleExtraColors() }

/** Datos extendidos del tema (no cubiertos por Material3). */
data class FramePuzzleExtraColors(
    val goldGradientStart: Color = FramePuzzleColors.GoldBright,
    val goldGradientEnd: Color = FramePuzzleColors.GoldDeep,
)

/**
 * Tema FramePuzzle. Envuelve toda la UI.
 *
 * @param darkTheme Forzar modo oscuro. Por defecto sigue al sistema.
 * @param content Contenido Compose bajo el tema.
 */
@Composable
fun FramePuzzleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) FramePuzzleDarkColorScheme else FramePuzzleLightColorScheme
    val extra = FramePuzzleExtraColors()

    CompositionLocalProvider(LocalFramePuzzleExtraColors provides extra) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = FramePuzzleTypography,
            shapes = FramePuzzleShapes,
            content = content,
        )
    }
}
