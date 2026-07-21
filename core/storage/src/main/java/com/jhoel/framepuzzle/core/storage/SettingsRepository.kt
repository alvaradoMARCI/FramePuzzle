package com.jhoel.framepuzzle.core.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "framepuzzle_settings")

enum class ThemeMode { SYSTEM, LIGHT, DARK }

class SettingsRepository(private val context: Context) {
    private val key = intPreferencesKey("theme_mode")

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        runCatching {
            when (prefs[key] ?: 0) {
                1 -> ThemeMode.LIGHT
                2 -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
        }.getOrDefault(ThemeMode.SYSTEM)
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        runCatching {
            context.dataStore.edit { it[key] = mode.ordinal }
        }
    }
}
