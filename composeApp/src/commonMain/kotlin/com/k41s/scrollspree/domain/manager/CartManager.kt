package com.k41s.scrollspree.domain.manager

import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.data.repository.CartRepository
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class CartManager(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {
    private val _cartState = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cartState: StateFlow<Map<Product, Int>> = _cartState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        scope.launch {
            authRepository.getCurrentUser().collect { user ->
                if (user != null) {
                    syncLocalCartToApi()
                    fetchCart()
                }
            }
        }
    }

    private suspend fun syncLocalCartToApi() {
        val localItems = _cartState.value
        if (localItems.isNotEmpty()) {
            for ((product, quantity) in localItems) {
                cartRepository.addToCart(product.id, quantity)
            }
        }
    }

    fun fetchCart() {
        scope.launch {
            _isLoading.value = true
            when (val result = cartRepository.getCart()) {
                is NetworkResult.Success -> _cartState.value = result.data
                is NetworkResult.Error -> _errorEvent.emit(result.message)
                is NetworkResult.Loading -> { /* Handled by the isLoading flow */ }
            }
            _isLoading.value = false
        }
    }

    fun addToCart(product: Product, quantity: Int = 1) {
        scope.launch {
            _isLoading.value = true
            val user = authRepository.getCurrentUser().firstOrNull()

            if (user != null) {
                when (val result = cartRepository.addToCart(product.id, quantity)) {
                    is NetworkResult.Success -> _cartState.value = result.data
                    is NetworkResult.Error -> _errorEvent.emit(result.message)
                    is NetworkResult.Loading -> {}
                }
            } else {
                val currentCart = _cartState.value.toMutableMap()
                val currentQty = currentCart[product] ?: 0
                currentCart[product] = currentQty + quantity
                _cartState.value = currentCart
            }
            _isLoading.value = false
        }
    }

    fun updateQuantity(product: Product, change: Int) {
        val currentQuantity = _cartState.value[product] ?: 0
        val newQuantity = currentQuantity + change

        if (newQuantity <= 0) {
            removeFromCart(product)
            return
        }

        scope.launch {
            _isLoading.value = true
            val user = authRepository.getCurrentUser().firstOrNull()

            if (user != null) {
                when (val result = cartRepository.updateCartItemQuantity(product.id, newQuantity)) {
                    is NetworkResult.Success -> _cartState.value = result.data
                    is NetworkResult.Error -> _errorEvent.emit(result.message)
                    is NetworkResult.Loading -> {}
                }
            } else {
                val currentCart = _cartState.value.toMutableMap()
                currentCart[product] = newQuantity
                _cartState.value = currentCart
            }
            _isLoading.value = false
        }
    }

    fun removeFromCart(product: Product) {
        scope.launch {
            _isLoading.value = true
            val user = authRepository.getCurrentUser().firstOrNull()

            if (user != null) {
                when (val result = cartRepository.removeFromCart(product.id)) {
                    is NetworkResult.Success -> _cartState.value = result.data
                    is NetworkResult.Error -> _errorEvent.emit(result.message)
                    is NetworkResult.Loading -> {}
                }
            } else {
                val currentCart = _cartState.value.toMutableMap()
                currentCart.remove(product)
                _cartState.value = currentCart
            }
            _isLoading.value = false
        }
    }

    fun clearCart() {
        scope.launch {
            _isLoading.value = true
            when (val result = cartRepository.clearCart()) {
                is NetworkResult.Success -> _cartState.value = result.data
                is NetworkResult.Error -> _errorEvent.emit(result.message)
                is NetworkResult.Loading -> {}
            }
            _isLoading.value = false
        }
    }
}