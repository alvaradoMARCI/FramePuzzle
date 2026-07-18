package com.jhoel.framepuzzle.feature.transfer.data

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.jhoel.framepuzzle.core.security.crypto.CryptoManager
import com.jhoel.framepuzzle.feature.transfer.domain.TransferToken
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Administrador de transferencia entre teléfonos (sección 37).
 *
 * Método principal: transferencia mediante QR.
 *
 * Flujo:
 *   Teléfono antiguo:
 *     - Genera código de transferencia (QR).
 *   Teléfono nuevo:
 *     - Escanea código.
 *     - Se inicia transferencia segura.
 *
 * Características:
 *   - Transferencia directa (P2P).
 *   - Uso opcional de internet para acelerar.
 *   - Datos protegidos (cifrados con OTP y CryptoManager).
 *   - Sin dependencia permanente de servidores.
 */
@Singleton
class TransferManager @Inject constructor(
    private val crypto: CryptoManager,
) {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Genera un [TransferToken] fresco para esta sesión.
     */
    fun generateSessionToken(deviceName: String, dataSizeBytes: Long): TransferToken {
        return TransferToken(
            deviceName = deviceName,
            sessionId = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            port = DEFAULT_PORT,
            otp = UUID.randomUUID().toString().take(8),
            dataSizeBytes = dataSizeBytes,
        )
    }

    /**
     * Serializa y cifra el token, devolviendo un string seguro para QR.
     */
    fun encodeToken(token: TransferToken): String {
        val plaintext = json.encodeToString(TransferToken.serializer(), token).toByteArray()
        val payload = crypto.encrypt(plaintext)
        // Codificamos como base64 con cabecera: FPT1.<iv_b64>.<ct_b64>
        val ivB64 = android.util.Base64.encodeToString(payload.iv, android.util.Base64.NO_WRAP)
        val ctB64 = android.util.Base64.encodeToString(payload.ciphertext, android.util.Base64.NO_WRAP)
        return "FPT1.$ivB64.$ctB64"
    }

    /**
     * Decodifica un string QR al token original.
     */
    fun decodeToken(qr: String): TransferToken? {
        val parts = qr.split(".")
        if (parts.size != 3 || parts[0] != "FPT1") return null
        return try {
            val iv = android.util.Base64.decode(parts[1], android.util.Base64.NO_WRAP)
            val ct = android.util.Base64.decode(parts[2], android.util.Base64.NO_WRAP)
            val bytes = crypto.decrypt(CryptoManager.EncryptedPayload(iv = iv, ciphertext = ct))
            json.decodeFromString(TransferToken.serializer(), bytes.toString(Charsets.UTF_8))
        } catch (t: Throwable) {
            null
        }
    }

    /**
     * Genera el bitmap del QR a partir de un texto.
     */
    fun renderQrBitmap(content: String, size: Int = 600): Bitmap {
        val hints = mapOf(
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
            EncodeHintType.MARGIN to 1,
        )
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    companion object {
        const val DEFAULT_PORT = 48231
    }
}
