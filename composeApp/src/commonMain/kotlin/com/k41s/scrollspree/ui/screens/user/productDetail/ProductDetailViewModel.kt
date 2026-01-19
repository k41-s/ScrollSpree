package com.k41s.scrollspree.ui.screens.user.productDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.ProductRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository,
    private val productId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            val result = repository.getById(productId)
            _uiState.value = when (result) {
                is NetworkResult.Success -> {
                    ProductDetailUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    ProductDetailUiState.Error(result.message)
                }
                is NetworkResult.Loading -> {
                    ProductDetailUiState.Loading
                }
            }
        }
    }
}