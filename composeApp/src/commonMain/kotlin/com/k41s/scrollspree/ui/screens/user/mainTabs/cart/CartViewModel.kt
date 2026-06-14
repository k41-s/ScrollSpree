package com.k41s.scrollspree.ui.screens.user.mainTabs.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.domain.manager.CartManager
import com.k41s.scrollspree.domain.model.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel(
    private val cartManager: CartManager,
    authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<CartUiState> = combine(
        cartManager.cartState,
        authRepository.getCurrentUser()
    ) { itemsMap, user ->
        val total = itemsMap.entries.sumOf { (product, quantity) -> product.price * quantity }
        CartUiState(
            items = itemsMap,
            totalAmount = total,
            isLoggedIn = user != null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartUiState()
    )

    fun updateQuantity(product: Product, change: Int) {
        cartManager.updateQuantity(product, change)
    }

    fun removeProduct(product: Product) {
        cartManager.removeFromCart(product)
    }
}