package com.k41s.scrollspree.ui.screens.user.productDetail

import com.k41s.scrollspree.domain.model.Product

sealed class ProductDetailUiState {
    data object Loading: ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}