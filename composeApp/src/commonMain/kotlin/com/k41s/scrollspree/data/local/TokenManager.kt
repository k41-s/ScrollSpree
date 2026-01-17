package com.k41s.scrollspree.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val ROLE_KEY = stringPreferencesKey("user_role")
        private val USERNAME_KEY = stringPreferencesKey("user_name")
    }

    val token: Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }
    val username: Flow<String?> = dataStore.data.map { it[USERNAME_KEY] }
    val role: Flow<Role?> = dataStore.data.map { preferences ->
        preferences[ROLE_KEY]?.let {
            try { Role.valueOf(it) } catch (e: Exception) { null }
        }
    }

    suspend fun saveAuthData(token: String, role: Role, username: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[ROLE_KEY] = role.name
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun clearAuthData() {
        dataStore.edit { it.clear() }
    }

}