package com.k41s.scrollspree.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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
            val userDeferred = async { repository.getCurrentUser().first() }
            val timerDeferred = async { delay(2000) }

            val user = userDeferred.await()
            timerDeferred.await()

            if (user != null) {
                _viewState.value = if (user.role == Role.ADMIN)
                    AppViewState.AdminAuthenticated else AppViewState.UserAuthenticated
            } else {
                _viewState.value = AppViewState.Unauthorized
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}