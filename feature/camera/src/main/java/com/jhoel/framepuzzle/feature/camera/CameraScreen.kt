package com.jhoel.framepuzzle.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.feature.camera.ui.CameraViewModel
import com.jhoel.framepuzzle.feature.camera.ui.CameraXHelper
import com.jhoel.framepuzzle.feature.camera.ui.CameraXHelperProvider

/**
 * Pantalla Crear/Cámara (sección 11 + 12).
 *
 * Flujo:
 *   1. Usuario abre la cámara (con permiso).
 *   2. Toma fotografía o elige imagen de galería.
 *   3. Se muestra preview con confirmación: aceptar o reintentar.
 *   4. Al aceptar, se crea el recuerdo y se navega al editor.
 */
@Composable
fun CameraScreen(
    onMemoryCreated: (memoryId: String) -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val cameraPermission = Manifest.permission.CAMERA
    val galleryPermission = if (android.os.Build.VERSION.SDK_INT >= 33) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        viewModel.setPermissions(camera = granted, gallery = state.hasGalleryPermission)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        viewModel.setPermissions(camera = state.hasCameraPermission, gallery = granted)
    }

    val galleryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri != null) {
            val path = copyUriToTemp(context, uri)
            viewModel.onImagePicked(path)
        }
    }

    LaunchedEffect(Unit) {
        val cameraGranted = ContextCompat.checkSelfPermission(context, cameraPermission) ==
            PackageManager.PERMISSION_GRANTED
        val galleryGranted = ContextCompat.checkSelfPermission(context, galleryPermission) ==
            PackageManager.PERMISSION_GRANTED
        viewModel.setPermissions(cameraGranted, galleryGranted)
        if (!cameraGranted) cameraLauncher.launch(cameraPermission)
    }

    LaunchedEffect(state.capturedMemoryId) {
        state.capturedMemoryId?.let { id ->
            onMemoryCreated(id)
            viewModel.consumeNavigation()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when {
            // Etapa 3: confirmación de captura
            state.pendingImagePath != null -> {
                ConfirmCaptureScreen(
                    imagePath = state.pendingImagePath!!,
                    onConfirm = { viewModel.confirmCapture() },
                    onRetake = { viewModel.discardCapture() },
                )
            }
            // Etapa 1/2: cámara activa con permiso
            state.hasCameraPermission -> {
                CameraPreview(
                    lensFacing = if (state.cameraSelector == CameraSelectorUi.BACK)
                        CameraXHelper.LENS_BACK else CameraXHelper.LENS_FRONT,
                    modifier = Modifier.fillMaxSize(),
                    onCapture = { path -> viewModel.onImageCaptured(path) },
                    onFlip = viewModel::flipCamera,
                    onOpenGallery = {
                        if (state.hasGalleryPermission) {
                            galleryPicker.launch("image/*")
                        } else {
                            galleryLauncher.launch(galleryPermission)
                        }
                    },
                    isCapturing = state.isCapturing,
                )
            }
            // Sin permiso de cámara
            else -> {
                NoPermissionState(
                    onRequestCamera = { cameraLauncher.launch(cameraPermission) },
                    onOpenGallery = {
                        if (state.hasGalleryPermission) {
                            galleryPicker.launch("image/*")
                        } else {
                            galleryLauncher.launch(galleryPermission)
                        }
                    },
                )
            }
        }

        state.error?.let { err ->
            AlertDialog(
                onDismissRequest = viewModel::clearError,
                confirmButton = {
                    Button(onClick = viewModel::clearError) { Text("OK") }
                },
                title = { Text("FramePuzzle") },
                text = { Text(err) },
            )
        }
    }
}

@Composable
private fun CameraPreview(
    lensFacing: Int,
    modifier: Modifier = Modifier,
    onCapture: (String) -> Unit,
    onFlip: () -> Unit,
    onOpenGallery: () -> Unit,
    isCapturing: Boolean,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val helper = remember { CameraXHelperProvider.get(context) }
    val previewView = remember { PreviewView(context).apply {
        scaleType = PreviewView.ScaleType.FILL_CENTER
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
    } }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    DisposableEffect(lensFacing) {
        imageCapture = helper.startPreview(lifecycleOwner, previewView, lensFacing)
        onDispose {
            // Desligar cámara para liberar recursos al salir
            try {
                val provider = ProcessCameraProvider.getInstance(context).get()
                provider.unbindAll()
            } catch (_: Throwable) {}
        }
    }

    Box(modifier = modifier) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // Overlay sutil para que los controles se vean bien
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.0f)),
        )

        // Controles inferiores
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 48.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onOpenGallery) {
                Icon(
                    imageVector = Icons.Outlined.PhotoLibrary,
                    contentDescription = "Galería",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp),
                )
            }

            // Botón principal de captura
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(enabled = !isCapturing && imageCapture != null) {
                        val fileName = java.util.UUID.randomUUID().toString()
                        val capture = imageCapture
                        if (capture != null) {
                            helper.capture(
                                imageCapture = capture,
                                fileName = fileName,
                                onSaved = onCapture,
                                onError = { /* el helper loguea */ },
                            )
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }

            IconButton(onClick = onFlip) {
                Icon(
                    imageVector = Icons.Outlined.Cameraswitch,
                    contentDescription = "Cambiar cámara",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp),
                )
            }
        }
    }
}

@Composable
private fun ConfirmCaptureScreen(
    imagePath: String,
    onConfirm: () -> Unit,
    onRetake: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = imagePath,
                contentDescription = "Captura",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onRetake,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Outlined.Close, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Reintentar")
            }
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Outlined.Check, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Usar foto")
            }
        }
    }
}

@Composable
private fun NoPermissionState(
    onRequestCamera: () -> Unit,
    onOpenGallery: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Crea tu primer recuerdo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(FramePuzzleSpacing.sm))
        Text(
            text = "Toma una foto o elige una imagen de tu galería para empezar a armar recuerdos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(FramePuzzleSpacing.xl))
        Button(onClick = onRequestCamera, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Permitir cámara")
        }
        Spacer(Modifier.height(FramePuzzleSpacing.sm))
        OutlinedButton(onClick = onOpenGallery, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text("Elegir imagen de galería")
        }
    }
}

private fun copyUriToTemp(context: android.content.Context, uri: Uri): String {
    val temp = java.io.File(context.cacheDir, "import_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        java.io.FileOutputStream(temp).use { output -> input.copyTo(output) }
    }
    return temp.absolutePath
}
