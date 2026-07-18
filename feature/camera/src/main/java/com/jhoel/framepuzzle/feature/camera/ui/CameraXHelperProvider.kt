package com.jhoel.framepuzzle.feature.camera.ui

import android.content.Context
import com.jhoel.framepuzzle.core.storage.local.LocalStorageManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

/**
 * Entry Point Hilt para acceder a CameraXHelper desde Compose sin ViewModel.
 *
 * Necesario porque la pantalla de cámara necesita el helper para iniciar
 * la preview y disparar capturas, y no queremos pasar todo por ViewModel.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface CameraXHelperEntryPoint {
    fun cameraXHelper(): CameraXHelper
}

object CameraXHelperProvider {
    fun get(context: Context): CameraXHelper {
        return EntryPointAccessors.fromApplication(
            context.applicationContext,
            CameraXHelperEntryPoint::class.java,
        ).cameraXHelper()
    }
}
