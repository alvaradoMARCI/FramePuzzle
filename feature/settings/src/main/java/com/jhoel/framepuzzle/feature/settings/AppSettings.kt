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
 * Solo contiene opciones realmente funcionales. Las opciones que aún no
 * están implementadas no aparecen en la UI.
 */
private val Context.dataStore by preferencesDataStore(name = "framepuzzle_settings")

/** Modo de tema: 0 = seguir sistema, 1 = claro forzado, 2 = oscuro forzado. */
enum class ThemeMode(val display: String) {
    SYSTEM("Seguir sistema"),
    LIGHT("Claro"),
    DARK("Oscuro"),
}

@Singleton
class AppSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    object Keys {
        val THEME_MODE = intPreferencesKey("theme_mode")
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        val raw = prefs[Keys.THEME_MODE] ?: 0
        when (raw) {
            1 -> ThemeMode.LIGHT
            2 -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    val onboardingDone: Flow<Boolean> = context.dataStore.data.map { it[Keys.ONBOARDING_DONE] ?: false }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.ordinal }
    }

    suspend fun setOnboardingDone() {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = true }
    }
}
