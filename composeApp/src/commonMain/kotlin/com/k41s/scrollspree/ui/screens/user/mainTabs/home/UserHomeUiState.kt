package com.k41s.scrollspree.ui.screens.user.mainTabs.home

import com.k41s.scrollspree.domain.model.Product

sealed class UserHomeUiState {
    data object Loading : UserHomeUiState()

    data class Success(
        val products: List<Product>
    ) : UserHomeUiState()

    data class Error(
        val message: String
    ) : UserHomeUiState()
}