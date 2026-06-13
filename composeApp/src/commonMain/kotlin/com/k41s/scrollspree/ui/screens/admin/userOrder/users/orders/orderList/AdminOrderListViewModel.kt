package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.orderList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.OrderRepository
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminOrderListViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminOrderListUiState>(AdminOrderListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun initInitialOrders(orders: List<Order>) {
        _uiState.value = AdminOrderListUiState.Success(orders)
    }

    fun searchOrdersByDateRange(userId: Int, startDate: String, endDate: String) {
        viewModelScope.launch {
            _uiState.value = AdminOrderListUiState.Loading

            val isoStart = "${startDate}T00:00:00"
            val isoEnd = "${endDate}T23:59:59"

            when (val result = orderRepository.getUserOrdersByDateRange(userId, isoStart, isoEnd)) {
                is NetworkResult.Success -> {
                    _uiState.value = AdminOrderListUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = AdminOrderListUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }
}