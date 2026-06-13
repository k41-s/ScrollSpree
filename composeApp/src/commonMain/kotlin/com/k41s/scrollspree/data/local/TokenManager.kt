package com.k41s.scrollspree.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TokenManager(
    private val dataStore: DataStore<Preferences>,
    scope: CoroutineScope
) {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("jwt_access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("jwt_refresh_token")
        private val ROLE_KEY = stringPreferencesKey("user_role")
        private val USERNAME_KEY = stringPreferencesKey("user_name")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
    }

    val token: StateFlow<String?> = dataStore.data
        .map { it[ACCESS_TOKEN_KEY] }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
    val refreshToken: Flow<String?> = dataStore.data.map { it[REFRESH_TOKEN_KEY] }
    val username: Flow<String?> = dataStore.data.map { it[USERNAME_KEY] }
    val email: Flow<String?> = dataStore.data.map { it[EMAIL_KEY] }

    val role: Flow<Role?> = dataStore.data.map { preferences ->
        preferences[ROLE_KEY]?.let {
            try { Role.valueOf(it) } catch (e: Exception) { null }
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit {
            it[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun saveRole(role: Role) {
        dataStore.edit {
            it[ROLE_KEY] = role.name
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit {
            it[USERNAME_KEY] = username
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit {
            it[EMAIL_KEY] = email
        }
    }

    suspend fun saveAuthData(
        token: String,
        refreshToken: String,
        role: Role,
        username: String,
        email: String
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[ROLE_KEY] = role.name
            preferences[USERNAME_KEY] = username
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun clearAuthData() {
        dataStore.edit { it.clear() }
    }
}