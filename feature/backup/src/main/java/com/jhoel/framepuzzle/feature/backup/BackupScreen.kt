package com.jhoel.framepuzzle.feature.backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.jhoel.framepuzzle.core.designsystem.components.FramePuzzleEmptyState
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing

/**
 * Pantalla de Respaldo (sección 36).
 *
 * Versión actual:placeholder informativo. La funcionalidad de crear/restaurar
 * respaldos estará disponible cuando se complete el picker SAF + integración
 * con el BackupManager. No se muestran botones que no funcionan.
 *
 * Esta pantalla solo comunica el estado al usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(
    onBack: () -> Unit,
) {
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
            verticalArrangement = Arrangement.Center,
        ) {
            FramePuzzleEmptyState(
                title = "Respaldo disponible próximamente",
                message = "Esta función está en desarrollo. Tu información " +
                    "permanece segura en el dispositivo mientras tanto.",
            )
        }
    }
}
