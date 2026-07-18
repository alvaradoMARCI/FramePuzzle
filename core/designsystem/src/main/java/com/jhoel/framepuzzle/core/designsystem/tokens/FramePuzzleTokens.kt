package com.jhoel.framepuzzle.core.designsystem.tokens

import androidx.compose.ui.unit.dp

/**
 * Tokens de espaciado. Mantienen consistencia en toda la UI.
 */
object FramePuzzleSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
    val xxxl = 48.dp
}

/**
 * Tokens de tamaño para componentes comunes.
 */
object FramePuzzleSizes {
    val touchTarget = 48.dp
    val avatarSmall = 36.dp
    val avatarMedium = 56.dp
    val avatarLarge = 96.dp
    val cardCorner = 16.dp
    val memoryThumb = 96.dp
    val memoryFull = 240.dp
}

/**
 * Tokens de duración de animación.
 */
object FramePuzzleDurations {
    const val Instant = 100
    const val Quick = 200
    const val Normal = 350
    const val Slow = 600
    const val Celebration = 1200
}
