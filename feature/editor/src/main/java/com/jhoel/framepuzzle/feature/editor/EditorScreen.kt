package com.jhoel.framepuzzle.feature.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.Rotate90DegreesCw
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleLoading
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.feature.editor.domain.FramePuzzleFilter
import com.jhoel.framepuzzle.feature.editor.ui.EditorViewModel

/**
 * Pantalla Editor (sección 13).
 *
 * Regla: la edición es NO destructiva. El original nunca se modifica.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    memoryId: String,
    onDone: () -> Unit,
    viewModel: EditorViewModel = hiltViewModel(),
) {
    LaunchedEffect(memoryId) { viewModel.load(memoryId) }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) {
            viewModel.consumeSaved()
            onDone()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                actions = {
                    IconButton(onClick = viewModel::reset) { Text("↺") }
                    Button(
                        onClick = { viewModel.save(memoryId) },
                        enabled = !state.isSaving,
                        modifier = Modifier.padding(end = 8.dp),
                    ) { Text("Guardar") }
                },
            )
        },
    ) { padding ->
        if (state.isLoading) {
            FramePuzzleLoading(modifier = Modifier.padding(padding))
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(FramePuzzleSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.lg),
        ) {
            // Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = state.editedPath ?: state.originalPath,
                    contentDescription = state.title,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // Quick tools
            Row(horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md)) {
                FilledTonalIconButton(onClick = viewModel::rotate90) {
                    Icon(Icons.Outlined.Rotate90DegreesCw, contentDescription = "Rotar")
                }
                FilledTonalIconButton(onClick = { /* crop UI */ }) {
                    Icon(Icons.Outlined.Crop, contentDescription = "Recortar")
                }
            }

            // Filtros (sección 13: Filtros propios FramePuzzle)
            Text(
                text = "Filtros",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md)) {
                items(FramePuzzleFilter.entries) { filter ->
                    FilterChip(
                        filter = filter,
                        selected = state.filter == filter,
                        onClick = { viewModel.setFilter(filter) },
                    )
                }
            }

            // Sliders básicos
            SliderRow(
                label = "Brillo",
                value = state.adjustments.brightness,
                onValueChange = { v -> viewModel.updateAdjustments { it.copy(brightness = v) } },
            )
            SliderRow(
                label = "Contraste",
                value = state.adjustments.contrast,
                onValueChange = { v -> viewModel.updateAdjustments { it.copy(contrast = v) } },
            )
            SliderRow(
                label = "Saturación",
                value = state.adjustments.saturation,
                onValueChange = { v -> viewModel.updateAdjustments { it.copy(saturation = v) } },
            )
            SliderRow(
                label = "Temperatura",
                value = state.adjustments.temperature,
                onValueChange = { v -> viewModel.updateAdjustments { it.copy(temperature = v) } },
            )
            SliderRow(
                label = "Exposición",
                value = state.adjustments.exposure,
                onValueChange = { v -> viewModel.updateAdjustments { it.copy(exposure = v) } },
            )

            OutlinedButton(
                onClick = { viewModel.save(memoryId) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving,
            ) {
                Text(if (state.isSaving) "Guardando…" else "Guardar y continuar")
            }
        }
    }
}

@Composable
private fun FilterChip(
    filter: FramePuzzleFilter,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(width = 90.dp, height = 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = filter.display,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Color(0xFF1A1A1F) else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                String.format("%.2f", value),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = -1f..1f,
        )
    }
}
