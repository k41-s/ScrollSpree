package com.k41s.scrollspree.ui.screens.user.mainTabs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.ProductRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserHomeViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserHomeUiState>(UserHomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = UserHomeUiState.Loading

            val result = repository.getAll()

            _uiState.value = when (result) {
                is NetworkResult.Success -> {
                    UserHomeUiState.Success(result.data.products)
                }
                is NetworkResult.Error -> {
                    UserHomeUiState.Error(result.message)
                }
                is NetworkResult.Loading -> UserHomeUiState.Loading
            }
        }
    }
}