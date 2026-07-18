package com.jhoel.framepuzzle.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores FramePuzzle.
 *
 * Principios visuales del producto:
 *  - Premium, elegante, minimalista, moderno.
 *  - Dorado solo como acento de progreso / logro / detalles importantes.
 *  - Base limpia: blanco, negro, gris grafito y azul oscuro.
 *  - Modo oscuro como predeterminado (mayor emoción visual sobre fotografías).
 */
object FramePuzzleColors {

    // Acento dorado (SOLO para detalles importantes: progreso, logros, primary)
    val Gold = Color(0xFFE8B86C)
    val GoldBright = Color(0xFFF5CE8A)
    val GoldDeep = Color(0xFFB8893E)

    // Modo oscuro premium (predeterminado)
    // Azul oscuro casi negro, no marrón
    val DarkBackground = Color(0xFF0B1220)       // azul oscuro profundo
    val DarkSurface = Color(0xFF121A2C)          // surface azul oscuro
    val DarkSurfaceVariant = Color(0xFF1B2438)   // variant más claro
    val DarkOnBackground = Color(0xFFF5F7FA)     // texto principal casi blanco
    val DarkOnSurface = Color(0xFFF5F7FA)
    val DarkOnSurfaceVariant = Color(0xFFA5ADC0) // texto secundario gris
    val DarkOutline = Color(0xFF2B3550)

    // Modo claro
    val LightBackground = Color(0xFFFAFBFC)      // blanco roto
    val LightSurface = Color(0xFFFFFFFF)
    val LightSurfaceVariant = Color(0xFFEFF1F5)
    val LightOnBackground = Color(0xFF0B1220)
    val LightOnSurface = Color(0xFF0B1220)
    val LightOnSurfaceVariant = Color(0xFF5A6378)
    val LightOutline = Color(0xFFE0E4EC)

    // Estado
    val Error = Color(0xFFFF5252)
    val Success = Color(0xFF3DD68C)
    val Warning = Color(0xFFFFC107)
    val Info = Color(0xFF4A90E2)
}
