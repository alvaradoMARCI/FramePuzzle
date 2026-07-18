package com.jhoel.framepuzzle.feature.puzzle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleGoldChip
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleLoading
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.core.utils.time.TimeUtils
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleBoard
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzleDifficulty
import com.jhoel.framepuzzle.feature.puzzle.domain.PuzzlePiece
import com.jhoel.framepuzzle.feature.puzzle.ui.PuzzleViewModel

/**
 * Pantalla Puzzle (sección 14-18).
 *
 * El usuario juega; al completar, se muestra animación especial (sección 18):
 * piezas se unen formando la imagen completa, manteniendo pequeñas
 * separaciones visuales para conservar la identidad de puzzle.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleScreen(
    memoryId: String,
    onCompleted: () -> Unit,
    viewModel: PuzzleViewModel = hiltViewModel(),
) {
    LaunchedEffect(memoryId) { viewModel.load(memoryId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(state.title, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = TimeUtils.formatPuzzleDuration(state.elapsedMillis) +
                                " · ${state.moves} movimientos",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                actions = {
                    FramePuzzleGoldChip(text = "${state.config.totalPieces} piezas")
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (state.isLoading || state.board == null) {
                FramePuzzleLoading()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(FramePuzzleSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
                ) {
                    DifficultySelector(state.config.difficulty, viewModel::setDifficulty)

                    PuzzleGrid(
                        board = state.board!!,
                        onSwap = viewModel::swapPieces,
                        modifier = Modifier.weight(1f),
                    )
                }

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
}

@Composable
private fun DifficultySelector(
    current: PuzzleDifficulty,
    onSelect: (PuzzleDifficulty) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.sm),
    ) {
        PuzzleDifficulty.entries.forEach { d ->
            Card(
                onClick = { onSelect(d) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (current == d) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface,
                ),
            ) {
                Text(
                    text = when (d) {
                        PuzzleDifficulty.EASY -> "Fácil"
                        PuzzleDifficulty.NORMAL -> "Normal"
                        PuzzleDifficulty.HARD -> "Difícil"
                        PuzzleDifficulty.CUSTOM -> "Custom"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = if (current == d) Color(0xFF1A1A1F) else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun PuzzleGrid(
    board: PuzzleBoard,
    onSwap: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridSize = board.gridSize
    var firstSelected by remember { mutableStateOf<Int?>(null) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridSize),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        gridItems(count = board.pieces.size) { slot ->
            val piece = board.pieces.firstOrNull { it.currentIndex == slot }
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        if (firstSelected == null) firstSelected = slot
                        else {
                            onSwap(firstSelected!!, slot)
                            firstSelected = null
                        }
                    },
            ) {
                if (piece?.bitmapPath != null) {
                    AsyncImage(
                        model = piece.bitmapPath,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
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
                text = "${moves} movimientos · ${TimeUtils.formatPuzzleDuration(timeMillis)}",
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

// Fin de PuzzleScreen.kt
