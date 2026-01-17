package com.k41s.scrollspree.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel (
    private val repository: AuthRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<AppViewState>(AppViewState.Loading)
    val viewState = _viewState.asStateFlow()

    init {
        observeAuthStatus()
    }

    private fun observeAuthStatus() {
        viewModelScope.launch {
            repository.getCurrentUser().collect { user ->
                if (user != null) {
                    if (user.role == Role.ADMIN)
                        _viewState.value = AppViewState.AdminAuthenticated
                    else
                        _viewState.value = AppViewState.UserAuthenticated
                } else {
                    _viewState.value = AppViewState.Unauthorized
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}