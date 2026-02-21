package com.k41s.scrollspree.ui.screens.admin.userOrder.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminUsersContainer() {

    val viewModel: AdminUsersViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is AdminUsersUiState.Loading -> BasicLoadingScreen()
        is AdminUsersUiState.Error -> ErrorScreen(state.message) {
            viewModel.loadUsers()
        }
        is AdminUsersUiState.Success -> {
            AdminUsersScreen(state.users) {
                // navigate to user's orders on click
            }
        }
    }
}