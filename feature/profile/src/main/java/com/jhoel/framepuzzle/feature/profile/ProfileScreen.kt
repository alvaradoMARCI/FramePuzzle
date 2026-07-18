package com.jhoel.framepuzzle.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleGoldChip
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleXpBar
import com.jhoel.framepuzzle.core.designsystem.theme.LocalFramePuzzleExtraColors
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.feature.profile.ui.ProfileViewModel

/**
 * Pantalla Perfil (sección 7 + 20).
 *
 * Contiene:
 *  - Avatar.
 *  - Logros.
 *  - Estadísticas.
 *  - Seguridad.
 *  - Configuración.
 */
@Composable
fun ProfileScreen(
    onSettings: () -> Unit,
    onSecurity: () -> Unit,
    onBackup: () -> Unit,
    onTransfer: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = FramePuzzleSpacing.lg, vertical = FramePuzzleSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.lg),
    ) {
        Header(state)
        StatsGrid(state)
        AchievementsList(state)
        ActionsList(onSettings, onSecurity, onBackup, onTransfer)
    }
}

@Composable
private fun Header(state: ProfileUiState) {
    val extra = LocalFramePuzzleExtraColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FramePuzzleSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.sm),
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(extra.goldGradientStart, extra.goldGradientEnd),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = state.userName.firstOrNull()?.uppercase() ?: "F",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1F),
                )
            }
            Text(
                text = state.userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            FramePuzzleGoldChip(text = "Nivel ${state.level}")
            Spacer(Modifier.height(FramePuzzleSpacing.sm))
            Text(
                text = "XP ${state.currentXp} / ${state.requiredXp}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            FramePuzzleXpBar(
                currentXp = state.currentXp,
                levelXp = state.requiredXp,
            )
        }
    }
}

@Composable
private fun StatsGrid(state: ProfileUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
    ) {
        StatCard("Recuerdos", state.totalMemories.toString(), Modifier.weight(1f))
        StatCard("Puzzles", state.totalPuzzlesSolved.toString(), Modifier.weight(1f))
        StatCard("Logros", state.achievements.count { it.unlocked }.toString(), Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
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

@Composable
private fun AchievementsList(state: ProfileUiState) {
    Text(
        text = "Logros",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground,
    )
    state.achievements.take(5).forEach { a ->
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (a.unlocked) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(FramePuzzleSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (a.unlocked) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface,
                        ),
                )
                Spacer(Modifier.size(FramePuzzleSpacing.md))
                Column {
                    Text(
                        text = a.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = a.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionsList(
    onSettings: () -> Unit,
    onSecurity: () -> Unit,
    onBackup: () -> Unit,
    onTransfer: () -> Unit,
) {
    ActionRow(Icons.Outlined.Settings, "Configuración", onSettings)
    ActionRow(Icons.Outlined.Lock, "Seguridad y PIN", onSecurity)
    ActionRow(Icons.Outlined.Backup, "Respaldo", onBackup)
    ActionRow(Icons.Outlined.QrCodeScanner, "Transferir entre teléfonos", onTransfer)
}

@Composable
private fun ActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FramePuzzleSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.size(FramePuzzleSpacing.md))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
