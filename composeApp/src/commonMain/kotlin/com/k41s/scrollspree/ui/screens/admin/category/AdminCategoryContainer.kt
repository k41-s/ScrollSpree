package com.k41s.scrollspree.ui.screens.admin.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminCategoryContainer() {

    val viewModel: AdminCategoryViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is AdminCategoryUiState.Loading -> {
            BasicLoadingScreen()
        }
        is AdminCategoryUiState.Error -> {
            ErrorScreen(
                message = state.message,
                onRetry = { viewModel.loadCategories() }
            )
        }
        is AdminCategoryUiState.Success -> {
            AdminCategoryScreen(
                categories = state.categories,
                onAddCategory = { name -> viewModel.createCategory(name) },
                onUpdateCategory = { id, name -> viewModel.updateCategory(id, name) },
                onDeleteCategory = { id -> viewModel.deleteCategory(id) }
            )
        }
    }
}