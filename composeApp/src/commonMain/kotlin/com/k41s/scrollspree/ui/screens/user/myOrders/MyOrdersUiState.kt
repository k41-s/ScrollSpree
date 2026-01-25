package com.k41s.scrollspree.ui.screens.user.myOrders

import com.k41s.scrollspree.domain.model.Order

sealed class MyOrdersUiState {
    data object Loading : MyOrdersUiState()
    data class Success(val orders: List<Order>) : MyOrdersUiState()
    data class Error(val message: String) : MyOrdersUiState()
}