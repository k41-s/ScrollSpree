package com.k41s.scrollspree.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.k41s.scrollspree.ui.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsManager(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val THEME = stringPreferencesKey("app_theme")
    }

    val isNotificationsEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[Keys.NOTIFICATIONS_ENABLED] ?: true
        }

    val themeSelection: Flow<Theme> = dataStore.data
        .map { preferences ->
            val name = preferences[Keys.THEME] ?: Theme.SYSTEM.name
            try {
                Theme.valueOf(name)
            } catch (e: Exception) {
                Theme.SYSTEM
            }
        }

    suspend fun saveNotificationPreference(enabled: Boolean) {
        dataStore.edit { it[Keys.NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun saveThemePreference(theme: Theme) {
        dataStore.edit { it[Keys.THEME] = theme.name }
    }
}