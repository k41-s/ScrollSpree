package com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminOrdersViewModel(

) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminOrdersUiState>(AdminOrdersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {

    }
}