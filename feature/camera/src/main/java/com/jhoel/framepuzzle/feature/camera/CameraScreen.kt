package com.jhoel.framepuzzle.feature.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jhoel.framepuzzle.core.designsystem.tokens.FramePuzzleSpacing
import com.jhoel.framepuzzle.feature.camera.ui.CameraViewModel
import com.jhoel.framepuzzle.feature.camera.ui.CameraXHelper

/**
 * Pantalla Crear/Cámara (sección 11 + 12).
 *
 * Funciones principales:
 *  - Tomar fotografía (cámara frontal/trasera).
 *  - Elegir imagen de galería.
 *  - Crear nuevo recuerdo.
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
            viewModel.createMemory(path)
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
        if (state.hasCameraPermission) {
            CameraPreview(
                lensFacing = if (state.cameraSelector == com.jhoel.framepuzzle.feature.camera.CameraSelectorUi.BACK)
                    CameraXHelper.LENS_BACK else CameraXHelper.LENS_FRONT,
                modifier = Modifier.fillMaxSize(),
                onCapture = { path -> viewModel.createMemory(path) },
                onFlip = viewModel::flipCamera,
                onOpenGallery = { galleryPicker.launch("image/*") },
                flashEnabled = state.flashEnabled,
                onToggleFlash = viewModel::toggleFlash,
                isCapturing = state.isCapturing,
            )
        } else {
            NoPermissionState(
                onRequestCamera = { cameraLauncher.launch(cameraPermission) },
                onRequestGallery = { galleryLauncher.launch(galleryPermission) },
                onOpenGallery = { galleryPicker.launch("image/*") },
            )
        }

        state.error?.let { err ->
            AlertDialog(
                onDismissRequest = viewModel::clearError,
                confirmButton = { Button(onClick = viewModel::clearError) { Text("OK") } },
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
    flashEnabled: Boolean,
    onToggleFlash: () -> Unit,
    isCapturing: Boolean,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val helper = remember { com.jhoel.framepuzzle.feature.camera.ui.CameraXHelperProvider.get(context) }
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    DisposableEffect(lensFacing) {
        helper.startPreview(lifecycleOwner, previewView, lensFacing)
        onDispose { /* CameraX se desliga automáticamente */ }
    }

    Box(modifier = modifier) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        // Controles inferiores
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
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
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCapturing) MaterialTheme.colorScheme.surfaceVariant
                        else MaterialTheme.colorScheme.primary,
                    )
                    .clickable(enabled = !isCapturing) {
                        val fileName = java.util.UUID.randomUUID().toString()
                        helper.capture(
                            imageCapture = imageCapture,
                            fileName = fileName,
                            onSaved = onCapture,
                            onError = { /* el helper loguea */ },
                        )
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (isCapturing) {
                    Text("…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
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

        // Botón flash arriba
        IconButton(
            onClick = onToggleFlash,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
        ) {
            Icon(
                imageVector = if (flashEnabled) Icons.Outlined.FlashOn else Icons.Outlined.FlashOff,
                contentDescription = "Flash",
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun NoPermissionState(
    onRequestCamera: () -> Unit,
    onRequestGallery: () -> Unit,
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
            text = "Toma una foto o elige una imagen de tu galería.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(FramePuzzleSpacing.xl))
        Button(onClick = onRequestCamera, modifier = Modifier.fillMaxWidth()) {
            Text("Tomar fotografía")
        }
        Spacer(Modifier.height(FramePuzzleSpacing.sm))
        OutlinedButton(onClick = onRequestGallery, modifier = Modifier.fillMaxWidth()) {
            Text("Dar permiso de galería")
        }
        Spacer(Modifier.height(FramePuzzleSpacing.sm))
        OutlinedButton(onClick = onOpenGallery, modifier = Modifier.fillMaxWidth()) {
            Text("Elegir imagen")
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
