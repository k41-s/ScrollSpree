package com.k41s.scrollspree.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun isFormValid(): Boolean {
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

            try {
                val result = authRepository.login(
                    currentState.toDto()
                )

                when (result) {
                    is NetworkResult.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = true,
                                userRole = result.data.role
                            )
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
                    else -> {
                        _state.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            } catch (e: Exception) {
                println("CRITICAL_NETWORK_ERROR: ${e.message}")
                _state.update {
                    it.copy(
                        errorMessage = "CRITICAL ERROR OCCURRED",
                        isSuccess = false
                    )
                }
            } finally {
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}