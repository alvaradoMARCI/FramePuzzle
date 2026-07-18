package com.jhoel.framepuzzle.feature.backup.domain

import kotlinx.serialization.Serializable

/**
 * Estructura del archivo .fpbackup (sección 36).
 *
 * Formato: ZIP cifrado con AES/GCM (CryptoManager).
 * Dentro del ZIP:
 *  - manifest.json (este modelo)
 *  - database.db (copia de la Room DB)
 *  - images/ (carpeta con originales y editadas)
 *
 * Validación de integridad: SHA-256 del contenido incluido en el manifest.
 */
@Serializable
data class BackupManifest(
    val version: Int = 1,
    val appVersion: String = "0.1.0-alpha",
    val createdAt: Long,
    val imageCount: Int,
    val dbSizeBytes: Long,
    val sha256: String,
    val deviceModel: String,
)
