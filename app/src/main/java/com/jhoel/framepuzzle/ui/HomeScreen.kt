package com.jhoel.framepuzzle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jhoel.framepuzzle.R
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleGoldChip
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleLogoBadge
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleXpBar
import com.jhoel.framepuzzle.core.designsystem.theme.LocalFramePuzzleExtraColors
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.feature.profile.HomeUiState

/**
 * Pantalla Inicio (sección 7).
 *
 * Debe mostrar:
 *  - Avatar del usuario.
 *  - Nivel.
 *  - Experiencia acumulada.
 *  - Recuerdo destacado.
 *  - Accesos rápidos.
 */
@Composable
fun HomeScreen(
    onQuickCreate: () -> Unit,
    onMemoryClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onQuickCreate = onQuickCreate,
        onMemoryClick = onMemoryClick,
    )
}

@Composable
private fun HomeContent(
    state: HomeUiState,
    onQuickCreate: () -> Unit,
    onMemoryClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FramePuzzleSpacing.lg, vertical = FramePuzzleSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.lg),
    ) {
        Header(state)

        FeaturedMemory(state, onMemoryClick)

        QuickActions(onQuickCreate)

        StatsRow(state)
    }
}

@Composable
private fun Header(state: HomeUiState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        FramePuzzleLogoBadge(size = 48.dp)
        Spacer(Modifier.width(FramePuzzleSpacing.md))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (state.isLogged) {
            FramePuzzleGoldChip(text = "Nivel ${state.level}")
        }
    }

    if (state.isLogged) {
        Spacer(Modifier.height(FramePuzzleSpacing.sm))
        Column {
            Text(
                text = "XP ${state.currentXp} / ${state.requiredXp}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(FramePuzzleSpacing.xs))
            FramePuzzleXpBar(
                currentXp = state.currentXp,
                levelXp = state.requiredXp,
            )
        }
    }
}

@Composable
private fun FeaturedMemory(state: HomeUiState, onMemoryClick: (String) -> Unit) {
    val extra = LocalFramePuzzleExtraColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FramePuzzleSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
        ) {
            Text(
                text = stringResource(R.string.home_featured_memory),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            val featured = state.featuredMemory
            if (featured != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = featured.editedImagePath ?: featured.originalImagePath,
                        contentDescription = featured.title,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Text(
                    text = featured.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(extra.goldGradientStart, extra.goldGradientEnd),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Aún no creaste recuerdos.\nToca “Crear” para empezar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1A1A1F),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActions(onQuickCreate: () -> Unit) {
    Text(
        text = stringResource(R.string.home_quick_actions),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
    ) {
        items(quickActions()) { action ->
            QuickActionCard(
                icon = action.icon,
                label = action.label,
                onClick = if (action.id == "create") onQuickCreate else { {} },
            )
        }
    }
}

private data class QuickAction(val id: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private fun quickActions() = listOf(
    QuickAction("create", "Nuevo recuerdo", Icons.Outlined.AddAPhoto),
    QuickAction("auto", "Automagic", Icons.Outlined.AutoAwesome),
    QuickAction("collections", "Álbumes", Icons.Outlined.Collections),
    QuickAction("timer", "Línea temporal", Icons.Outlined.Timer),
)

@Composable
private fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(width = 140.dp, height = 90.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(FramePuzzleSpacing.md),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(FramePuzzleSpacing.xs))
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun StatsRow(state: HomeUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
    ) {
        StatBox("Recuerdos", state.totalMemories.toString(), Modifier.weight(1f))
        StatBox("Puzzles", state.totalPuzzlesCompleted.toString(), Modifier.weight(1f))
        StatBox("Logros", state.totalAchievements.toString(), Modifier.weight(1f))
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FramePuzzleSpacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
