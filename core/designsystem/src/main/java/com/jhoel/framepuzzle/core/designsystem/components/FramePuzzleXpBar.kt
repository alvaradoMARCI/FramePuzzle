package com.jhoel.framepuzzle.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jhoel.framepuzzle.core.designsystem.theme.LocalFramePuzzleExtraColors

/**
 * Indicador de XP con degradado dorado, usado en home y perfil.
 * Refleja el progreso del usuario dentro de FramePuzzle.
 */
@Composable
fun FramePuzzleXpBar(
    currentXp: Int,
    levelXp: Int,
    modifier: Modifier = Modifier,
) {
    val progress = if (levelXp <= 0) 0f else (currentXp.toFloat() / levelXp).coerceIn(0f, 1f)
    val extra = LocalFramePuzzleExtraColors.current

    Box(modifier = modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

/**
 * Pequeño chip dorado con texto. Ej: "Nivel 5".
 */
@Composable
fun FramePuzzleGoldChip(
    text: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
) {
    val extra = LocalFramePuzzleExtraColors.current
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(extra.goldGradientStart, extra.goldGradientEnd),
                ),
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color(0xFF1A1A1F),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
