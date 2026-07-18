package com.jhoel.framepuzzle.core.security.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manejador de biometría (sección 38).
 *
 * FramePuzzle usa biometría solo como *atajo* de acceso; el PIN siempre
 * está disponible como fallback.
 */
@Singleton
class BiometricManagerHelper @Inject constructor() {

    /** ¿Puede usarse biometría en este dispositivo? */
    fun canAuthenticate(activity: FragmentActivity): Boolean {
        val bm = BiometricManager.from(activity)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK
        return bm.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Lanza el prompt biométrico.
     * @param onSuccess callback si la autenticación es exitosa.
     * @param onError callback con código y mensaje si falla.
     */
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (Int, String) -> Unit,
    ) {
        val executor = androidx.core.content.ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errorCode, errString.toString())
                }
            },
        )
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Usar PIN")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()
        prompt.authenticate(info)
    }
}
