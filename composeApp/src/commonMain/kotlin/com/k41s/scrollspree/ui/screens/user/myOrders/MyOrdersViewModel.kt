package com.k41s.scrollspree.ui.screens.user.myOrders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.repository.OrderRepository
import com.k41s.scrollspree.data.repository.UserRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyOrdersViewModel(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyOrdersUiState>(MyOrdersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = MyOrdersUiState.Loading

            val email = tokenManager.email.first()
            if (email == null) {
                _uiState.value = MyOrdersUiState.Error("User session not found.")
                return@launch
            }

            val userResult = userRepository.getByEmail(email)
            if (userResult is NetworkResult.Success) {
                val userId = userResult.data.id

                if (userId != null) {
                    when (val orderResult = orderRepository.getUserOrders(userId)) {
                        is NetworkResult.Success -> {
                            val sortedOrders = orderResult.data.sortedByDescending { it.orderedAt }
                            _uiState.value = MyOrdersUiState.Success(sortedOrders)
                        }

                        is NetworkResult.Error -> {
                            _uiState.value = MyOrdersUiState.Error(orderResult.message)
                        }
                        else -> { /* UI state loading by default */ }
                    }
                } else {
                    _uiState.value = MyOrdersUiState.Error("User ID is missing. Please re-login.")
                }
            } else {
                val error = (userResult as? NetworkResult.Error)?.message ?: "Failed to load user profile"
                _uiState.value = MyOrdersUiState.Error(error)
            }
        }
    }

    fun searchOrdersByDateRange(startDate: String, endDate: String) {
        viewModelScope.launch {
            _uiState.value = MyOrdersUiState.Loading

            val email = tokenManager.email.first()
            if (email == null) {
                _uiState.value = MyOrdersUiState.Error("User session not found.")
                return@launch
            }

            val userResult = userRepository.getByEmail(email)
            if (userResult is NetworkResult.Success) {
                val userId = userResult.data.id

                if (userId != null) {
                    val isoStart = "${startDate}T00:00:00"
                    val isoEnd = "${endDate}T23:59:59"

                    when (val orderResult = orderRepository.getUserOrdersByDateRange(userId, isoStart, isoEnd)) {
                        is NetworkResult.Success -> {
                            val sortedOrders = orderResult.data.sortedByDescending { it.orderedAt }
                            _uiState.value = MyOrdersUiState.Success(sortedOrders)
                        }
                        is NetworkResult.Error -> {
                            _uiState.value = MyOrdersUiState.Error(orderResult.message)
                        }
                        else -> {}
                    }
                } else {
                    _uiState.value = MyOrdersUiState.Error("User ID is missing.")
                }
            } else {
                _uiState.value = MyOrdersUiState.Error("Failed to load user profile")
            }
        }
    }
}