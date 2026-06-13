package com.k41s.scrollspree.domain.manager

import com.k41s.scrollspree.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartManager {

    private val _cartState = MutableStateFlow<Map<Product, Int>>(emptyMap())

    val cartState: StateFlow<Map<Product, Int>> = _cartState.asStateFlow()

    fun addToCart(product: Product, quantity: Int = 1) {
        _cartState.update { currentCart ->
            val updatedCart = currentCart.toMutableMap()
            val currentQuantity = updatedCart[product] ?: 0
            updatedCart[product] = currentQuantity + quantity
            updatedCart
        }
    }

    fun updateQuantity(product: Product, change: Int) {
        _cartState.update { currentCart ->
            val updatedCart = currentCart.toMutableMap()
            val currentQuantity = updatedCart[product] ?: 0
            val newQuantity = currentQuantity + change

            if (newQuantity <= 0) {
                updatedCart.remove(product)
            } else {
                updatedCart[product] = newQuantity
            }
            updatedCart
        }
    }

    fun removeFromCart(product: Product) {
        _cartState.update { currentCart ->
            val updatedCart = currentCart.toMutableMap()
            updatedCart.remove(product)
            updatedCart
        }
    }

    fun clearCart() {
        _cartState.value = emptyMap()
    }
}