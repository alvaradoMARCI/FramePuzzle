package com.jhoel.framepuzzle.feature.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Configuraciones globales de FramePuzzle (sección 31, DataStore).
 *
 * Almacena preferencias del usuario: tema, idioma, hapticos, sonidos,
 * bloqueo automático, etc. NO almacena credenciales (esas van a PinManager).
 */
private val Context.dataStore by preferencesDataStore(name = "framepuzzle_settings")

@Singleton
class AppSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    object Keys {
        val THEME_DARK = booleanPreferencesKey("theme_dark")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val AUTO_LOCK_MINUTES = intPreferencesKey("auto_lock_minutes")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val LAST_USED_DIFFICULTY = stringPreferencesKey("last_difficulty")
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { it[Keys.THEME_DARK] ?: true }
    val hapticsEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.HAPTICS_ENABLED] ?: true }
    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.SOUND_ENABLED] ?: true }
    val autoLockMinutes: Flow<Int> = context.dataStore.data.map { it[Keys.AUTO_LOCK_MINUTES] ?: 5 }
    val onboardingDone: Flow<Boolean> = context.dataStore.data.map { it[Keys.ONBOARDING_DONE] ?: false }

    suspend fun setDarkTheme(value: Boolean) {
        context.dataStore.edit { it[Keys.THEME_DARK] = value }
    }

    suspend fun setHapticsEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.HAPTICS_ENABLED] = value }
    }

    suspend fun setSoundEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.SOUND_ENABLED] = value }
    }

    suspend fun setAutoLockMinutes(value: Int) {
        context.dataStore.edit { it[Keys.AUTO_LOCK_MINUTES] = value }
    }

    suspend fun setOnboardingDone() {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }
}
