package com.k41s.scrollspree.ui.screens.admin.product.main

import com.k41s.scrollspree.domain.model.Category
import com.k41s.scrollspree.domain.model.Product

sealed class AdminProductUiState {
    object Loading : AdminProductUiState()
    data class Success(
        val products: List<Product>,
        val categories: List<Category>,
        val isLastPage: Boolean = false
    ) : AdminProductUiState()
    data class Error(val message: String) : AdminProductUiState()
}