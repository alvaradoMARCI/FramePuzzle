package com.jhoel.framepuzzle.core.designsystem.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jhoel.framepuzzle.core.designsystem.theme.LocalFramePuzzleExtraColors

/**
 * Pantalla de carga inicial. Refleja la identidad FramePuzzle:
 * dorado + animación sutil. Sin dependencias externas.
 */
@Composable
fun FramePuzzleLoading(modifier: Modifier = Modifier, label: String = "FramePuzzle") {
    val extra = LocalFramePuzzleExtraColors.current
    val transition = rememberInfiniteTransition(label = "fp_loading")
    val scale by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "fp_loading_scale",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(extra.goldGradientStart, extra.goldGradientEnd),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "F",
                color = Color(0xFF1A1A1F),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

/**
 * Pieza de puzzle decorativa: marco con pieza dorada.
 * Usada en empty states, encabezados, etc.
 */
@Composable
fun FramePuzzleLogoBadge(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 48.dp,
) {
    val extra = LocalFramePuzzleExtraColors.current
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(extra.goldGradientStart, extra.goldGradientEnd),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "F",
            color = Color(0xFF1A1A1F),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}
