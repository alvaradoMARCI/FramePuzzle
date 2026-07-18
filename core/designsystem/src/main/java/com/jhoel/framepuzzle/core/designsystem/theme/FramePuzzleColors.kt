package com.jhoel.framepuzzle.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Design System FramePuzzle v3 — Paleta premium.
 *
 * Principios:
 *  - Base minimalista: blanco, grises claros, gris grafito, negro suave.
 *  - Acentos modernos: azul, esmeralda, violeta.
 *  - Dorado reservado EXCLUSIVAMENTE para logros y elementos especiales.
 *  - Mucho espacio en blanco, alto contraste, sin colores cálidos dominantes.
 *  - Inspiración visual: Google Photos, Pixel Camera, Adobe Lightroom.
 */
object FramePuzzleColors {

    // -------------------------------------------------------------------------
    // Base neutrals (predominantes)
    // -------------------------------------------------------------------------
    val White = Color(0xFFFFFFFF)
    val GrayExtraLight = Color(0xFFF7F8FA)
    val GrayLight = Color(0xFFEFF1F5)
    val GrayMedium = Color(0xFFD5D9E0)
    val GrayDark = Color(0xFF6E7686)
    val Graphite = Color(0xFF3A4051)
    val BlackSoft = Color(0xFF1C1F2A)
    val BlackDeep = Color(0xFF0F1117)

    // -------------------------------------------------------------------------
    // Acentos modernos (uso controlado)
    // -------------------------------------------------------------------------
    val BlueModern = Color(0xFF3B82F6)
    val BlueBright = Color(0xFF60A5FA)
    val BlueDeep = Color(0xFF1E40AF)

    val Emerald = Color(0xFF10B981)
    val EmeraldBright = Color(0xFF34D399)
    val EmeraldDeep = Color(0xFF047857)

    val Violet = Color(0xFF8B5CF6)
    val VioletBright = Color(0xFFA78BFA)
    val VioletDeep = Color(0xFF6D28D9)

    // -------------------------------------------------------------------------
    // Dorado — solo para logros y elementos especiales
    // -------------------------------------------------------------------------
    val Gold = Color(0xFFD4A24E)
    val GoldBright = Color(0xFFE8C078)
    val GoldDeep = Color(0xFFB07F2C)

    // -------------------------------------------------------------------------
    // Estado
    // -------------------------------------------------------------------------
    val Error = Color(0xFFEF4444)
    val ErrorBright = Color(0xFFF87171)
    val Success = Color(0xFF22C55E)
    val Warning = Color(0xFFF59E0B)
    val Info = BlueModern

    // -------------------------------------------------------------------------
    // Modo claro (predeterminado en Android con tema del sistema claro)
    // -------------------------------------------------------------------------
    val LightBackground = White
    val LightSurface = White
    val LightSurfaceVariant = GrayExtraLight
    val LightSurfaceElevated = White
    val LightOnBackground = BlackSoft
    val LightOnSurface = BlackSoft
    val LightOnSurfaceVariant = GrayDark
    val LightOutline = GrayMedium
    val LightOutlineVariant = GrayLight

    // -------------------------------------------------------------------------
    // Modo oscuro (predeterminado en Android con tema del sistema oscuro)
    // -------------------------------------------------------------------------
    val DarkBackground = BlackDeep
    val DarkSurface = BlackSoft
    val DarkSurfaceVariant = Graphite
    val DarkSurfaceElevated = Color(0xFF252938)
    val DarkOnBackground = Color(0xFFF7F8FA)
    val DarkOnSurface = Color(0xFFF7F8FA)
    val DarkOnSurfaceVariant = Color(0xFFA0A8B8)
    val DarkOutline = Color(0xFF3A4051)
    val DarkOutlineVariant = Color(0xFF2A2F3D)
}
