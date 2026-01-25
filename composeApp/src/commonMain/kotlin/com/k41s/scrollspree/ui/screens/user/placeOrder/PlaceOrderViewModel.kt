package com.k41s.scrollspree.ui.screens.user.placeOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.dto.OrderDTO
import com.k41s.scrollspree.data.repository.OrderRepository
import com.k41s.scrollspree.data.repository.ProductRepository
import com.k41s.scrollspree.data.repository.UserRepository
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.domain.model.enums.PaymentMethod
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PlaceOrderViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceOrderUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = productRepository.getById(productId)

            _uiState.update {
                when (result) {
                    is NetworkResult.Success -> {
                        it.copy(
                            product = result.data,
                            isLoading = false
                        )
                    }
                    is NetworkResult.Error -> {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                    is NetworkResult.Loading -> {
                        it.copy(isLoading = true, errorMessage = null)
                    }
                }
            }
        }
    }

    fun onNotesChanged(newNotes: String) {
        _uiState.update { it.copy(notes = newNotes) }
    }

    fun onPaymentMethodSelected(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun submitOrder(productId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val userDeferred = async { getFullProfile() }

            val userResult = userDeferred.await()

            val product = _uiState.value.product
                ?: (productRepository.getById(productId) as? NetworkResult.Success)?.data

            if (userResult is NetworkResult.Success && product != null) {
                val orderDto = constructOrderDto(userResult.data, product)

                processOrderPlacement(orderDto)
            } else {
                handleFetchErrors(userResult)
            }
        }
    }

    private suspend fun getFullProfile() : NetworkResult<User> {
        val email = tokenManager.email.first()
            ?: return NetworkResult.Error("User email not found in session")

        return userRepository.getByEmail(email)
    }

    private fun constructOrderDto(user: User, product: Product): OrderDTO {
        return OrderDTO(
            productId = product.id,
            productName = product.name,
            userId = user.id,
            userName = user.username,
            paymentMethod = _uiState.value.selectedPaymentMethod,
            notes = _uiState.value.notes,
            mainImgId = product.images.firstOrNull()?.id,
            orderedAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    private suspend fun processOrderPlacement(dto: OrderDTO) {
        val result = orderRepository.create(dto)
        _uiState.update {
            when (result) {
                is NetworkResult.Success -> it.copy(isLoading = false, isOrderPlaced = true)
                is NetworkResult.Error -> it.copy(isLoading = false, errorMessage = result.message)
                else -> it
            }
        }
    }

    private fun handleFetchErrors(userRes: NetworkResult<User>) {
        val message = when {
            userRes is NetworkResult.Error -> userRes.message
            else -> "An unknown error occurred while preparing your order."
        }
        _uiState.update { it.copy(isLoading = false, errorMessage = message) }
    }

    fun resetState() {
        _uiState.value = PlaceOrderUiState()
    }
}