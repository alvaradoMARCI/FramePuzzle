package com.jhoel.framepuzzle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.core.utils.time.TimeUtils
import com.jhoel.framepuzzle.feature.library.domain.Memory

/**
 * Pantalla de detalle de un recuerdo (sección 9 + 21).
 *
 * Muestra:
 *  - Imagen editada (o la original si no se editó).
 *  - Título y fecha.
 *  - Botones: editar, armar puzzle, favorito, compartir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryDetailScreen(
    memoryId: String,
    onBack: () -> Unit,
    onPlayPuzzle: (memoryId: String) -> Unit,
    onEdit: (memoryId: String) -> Unit,
    viewModel: MemoryDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(memoryId) { viewModel.load(memoryId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val memory = state.memory

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(memory?.title ?: "Recuerdo", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (memory != null) {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (memory.favorite) Icons.Outlined.Favorite
                                else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (memory.favorite) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { padding ->
        if (memory == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (state.isLoading) {
                    Text("Cargando…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Text(
                        state.error ?: "Recuerdo no encontrado",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = FramePuzzleSpacing.lg, vertical = FramePuzzleSpacing.md),
            verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
        ) {
            // Imagen principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = memory.editedImagePath ?: memory.originalImagePath,
                    contentDescription = memory.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }

            // Metadata
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column(Modifier.padding(FramePuzzleSpacing.md)) {
                    Text(
                        text = memory.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Creado el ${TimeUtils.formatLong(memory.createdDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (memory.editedImagePath != null)
                            "Imagen editada (el original está preservado)"
                        else "Imagen original",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Acción principal: armar puzzle
            Button(
                onClick = { onPlayPuzzle(memory.id) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Outlined.Extension, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Armar puzzle")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
            ) {
                OutlinedButton(
                    onClick = { onEdit(memory.id) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(6.dp))
                    Text("Editar")
                }
                OutlinedButton(
                    onClick = { /* share: pendiente */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(6.dp))
                    Text("Compartir")
                }
            }
        }
    }
}
