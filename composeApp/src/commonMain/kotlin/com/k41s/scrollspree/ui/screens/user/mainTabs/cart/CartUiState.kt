package com.k41s.scrollspree.ui.screens.user.mainTabs.cart

import com.k41s.scrollspree.domain.model.Product

data class CartUiState(
    val items: Map<Product, Int> = emptyMap(),
    val totalAmount: Double = 0.0,
    val isLoggedIn: Boolean = false
) {
    val isEmpty: Boolean get() = items.isEmpty()
}
