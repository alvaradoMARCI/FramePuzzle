package com.jhoel.framepuzzle.core.designsystem.tokens

import androidx.compose.ui.unit.dp

/**
 * Tokens de espaciado FramePuzzle.
 *
 * Sistema consistente: 4pt base.
 * Usar SIEMPRE estos tokens, nunca valores sueltos.
 */
object FramePuzzleSpacing {
    val xxxs = 2.dp
    val xxs = 4.dp
    val xs = 8.dp
    val sm = 12.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 48.dp
    val huge = 64.dp

    /** Padding horizontal estándar de pantallas. */
    val screenHorizontal = md

    /** Padding vertical estándar de pantallas. */
    val screenVertical = lg
}

/**
 * Tokens de tamaño para componentes.
 */
object FramePuzzleSizes {
    // Touch targets (mínimo 48dp Material)
    val touchTargetMin = 48.dp
    val buttonHeight = 52.dp
    val buttonHeightCompact = 44.dp

    // Avatares
    val avatarExtraSmall = 24.dp
    val avatarSmall = 36.dp
    val avatarMedium = 56.dp
    val avatarLarge = 96.dp
    val avatarExtraLarge = 128.dp

    // Cards y superficies
    val cardCorner = 16.dp
    val cardCornerLarge = 24.dp

    // Imágenes
    val memoryThumbSmall = 80.dp
    val memoryThumb = 120.dp
    val memoryFull = 280.dp

    // Bottom bar
    val bottomBarHeight = 80.dp
    val bottomBarIconSize = 24.dp

    // Top bar
    val topBarHeight = 64.dp

    // Pieza de puzzle
    val puzzleGap = 2.dp
    val puzzlePieceCorner = 6.dp
    val puzzlePieceElevation = 6.dp
}

/**
 * Tokens de duración de animación (ms).
 */
object FramePuzzleDurations {
    const val Instant = 50
    const val Quick = 150
    const val Normal = 300
    const val Slow = 500
    const val Celebration = 1200
    const val PieceMove = 200
}

/**
 * Tokens de elevación (sombras).
 */
object FramePuzzleElevation {
    val none = 0.dp
    val low = 1.dp
    val medium = 3.dp
    val high = 6.dp
    val higher = 8.dp
    val highest = 12.dp
}
