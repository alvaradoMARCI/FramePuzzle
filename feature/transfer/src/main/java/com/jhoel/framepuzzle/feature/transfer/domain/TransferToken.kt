package com.jhoel.framepuzzle.feature.transfer.domain

import kotlinx.serialization.Serializable

/**
 * Token de transferencia entre teléfonos (sección 37).
 *
 * El teléfono antiguo genera este payload, lo serializa a JSON,
 * lo cifra con CryptoManager y lo codifica como QR.
 *
 * El teléfono nuevo escanea el QR, descifra y obtiene la información
 * necesaria para iniciar la transferencia directa (P2P).
 */
@Serializable
data class TransferToken(
    val deviceName: String,
    val sessionId: String,
    val timestamp: Long,
    val port: Int,
    val otp: String,
    val dataSizeBytes: Long,
)
