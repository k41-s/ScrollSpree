package com.k41s.scrollspree.ui.screens.admin.userOrder.users

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

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = AdminUsersUiState.Loading

            when (val result = userRepo.getUsersWithOrders()) {
                is NetworkResult.Success -> {
                    _allUsers.value = result.data
                    filterUsers()
                }
                is NetworkResult.Error -> AdminUsersUiState.Error(result.message)
                is NetworkResult.Loading -> AdminUsersUiState.Loading
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterUsers()
    }

    private fun filterUsers() {
        val query = _searchQuery.value.trim().lowercase()
        if (query.isEmpty()) {
            _uiState.value = AdminUsersUiState.Success(_allUsers.value)
        } else {
            val filteredList = _allUsers.value.filter {
                it.fullName.lowercase().contains(query) ||
                        it.email.lowercase().contains(query)
            }
            _uiState.value = AdminUsersUiState.Success(filteredList)
        }
    }
}