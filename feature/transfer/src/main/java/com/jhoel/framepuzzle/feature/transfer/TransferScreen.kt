package com.jhoel.framepuzzle.feature.transfer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.QrCodeScanner
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.feature.transfer.data.TransferManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val transferManager: TransferManager,
) : ViewModel() {

    var qrBitmap by mutableStateOf<android.graphics.Bitmap?>(null)
        private set

    var status by mutableStateOf<String?>(null)
        private set

    fun generateQr(deviceName: String) {
        viewModelScope.launch {
            val token = transferManager.generateSessionToken(deviceName, dataSizeBytes = 0)
            val encoded = transferManager.encodeToken(token)
            val bmp = withContext(Dispatchers.Default) { transferManager.renderQrBitmap(encoded) }
            qrBitmap = bmp
            status = "Código generado. Escanéalo desde el otro teléfono."
        }
    }

    fun clearQr() {
        qrBitmap = null
        status = null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    onBack: () -> Unit,
    viewModel: TransferViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transferir recuerdos", fontWeight = FontWeight.SemiBold) },
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
                        "Generar código QR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Muestra este código en el teléfono antiguo y escanéalo desde el nuevo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(
                        onClick = { viewModel.generateQr(android.os.Build.MODEL) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(Icons.Outlined.QrCode, contentDescription = null)
                        Spacer(Modifier.height(4.dp))
                        Text("Generar código")
                    }
                }
            }

            viewModel.qrBitmap?.let { bmp ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        Modifier.padding(FramePuzzleSpacing.lg),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "QR de transferencia",
                            modifier = Modifier
                                .size(280.dp)
                                .background(MaterialTheme.colorScheme.surface),
                        )
                    }
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(FramePuzzleSpacing.lg), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Recibir en este teléfono",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Escanea el código QR generado en el teléfono antiguo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedButton(onClick = { /* launch scanner */ }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Outlined.QrCodeScanner, contentDescription = null)
                        Spacer(Modifier.height(4.dp))
                        Text("Escanear código")
                    }
                }
            }

            viewModel.status?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
