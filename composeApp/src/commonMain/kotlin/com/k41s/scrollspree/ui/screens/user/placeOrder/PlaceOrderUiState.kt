package com.k41s.scrollspree.ui.screens.user.placeOrder

import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.domain.model.enums.PaymentMethod

data class PlaceOrderUiState(
    val product: Product? = null,
    val notes: String = "",
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOrderPlaced: Boolean = false
)