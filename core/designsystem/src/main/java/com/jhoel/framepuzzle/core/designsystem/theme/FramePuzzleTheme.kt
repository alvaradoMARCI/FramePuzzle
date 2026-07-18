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
 * Esquema oscuro FramePuzzle (sigue Android system dark).
 * Base: negro suave / grafito. Acento: azul moderno. Dorado NO aparece
 * como color del esquema Material (reservado para logros).
 */
private val FramePuzzleDarkColorScheme = darkColorScheme(
    primary = FramePuzzleColors.BlueBright,
    onPrimary = Color(0xFF0F1117),
    primaryContainer = FramePuzzleColors.BlueDeep,
    onPrimaryContainer = FramePuzzleColors.BlueBright,
    secondary = FramePuzzleColors.VioletBright,
    onSecondary = Color(0xFF0F1117),
    secondaryContainer = FramePuzzleColors.VioletDeep,
    onSecondaryContainer = FramePuzzleColors.VioletBright,
    tertiary = FramePuzzleColors.EmeraldBright,
    onTertiary = Color(0xFF0F1117),
    tertiaryContainer = FramePuzzleColors.EmeraldDeep,
    onTertiaryContainer = FramePuzzleColors.EmeraldBright,
    background = FramePuzzleColors.DarkBackground,
    onBackground = FramePuzzleColors.DarkOnBackground,
    surface = FramePuzzleColors.DarkSurface,
    onSurface = FramePuzzleColors.DarkOnSurface,
    surfaceVariant = FramePuzzleColors.DarkSurfaceVariant,
    onSurfaceVariant = FramePuzzleColors.DarkOnSurfaceVariant,
    surfaceTint = FramePuzzleColors.BlueBright,
    outline = FramePuzzleColors.DarkOutline,
    outlineVariant = FramePuzzleColors.DarkOutlineVariant,
    error = FramePuzzleColors.Error,
    onError = Color.White,
    errorContainer = FramePuzzleColors.Error,
    onErrorContainer = Color.White,
)

/**
 * Esquema claro FramePuzzle (predeterminado si Android está en claro).
 * Base: blanco puro con grises muy claros. Acento: azul moderno.
 */
private val FramePuzzleLightColorScheme = lightColorScheme(
    primary = FramePuzzleColors.BlueModern,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = FramePuzzleColors.BlueDeep,
    secondary = FramePuzzleColors.Violet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = FramePuzzleColors.VioletDeep,
    tertiary = FramePuzzleColors.Emerald,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = FramePuzzleColors.EmeraldDeep,
    background = FramePuzzleColors.LightBackground,
    onBackground = FramePuzzleColors.LightOnBackground,
    surface = FramePuzzleColors.LightSurface,
    onSurface = FramePuzzleColors.LightOnSurface,
    surfaceVariant = FramePuzzleColors.LightSurfaceVariant,
    onSurfaceVariant = FramePuzzleColors.LightOnSurfaceVariant,
    surfaceTint = FramePuzzleColors.BlueModern,
    outline = FramePuzzleColors.LightOutline,
    outlineVariant = FramePuzzleColors.LightOutlineVariant,
    error = FramePuzzleColors.Error,
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = FramePuzzleColors.Error,
)

/** CompositionLocal para colores extendidos (gradientes, dorado para logros). */
val LocalFramePuzzleExtraColors = staticCompositionLocalOf { FramePuzzleExtraColors() }

/**
 * Datos extendidos del tema (no cubiertos por Material3).
 */
data class FramePuzzleExtraColors(
    // Dorado reservado para logros / progreso / elementos especiales.
    val gold: Color = FramePuzzleColors.Gold,
    val goldBright: Color = FramePuzzleColors.GoldBright,
    val goldDeep: Color = FramePuzzleColors.GoldDeep,
    val goldGradientStart: Color = FramePuzzleColors.GoldBright,
    val goldGradientEnd: Color = FramePuzzleColors.GoldDeep,
    // Acentos
    val blueModern: Color = FramePuzzleColors.BlueModern,
    val emerald: Color = FramePuzzleColors.Emerald,
    val violet: Color = FramePuzzleColors.Violet,
    // Estado
    val success: Color = FramePuzzleColors.Success,
    val warning: Color = FramePuzzleColors.Warning,
)

/**
 * Tema FramePuzzle.
 *
 * @param darkTheme ¿Modo oscuro? Si es null, sigue al sistema.
 * @param dynamicColor Si true, usa colores dinámicos de Android 12+.
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
