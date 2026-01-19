package com.k41s.scrollspree.ui.screens.admin.category

import com.k41s.scrollspree.domain.model.Category

sealed class AdminCategoryUiState {
    data object Loading : AdminCategoryUiState()
    data class Success(val categories: List<Category>) : AdminCategoryUiState()
    data class Error(val message: String) : AdminCategoryUiState()
}