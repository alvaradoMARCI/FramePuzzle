package com.jhoel.framepuzzle.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appSettings: AppSettings,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        appSettings.isDarkTheme,
        appSettings.hapticsEnabled,
        appSettings.soundEnabled,
        appSettings.autoLockMinutes,
    ) { dark, haptics, sound, autoLock ->
        SettingsUiState(dark, haptics, sound, autoLock)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsUiState())

    fun setDarkTheme(value: Boolean) = viewModelScope.launch { appSettings.setDarkTheme(value) }
    fun setHaptics(value: Boolean) = viewModelScope.launch { appSettings.setHapticsEnabled(value) }
    fun setSound(value: Boolean) = viewModelScope.launch { appSettings.setSoundEnabled(value) }
}

data class SettingsUiState(
    val darkTheme: Boolean = true,
    val haptics: Boolean = true,
    val sound: Boolean = true,
    val autoLockMinutes: Int = 5,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(FramePuzzleSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.md),
        ) {
            SettingRow(
                icon = Icons.Outlined.Lock,
                title = "Tema oscuro",
                subtitle = "Mejor para visualizar recuerdos",
                checked = state.darkTheme,
                onCheckedChange = viewModel::setDarkTheme,
            )
            SettingRow(
                icon = Icons.Outlined.Vibration,
                title = "Vibración háptica",
                subtitle = "Al completar puzzles",
                checked = state.haptics,
                onCheckedChange = viewModel::setHaptics,
            )
            SettingRow(
                icon = Icons.Outlined.VolumeUp,
                title = "Sonido",
                subtitle = "Efectos de celebración",
                checked = state.sound,
                onCheckedChange = viewModel::setSound,
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FramePuzzleSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            androidx.compose.foundation.layout.Spacer(Modifier.size(FramePuzzleSpacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}
