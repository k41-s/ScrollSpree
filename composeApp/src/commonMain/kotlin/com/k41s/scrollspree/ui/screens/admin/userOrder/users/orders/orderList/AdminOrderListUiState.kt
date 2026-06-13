package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.orderList

import com.k41s.scrollspree.domain.model.Order

sealed class AdminOrderListUiState {
    object Loading : AdminOrderListUiState()
    data class Success(val orders: List<Order>) : AdminOrderListUiState()
    data class Error(val message: String) : AdminOrderListUiState()
}