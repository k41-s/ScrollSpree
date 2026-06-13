package com.k41s.scrollspree.ui.screens.user.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.dto.OrderDTO
import com.k41s.scrollspree.data.remote.dto.OrderItemDTO
import com.k41s.scrollspree.data.repository.OrderRepository
import com.k41s.scrollspree.data.repository.ProductRepository
import com.k41s.scrollspree.data.repository.UserRepository
import com.k41s.scrollspree.domain.manager.CartManager
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.domain.model.enums.PaymentMethod
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
    private val cartManager: CartManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    private var isCartCheckout = false

    fun loadInitialData(productId: Int?) {
        if (productId != null) {

            isCartCheckout = false
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                when (val result = productRepository.getById(productId)) {
                    is NetworkResult.Success -> {
                        val product = result.data

                        _uiState.update { state ->
                            val newCart = state.cartItems.toMutableMap()
                            newCart[product] = 1
                            state.copy(isLoading = false, cartItems = newCart)
                        }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                        }
                    }
                    else -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        } else {
            isCartCheckout = true
            val cartItems = cartManager.cartState.value

            if (cartItems.isEmpty()) {
                _uiState.update { it.copy(errorMessage = "Your cart is empty.") }
            } else {
                _uiState.update { it.copy(cartItems = cartItems) }
            }
        }
    }

    fun updateQuantity(product: Product, change: Int) {
        _uiState.update { state ->
            val currentQty = state.cartItems[product] ?: 0
            val newQty = currentQty + change

            val updatedCart = state.cartItems.toMutableMap()
            if (newQty <= 0) {
                updatedCart.remove(product)
            } else {
                updatedCart[product] = newQty
            }

            state.copy(cartItems = updatedCart)
        }
    }

    fun onNotesChanged(newNotes: String) {
        _uiState.update { it.copy(notes = newNotes) }
    }

    fun onPaymentMethodSelected(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun placeOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            if (_uiState.value.cartItems.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Your cart is empty.") }
                return@launch
            }

            val email = tokenManager.email.firstOrNull()
            if (email == null) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = "You must be logged in to place an order."
                )}
                return@launch
            }

            when (val userResult = userRepository.getByEmail(email)) {
                is NetworkResult.Success -> {
                    val user = userResult.data
                    val orderDto = constructOrderDto(
                        userId = user.id ?: -1,
                        username = user.username
                    )

                    when (val orderResult = orderRepository.create(orderDto)) {
                        is NetworkResult.Success -> {
                            if (isCartCheckout) {
                                cartManager.clearCart()
                            }
                            _uiState.update { it.copy(isLoading = false, isOrderPlaced = true) }
                        }
                        is NetworkResult.Error -> {
                            _uiState.update { it.copy(isLoading = false, errorMessage = orderResult.message) }
                        }
                        else -> _uiState.update { it.copy(isLoading = false) }
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to verify user profile: ${userResult.message}") }
                }
                else -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun constructOrderDto(userId: Int, username: String): OrderDTO {
        val itemsDto = _uiState.value.cartItems.map { (product, qty) ->
            OrderItemDTO(
                productId = product.id,
                productName = product.name,
                price = product.price,
                quantity = qty,
                mainImgId = product.images.firstOrNull()?.id,
                isProductDeleted = false
            )
        }

        return OrderDTO(
            userId = userId,
            userName = username,
            paymentMethod = _uiState.value.selectedPaymentMethod,
            notes = _uiState.value.notes,
            items = itemsDto
        )
    }

    fun resetState() {
        _uiState.value = CheckoutUiState()
    }
}