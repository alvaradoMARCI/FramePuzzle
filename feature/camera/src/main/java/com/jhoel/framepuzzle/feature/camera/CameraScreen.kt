package com.jhoel.framepuzzle.feature.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.jhoel.framepuzzle.core.domain.repository.MemoryRepository
import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import org.koin.compose.koinInject

@Composable
fun CameraScreen(
    onMemoryCreated: () -> Unit = {},
) {
    val context = LocalContext.current
    val memoryRepository: MemoryRepository = koinInject()
    val storageManager: LocalStorageManager = koinInject()

    var pendingPath by remember { mutableStateOf<String?>(null) }
    var isCreating by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val temp = java.io.File(context.cacheDir, "import_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                java.io.FileOutputStream(temp).use { output -> input.copyTo(output) }
            }
            pendingPath = temp.absolutePath
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        when {
            pendingPath != null -> {
                ConfirmScreen(
                    imagePath = pendingPath!!,
                    isCreating = isCreating,
                    onConfirm = {
                        isCreating = true
                        val path = pendingPath!!
                        scope.launch {
                            try {
                                val memoryId = java.util.UUID.randomUUID().toString()
                                val target = java.io.File(storageManager.originalDir, "$memoryId.jpg")
                                java.io.File(path).copyTo(target, overwrite = true)

                                val memory = com.jhoel.framepuzzle.core.domain.model.Memory(
                                    id = memoryId,
                                    title = "Recuerdo ${System.currentTimeMillis()}",
                                    originalImagePath = target.absolutePath,
                                    editedImagePath = null,
                                    createdDate = System.currentTimeMillis(),
                                    albumId = null,
                                    progress = 0f,
                                    favorite = false,
                                )
                                memoryRepository.create(memory)
                                pendingPath = null
                                isCreating = false
                                onMemoryCreated()
                            } catch (e: Exception) {
                                isCreating = false
                                error = e.message ?: "Error al crear recuerdo"
                            }
                        }
                    },
                    onRetake = { pendingPath = null; error = null },
                )
            }
            else -> {
                CreateOptions(
                    onOpenGallery = { galleryPicker.launch("image/*") },
                )
            }
        }

        error?.let { err ->
            Text(
                text = err,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun CreateOptions(
    onOpenGallery: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.Camera,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Crear recuerdo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Elige una imagen de tu galería",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onOpenGallery,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Icon(Icons.Outlined.PhotoLibrary, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Elegir de galería")
        }
    }
}

@Composable
private fun ConfirmScreen(
    imagePath: String,
    isCreating: Boolean,
    onConfirm: () -> Unit,
    onRetake: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth().background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = imagePath,
                contentDescription = "Imagen",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onRetake,
                enabled = !isCreating,
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Outlined.Close, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Reintentar")
            }
            Button(
                onClick = onConfirm,
                enabled = !isCreating,
                modifier = Modifier.weight(1f).height(52.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Outlined.Check, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text(if (isCreating) "Guardando…" else "Usar imagen")
            }
        }
    }
}
