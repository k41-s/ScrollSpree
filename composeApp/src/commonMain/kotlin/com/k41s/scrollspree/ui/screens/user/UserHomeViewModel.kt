package com.k41s.scrollspree.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.AuthRepository
import kotlinx.coroutines.launch

class UserHomeViewModel(
    private val authRepo: AuthRepository
) : ViewModel() {

    fun onLogoutClicked() =
        viewModelScope.launch {
            authRepo.logout()
        }
}