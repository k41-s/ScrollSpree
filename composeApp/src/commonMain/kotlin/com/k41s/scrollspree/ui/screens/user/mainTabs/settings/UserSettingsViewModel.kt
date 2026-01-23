package com.k41s.scrollspree.ui.screens.user.mainTabs.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.local.SettingsManager
import com.k41s.scrollspree.ui.theme.Theme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserSettingsViewModel(
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _isThemeMenuExpanded = MutableStateFlow(false)

    private val _uiState = MutableStateFlow<UserSettingsUiState>(UserSettingsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                settingsManager.isNotificationsEnabled,
                settingsManager.themeSelection,
                _isThemeMenuExpanded
            ) { notifications, theme, menuExpanded ->
                UserSettingsUiState.Success(
                    isNotificationsEnabled = notifications,
                    currentTheme = theme,
                    isThemeMenuExpanded = menuExpanded
                )
            }.collect { successState ->
                _uiState.value = successState
            }
        }
    }

    fun setMenuExpanded(expanded: Boolean) {
        _isThemeMenuExpanded.value = expanded
    }

    fun onNotificationToggled(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.saveNotificationPreference(enabled)
        }
    }

    fun onThemeSelected(theme: Theme) {
        viewModelScope.launch {
            settingsManager.saveThemePreference(theme)
            setMenuExpanded(false)
        }
    }
}