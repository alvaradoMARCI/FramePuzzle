package com.jhoel.framepuzzle.core.security.pin

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Almacén seguro del PIN de FramePuzzle (sección 38: Protección de acceso).
 *
 * El PIN nunca se guarda en texto plano. Se almacena su hash SHA-256
 * dentro de EncryptedSharedPreferences (cifrado adicional con MasterKey).
 *
 * El PIN real viaja por la app solo en memoria, durante la verificación.
 */
@Singleton
class PinManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val appContext: Context = context.applicationContext

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val prefs by lazy {
        EncryptedSharedPreferences.create(
            context,
            FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    /** ¿El usuario ya configuró un PIN? */
    fun isPinSet(): Boolean = prefs.contains(KEY_PIN_HASH)

    /** Guarda el hash del PIN. */
    fun setPin(pin: String) {
        val hashed = hash(pin)
        prefs.edit().putString(KEY_PIN_HASH, hashed).apply()
    }

    /** Cambia el PIN: borra el anterior y guarda el nuevo. */
    fun changePin(oldPin: String, newPin: String): Boolean {
        if (!verify(oldPin)) return false
        setPin(newPin)
        return true
    }

    /** Verifica un PIN ingresado contra el hash almacenado. */
    fun verify(pin: String): Boolean {
        val stored = prefs.getString(KEY_PIN_HASH, null) ?: return false
        return hash(pin) == stored
    }

    /** Elimina el PIN. */
    fun clear() {
        prefs.edit().remove(KEY_PIN_HASH).apply()
    }

    private fun hash(pin: String): String {
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val bytes = md.digest((pin + SALT).toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        private const val FILE_NAME = "framepuzzle_secure_prefs"
        private const val KEY_PIN_HASH = "pin_hash"
        private const val SALT = "FramePuzzle::v1::salt"
    }
}
