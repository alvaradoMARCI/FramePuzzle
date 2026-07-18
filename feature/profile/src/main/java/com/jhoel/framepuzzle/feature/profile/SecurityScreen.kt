package com.jhoel.framepuzzle.feature.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Fingerprint
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.core.security.biometric.BiometricManagerHelper
import com.jhoel.framepuzzle.core.security.pin.PinManager

/**
 * Pantalla de Seguridad (sección 38).
 *
 * Permite configurar PIN, activar biometría y bloqueo de aplicación.
 *
 * FramePuzzle protege:
 *  - Acceso: PIN + biometría + bloqueo automático.
 *  - Datos: cifrado local (CryptoManager), archivos protegidos.
 *  - Acciones sensibles: confirmación antes de eliminar/exportar/restaurar/transferir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
    onBack: () -> Unit,
    viewModel: com.jhoel.framepuzzle.feature.profile.ui.SecurityViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    val pinManager = viewModel.pinManager
    val biometricHelper = viewModel.biometricHelper
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguridad", fontWeight = FontWeight.SemiBold) },
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
                Column(Modifier.padding(FramePuzzleSpacing.lg)) {
                    Text(
                        text = "Protección de acceso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(FramePuzzleSpacing.sm))
                    Text(
                        text = "Configura un PIN para bloquear FramePuzzle. El PIN se almacena cifrado con Android Keystore.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(FramePuzzleSpacing.md))
                    Text(
                        text = if (pinManager.isPinSet()) "PIN configurado ✓" else "Sin PIN configurado",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(FramePuzzleSpacing.lg)) {
                    Text(
                        text = "Biometría",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(FramePuzzleSpacing.sm))
                    Text(
                        text = "Desbloqueo rápido con huella o rostro. El PIN siempre funciona como respaldo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(FramePuzzleSpacing.lg)) {
                    Text(
                        text = "Cifrado local",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.height(FramePuzzleSpacing.sm))
                    Text(
                        text = "Recuerdos y respaldos se cifran con AES-256/GCM vía Android Keystore. Las claves nunca salen del dispositivo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * Pantalla de desbloqueo (lock screen) que se muestra al abrir la app
 * si el usuario ya configuró PIN.
 */
@Composable
fun PinLockScreen(
    pinManager: PinManager,
    onUnlocked: () -> Unit,
    onUseBiometric: (() -> Unit)? = null,
) {
    var entered by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(FramePuzzleSpacing.xl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Ingresa tu PIN",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(FramePuzzleSpacing.xl))

        Row(horizontalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.sm)) {
            repeat(4) { i ->
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(
                            if (i < entered.length) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                        ),
                )
            }
        }

        Spacer(Modifier.height(FramePuzzleSpacing.lg))
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(Modifier.height(FramePuzzleSpacing.xl))
        // Numpad
        val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "⌫")
        Column(verticalArrangement = Arrangement.spacedBy(FramePuzzleSpacing.sm)) {
            keys.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    row.forEach { key ->
                        if (key.isEmpty()) {
                            Box(modifier = Modifier.size(64.dp))
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .clickable {
                                        when (key) {
                                            "⌫" -> entered = entered.dropLast(1)
                                            else -> {
                                                if (entered.length < 4) {
                                                    entered += key
                                                    if (entered.length == 4) {
                                                        if (pinManager.verify(entered)) {
                                                            onUnlocked()
                                                        } else {
                                                            error = "PIN incorrecto"
                                                            entered = ""
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = key,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }
        }

        if (onUseBiometric != null) {
            Spacer(Modifier.height(FramePuzzleSpacing.md))
            OutlinedButton(onClick = onUseBiometric) {
                Icon(Icons.Outlined.Fingerprint, contentDescription = null)
                                Spacer(Modifier.height(4.dp))
                Text("Usar biometría")
            }
        }
    }
}
