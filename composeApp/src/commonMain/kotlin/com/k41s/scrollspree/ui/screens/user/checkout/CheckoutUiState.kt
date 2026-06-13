package com.k41s.scrollspree.ui.screens.user.checkout

import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.domain.model.enums.PaymentMethod

data class CheckoutUiState(
    val cartItems: Map<Product, Int> = emptyMap(),
    val notes: String = "",
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isOrderPlaced: Boolean = false,
    val paypalRedirectUrl: String? = null
) {
    val cartTotal: Double
        get() = cartItems.entries.sumOf {
            (product, quantity) -> product.price * quantity
        }
}