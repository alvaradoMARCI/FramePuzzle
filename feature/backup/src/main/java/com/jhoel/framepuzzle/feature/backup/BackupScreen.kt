package com.jhoel.framepuzzle.feature.backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Restore
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.feature.backup.data.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val backupManager: BackupManager,
) : ViewModel() {

    var status by androidx.compose.runtime.mutableStateOf<String?>(null)
        private set

    fun createBackup() {
        viewModelScope.launch {
            try {
                val file = withContext(Dispatchers.IO) { backupManager.create() }
                status = "Respaldo creado: ${file.name}"
            } catch (t: Throwable) {
                status = "Error: ${t.message}"
            }
        }
    }

    fun restoreBackup(path: String) {
        viewModelScope.launch {
            try {
                val ok = withContext(Dispatchers.IO) { backupManager.restore(path) }
                status = if (ok) "Respaldo restaurado correctamente" else "Archivo inválido"
            } catch (t: Throwable) {
                status = "Error: ${t.message}"
            }
        }
    }

    fun clearStatus() { status = null }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    onBack: () -> Unit,
    viewModel: BackupViewModel = hiltViewModel(),
) {
    val status by viewModel::status

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Respaldo", fontWeight = FontWeight.SemiBold) },
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
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(FramePuzzleSpacing.lg), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Crear respaldo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Genera un archivo .fpbackup cifrado con todos tus recuerdos, álbumes, logros y progreso.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(onClick = viewModel::createBackup, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.Backup, contentDescription = null)
                        Spacer(Modifier.height(4.dp))
                        Text("Crear respaldo")
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(FramePuzzleSpacing.lg), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Restaurar respaldo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Restaura un archivo .fpbackup previamente creado. Esta acción reemplaza tus datos actuales.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedButton(onClick = { /* picker SAF */ }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.Restore, contentDescription = null)
                        Spacer(Modifier.height(4.dp))
                        Text("Elegir archivo .fpbackup")
                    }
                }
            }

            status?.let {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(FramePuzzleSpacing.lg),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}
