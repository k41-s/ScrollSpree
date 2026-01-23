package com.k41s.scrollspree.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.local.SettingsManager
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.domain.model.enums.Role
import com.k41s.scrollspree.ui.theme.Theme
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel (
    private val repository: AuthRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    val viewState: StateFlow<AppViewState> = repository.getCurrentUser()
        .map { user ->
            if (user != null) {
                if (user.role == Role.ADMIN) AppViewState.AdminAuthenticated
                else AppViewState.UserAuthenticated
            } else {
                AppViewState.Unauthorized
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppViewState.Loading
        )

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val themePreference: StateFlow<Theme> = settingsManager.themeSelection
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Theme.SYSTEM
        )
}