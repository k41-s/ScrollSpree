package com.k41s.scrollspree.ui.screens.user.myOrders.orderDetail

import com.k41s.scrollspree.domain.model.Order

data class OrderDetailUiState(
    val isLoading: Boolean = true,
    val order: Order? = null,
    val errorMessage: String? = null
)