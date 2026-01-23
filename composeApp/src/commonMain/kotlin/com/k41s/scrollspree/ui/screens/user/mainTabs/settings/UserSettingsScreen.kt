package com.k41s.scrollspree.ui.screens.user.mainTabs.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.screens.user.mainTabs.settings.components.SettingsClickableRow
import com.k41s.scrollspree.ui.screens.user.mainTabs.settings.components.SettingsSectionHeader
import com.k41s.scrollspree.ui.screens.user.mainTabs.settings.components.SettingsSwitchRow
import com.k41s.scrollspree.ui.theme.Theme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserSettingsScreen() {
    val viewModel: UserSettingsViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        when (val state = uiState) {
            is UserSettingsUiState.Loading -> {
                BasicLoadingScreen()
            }
            is UserSettingsUiState.Success -> {
                SettingsSectionHeader("Preferences")

                SettingsSwitchRow(
                    title = "Push Notifications",
                    icon = Icons.Default.Notifications,
                    checked = state.isNotificationsEnabled,
                    onCheckedChange = { viewModel.onNotificationToggled(it) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box {
                    SettingsClickableRow(
                        title = "App Theme",
                        subtitle = state.currentTheme.name
                            .replace("_", " ")
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        icon = Icons.Default.Palette,
                        onClick = { viewModel.setMenuExpanded(true) }
                    )

                    DropdownMenu(
                        expanded = state.isThemeMenuExpanded,
                        onDismissRequest = { viewModel.setMenuExpanded(false) }
                    ) {
                        Theme.entries.forEach { theme ->
                            DropdownMenuItem(
                                text = {
                                    Text(theme.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() })
                                },
                                leadingIcon = {
                                    val icon = when(theme) {
                                        Theme.LIGHT -> Icons.Default.LightMode
                                        Theme.DARK -> Icons.Default.DarkMode
                                        Theme.BLACK_WHITE -> Icons.Default.Contrast
                                        Theme.SYSTEM -> Icons.Default.SettingsSuggest
                                    }
                                    Icon(icon, contentDescription = null)
                                },
                                onClick = { viewModel.onThemeSelected(theme) }
                            )
                        }
                    }
                }
            }
            is UserSettingsUiState.Error -> {
                Text(state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}