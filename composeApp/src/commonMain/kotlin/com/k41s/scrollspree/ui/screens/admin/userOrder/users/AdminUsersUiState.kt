package com.k41s.scrollspree.ui.screens.admin.userOrder.users

import com.k41s.scrollspree.domain.model.User

sealed class AdminUsersUiState {
    data object Loading : AdminUsersUiState()
    data class Success(val users: List<User>) : AdminUsersUiState()
    data class Error(val message: String) : AdminUsersUiState()
}