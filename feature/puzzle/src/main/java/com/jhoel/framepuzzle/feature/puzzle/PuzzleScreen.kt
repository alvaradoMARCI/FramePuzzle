package com.jhoel.framepuzzle.feature.puzzle

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleLoading
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.core.utils.time.TimeUtils
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleDifficulty
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleType
import com.jhoel.framepuzzle.feature.puzzle.engine.PuzzleRenderer
import com.jhoel.framepuzzle.feature.puzzle.ui.PuzzleViewModel

/**
 * Pantalla Puzzle (sección 14-18).
 *
 * Render premium:
 *  - Piezas con forma real (tabs/blanks) via PuzzleRenderer.
 *  - Sombras y profundidad.
 *  - Animación al pulsar (scale 0.95).
 *  - Animación de celebración al completar.
 */
@Composable
fun PuzzleScreen(
    memoryId: String,
    onCompleted: () -> Unit,
    viewModel: PuzzleViewModel = hiltViewModel(),
) {
    LaunchedEffect(memoryId) { viewModel.load(memoryId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onCompleted) {
                    Icon(
                        Icons.Outlined.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                    Text(
                        text = state.title.ifBlank { "Puzzle" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "${TimeUtils.formatPuzzleDuration(state.elapsedMillis)} · ${state.moves} mov",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { viewModel.load(memoryId) }) {
                    Icon(
                        Icons.Outlined.Refresh,
                        contentDescription = "Reiniciar",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            if (state.isLoading || state.board == null) {
                FramePuzzleLoading()
                return@Box
            }

            val board = state.board!!
            // Render del tablero (operación pesada) en produceState → background.
            val renderedBitmap by produceState<Bitmap?>(initialValue = null, board) {
                value = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                    PuzzleRenderer.renderBoard(board, gapPx = 3, cornerRadius = 8f)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(FramePuzzleSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
            ) {
                // Selector de tipo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.sm),
                ) {
                    PuzzleType.entries.forEach { type ->
                        val isSelected = state.config.type == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                )
                                .clickable { viewModel.setType(type) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (type == PuzzleType.CLASSIC) "Clásico" else "Deslizante",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }

                // Selector de dificultad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.sm),
                ) {
                    PuzzleDifficulty.entries.forEach { d ->
                        val isSelected = state.config.difficulty == d
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                )
                                .clickable { viewModel.setDifficulty(d) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = when (d) {
                                    PuzzleDifficulty.EASY -> "Fácil"
                                    PuzzleDifficulty.NORMAL -> "Normal"
                                    PuzzleDifficulty.HARD -> "Difícil"
                                    PuzzleDifficulty.CUSTOM -> "Custom"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }

                // Tablero renderizado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .aspectRatio(1f)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(20.dp),
                            clip = false,
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    val bmp = renderedBitmap
                    if (bmp != null) {
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "Tablero",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                        )
                        // Overlay de detección de taps por celda.
                        TapGridOverlay(
                            gridSize = board.gridSize,
                            isSliding = state.config.type == PuzzleType.SLIDING,
                            onTapSlot = { slot ->
                                if (state.config.type == PuzzleType.SLIDING) {
                                    viewModel.slidePiece(slot)
                                }
                            },
                        )
                    } else {
                        FramePuzzleLoading()
                    }
                }

                // Indicador de piezas totales
                Text(
                    text = "${board.pieces.size} piezas · ${
                        if (state.config.type == PuzzleType.SLIDING)
                            "Desliza las piezas hacia el hueco vacío"
                        else "Toca dos piezas para intercambiarlas"
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }

            // Animación al completar
            AnimatedVisibility(
                visible = state.showCompletionAnimation,
                enter = fadeIn() + scaleIn(initialScale = 0.7f),
            ) {
                PuzzleCompletionOverlay(
                    title = state.title,
                    moves = state.moves,
                    timeMillis = state.elapsedMillis,
                    onContinue = {
                        viewModel.consumeCompletion()
                        onCompleted()
                    },
                )
            }
        }
    }
}

@Composable
private fun TapGridOverlay(
    gridSize: Int,
    isSliding: Boolean,
    onTapSlot: (Int) -> Unit,
) {
    // Grid invisible de gridSize x gridSize que captura taps por celda.
    val interaction = remember { MutableInteractionSource() }
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            for (row in 0 until gridSize) {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                ) {
                    for (col in 0 until gridSize) {
                        val slot = row * gridSize + col
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = interaction,
                                    indication = null,
                                ) {
                                    onTapSlot(slot)
                                },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PuzzleCompletionOverlay(
    title: String,
    moves: Int,
    timeMillis: Long,
    onContinue: () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "celebration")
    val scale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "celebrationScale",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xCC000000), Color(0xE6000000)),
                ),
            )
            .clickable(onClick = onContinue),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(FramePuzzleSpacing.xxl),
        ) {
            Icon(
                imageVector = Icons.Outlined.EmojiEvents,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(96.dp)
                    .scale(scale),
            )
            Text(
                text = "¡Recuerdo armado!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = FramePuzzleSpacing.lg),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
            )
            Text(
                text = "$moves movimientos · ${TimeUtils.formatPuzzleDuration(timeMillis)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = FramePuzzleSpacing.sm),
            )
            Text(
                text = "Toca para continuar",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = FramePuzzleSpacing.lg),
            )
        }
    }
}
