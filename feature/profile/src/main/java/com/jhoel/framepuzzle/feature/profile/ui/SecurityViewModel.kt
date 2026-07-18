package com.jhoel.framepuzzle.feature.profile.ui

import androidx.lifecycle.ViewModel
import com.jhoel.framepuzzle.core.security.biometric.BiometricManagerHelper
import com.jhoel.framepuzzle.core.security.pin.PinManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecurityViewModel @Inject constructor(
    val pinManager: PinManager,
    val biometricHelper: BiometricManagerHelper,
) : ViewModel() {

    fun setPin(pin: String) = pinManager.setPin(pin)

    fun verifyPin(pin: String): Boolean = pinManager.verify(pin)

    fun clearPin() = pinManager.clear()
}
