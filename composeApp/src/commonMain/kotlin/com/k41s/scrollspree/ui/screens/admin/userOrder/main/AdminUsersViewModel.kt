package com.k41s.scrollspree.ui.screens.admin.userOrder.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.UserRepository
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminUsersViewModel(
    private val userRepo: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminUsersUiState>(AdminUsersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = AdminUsersUiState.Loading

            val result = userRepo.getUsersWithOrders()
            _uiState.value = when (result) {
                is NetworkResult.Success -> AdminUsersUiState.Success(result.data)
                is NetworkResult.Error -> AdminUsersUiState.Error(result.message)
                is NetworkResult.Loading -> AdminUsersUiState.Loading
            }
        }
    }
}