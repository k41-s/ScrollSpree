package com.k41s.scrollspree.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserMainViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    val isAuthenticated: StateFlow<Boolean> = authRepo.getCurrentUser()
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun showMessage(message: String) {
        viewModelScope.launch { _snackbarMessage.emit(message) }
    }

    fun onLogoutClicked() =
        viewModelScope.launch {
            authRepo.logout()
        }
}