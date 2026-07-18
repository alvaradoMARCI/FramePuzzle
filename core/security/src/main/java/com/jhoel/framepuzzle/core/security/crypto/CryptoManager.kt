package com.jhoel.framepuzzle.core.security.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manejador de cifrado FramePuzzle.
 *
 * Usa Android Keystore (no se almacenan claves fuera del dispositivo).
 * Algoritmo: AES/GCM/NoPadding (256 bits).
 *
 * Uso:
 *   1. [encrypt] devuelve bytes cifrados + IV.
 *   2. [decrypt] descifra con IV.
 *
 * Aplicaciones en FramePuzzle:
 *   - Cifrar archivos de respaldo (.fpbackup) (sección 36).
 *   - Cifrar tokens de transferencia (sección 37).
 *   - Cifrar el PIN del usuario.
 */
@Singleton
class CryptoManager @Inject constructor() {

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
    }

    private fun getOrCreateKey(): SecretKey {
        keyStore.getKey(KEY_ALIAS, null)?.let { return it as SecretKey }
        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE,
        )
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        generator.init(spec)
        return generator.generateKey()
    }

    /**
     * Cifra [plaintext] y devuelve [EncryptedPayload] (iv + ciphertext).
     */
    fun encrypt(plaintext: ByteArray): EncryptedPayload {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val iv = cipher.iv
        val ciphertext = cipher.doFinal(plaintext)
        return EncryptedPayload(iv = iv, ciphertext = ciphertext)
    }

    /**
     * Descifra un [EncryptedPayload].
     */
    fun decrypt(payload: EncryptedPayload): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, payload.iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)
        return cipher.doFinal(payload.ciphertext)
    }

    /**
     * Versión serializable del payload cifrado.
     * Los backups lo escriben como [iv.size][iv][ciphertext].
     */
    data class EncryptedPayload(
        val iv: ByteArray,
        val ciphertext: ByteArray,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is EncryptedPayload) return false
            return iv.contentEquals(other.iv) && ciphertext.contentEquals(other.ciphertext)
        }

        override fun hashCode(): Int = 31 * iv.contentHashCode() + ciphertext.contentHashCode()
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "framepuzzle_master_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH_BITS = 128
    }
}
