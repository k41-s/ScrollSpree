package com.k41s.scrollspree.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onUsernameChanged(newValue: String) {
        _state.update { it.copy(username = newValue, errorMessage = null) }
    }

    fun onPasswordChanged(newValue: String) {
        _state.update { it.copy(password = newValue, errorMessage = null) }
    }

    private fun isFormValid(): Boolean {
        val currentState = _state.value
        return currentState.username.isNotBlank()
                && currentState.password.length >= 4
                && !currentState.isLoading
    }

    fun onLoginClicked() {

        if (!isFormValid()) return

        viewModelScope.launch {
            val currentState = _state.value

            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val result = authRepository.login(
                LoginDTO(currentState.username, currentState.password)
            )

            when (result) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(isLoading = false, isSuccess = true)
                    }
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            isSuccess = false
                        )
                    }
                }
                else -> {}
            }
        }

    }


}