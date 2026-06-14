package com.k41s.scrollspree.ui.screens.user.myOrders.orderDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.repository.OrderRepository
import com.k41s.scrollspree.data.repository.UserRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState.asStateFlow()

    fun loadOrder(orderId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val email = tokenManager.email.first()
                if (email != null) {
                    val userResult = userRepository.getByEmail(email)
                    if (userResult is NetworkResult.Success) {
                        val userId = userResult.data.id
                        if (userId != null) {
                            when (val result = orderRepository.getUserOrders(userId)) {
                                is NetworkResult.Success -> {
                                    val targetOrder = result.data.find { it.id == orderId }
                                    if (targetOrder != null) {
                                        _uiState.update { it.copy(isLoading = false, order = targetOrder) }
                                    } else {
                                        _uiState.update { it.copy(isLoading = false, errorMessage = "Order not found") }
                                    }
                                }
                                is NetworkResult.Error -> {
                                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                                }
                                else -> {
                                    _uiState.update { it.copy(isLoading = false, errorMessage = "An unknown error occurred.") }
                                }
                            }
                        } else {
                            _uiState.update { it.copy(isLoading = false, errorMessage = "User ID missing") }
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load user profile") }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "User not authenticated") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}