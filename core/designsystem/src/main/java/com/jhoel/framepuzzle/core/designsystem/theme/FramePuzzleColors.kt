package com.jhoel.framepuzzle.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores FramePuzzle.
 *
 * Principios visuales del producto:
 *  - Premium, elegante, minimalista, emocional.
 *  - Dorado como acento de progreso / logro.
 *  - Modo oscuro como predeterminado (mayor emoción visual sobre fotografías).
 */
object FramePuzzleColors {

    // Gold (acento principal: progreso, logro, identidad)
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFE8C766)
    val GoldDark = Color(0xFFB8902A)
    val GoldGradientStart = Color(0xFFE8C766)
    val GoldGradientEnd = Color(0xFFB8902A)

    // Dark theme (predeterminado)
    val DarkBackground = Color(0xFF0E0E12)
    val DarkSurface = Color(0xFF181821)
    val DarkSurfaceVariant = Color(0xFF22222E)
    val DarkOnBackground = Color(0xFFF5F5F7)
    val DarkOnSurface = Color(0xFFF5F5F7)
    val DarkOnSurfaceVariant = Color(0xFFA0A0AD)
    val DarkOutline = Color(0xFF33333F)

    // Light theme
    val LightBackground = Color(0xFFFAFAFB)
    val LightSurface = Color(0xFFFFFFFF)
    val LightSurfaceVariant = Color(0xFFF0F0F4)
    val LightOnBackground = Color(0xFF1A1A1F)
    val LightOnSurface = Color(0xFF1A1A1F)
    val LightOnSurfaceVariant = Color(0xFF5A5A66)
    val LightOutline = Color(0xFFE0E0E6)

    // Estado
    val Error = Color(0xFFFF5252)
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFFC107)
    val Info = Color(0xFF2196F3)
}
