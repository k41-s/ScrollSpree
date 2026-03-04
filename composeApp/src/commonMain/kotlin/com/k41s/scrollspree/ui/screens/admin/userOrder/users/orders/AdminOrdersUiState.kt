package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders

import com.k41s.scrollspree.domain.model.User

sealed class AdminOrdersUiState {
    data object Loading : AdminOrdersUiState()
    data class Error(val message: String) : AdminOrdersUiState()
    data class Success(val user: User) : AdminOrdersUiState()
}