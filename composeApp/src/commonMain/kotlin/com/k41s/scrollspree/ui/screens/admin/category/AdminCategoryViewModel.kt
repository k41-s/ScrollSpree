package com.k41s.scrollspree.ui.screens.admin.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.CategoryRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminCategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminCategoryUiState>(AdminCategoryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = AdminCategoryUiState.Loading

            val result = repository.getAll()

            _uiState.value = when (result) {
                is NetworkResult.Success -> {
                    AdminCategoryUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    AdminCategoryUiState.Error(result.message)
                }
                is NetworkResult.Loading -> AdminCategoryUiState.Loading
            }
        }
    }

    fun createCategory(name: String) {
        // This gave 403 error, check it
        viewModelScope.launch {
            when (val result = repository.create(name)) {
                is NetworkResult.Success -> {
                    loadCategories()
                }
                is NetworkResult.Error -> {
                    println("Create failed: ${result.message}")
                }
                else -> {}
            }
        }
    }

    fun updateCategory(id: Int, name: String) {
        viewModelScope.launch {
            when (val result = repository.update(id, name)) {
                is NetworkResult.Success -> {
                    loadCategories()
                }
                is NetworkResult.Error -> {
                    println("Update failed: ${result.message}")
                }
                else -> {}
            }
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            when (val result = repository.delete(id)) {
                is NetworkResult.Success -> {
                    loadCategories()
                }
                is NetworkResult.Error -> {
                    println("Delete failed: ${result.message}")
                }
                else -> {}
            }
        }
    }
}