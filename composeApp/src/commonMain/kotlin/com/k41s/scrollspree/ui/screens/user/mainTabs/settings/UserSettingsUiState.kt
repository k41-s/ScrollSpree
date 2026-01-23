package com.k41s.scrollspree.ui.screens.user.mainTabs.settings

import com.k41s.scrollspree.ui.theme.Theme

sealed class UserSettingsUiState {
    data object Loading : UserSettingsUiState()
    data class Success(
        val isNotificationsEnabled: Boolean,
        val currentTheme: Theme,
        val isThemeMenuExpanded: Boolean = false
    ) : UserSettingsUiState()
    data class Error(val message: String) : UserSettingsUiState()
}