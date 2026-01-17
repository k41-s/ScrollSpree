package com.k41s.scrollspree.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.k41s.scrollspree.data.repository.AuthRepository
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    fun onUsernameChanged(newValue: String) {
        _state.update { it.copy(username = newValue, errorMessage = null) }
    }

    fun onPasswordChanged(newValue: String) {
        _state.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun onRepeatPasswordChanged(newValue: String) {
        _state.update { it.copy(repeatPassword = newValue, errorMessage = null) }
    }

    fun onNameChanged(newValue: String) {
        _state.update { it.copy(name = newValue, errorMessage = null) }
    }

    fun onSurnameChanged(newValue: String) {
        _state.update { it.copy(surname = newValue, errorMessage = null) }
    }

    fun onEmailChanged(newValue: String) {
        _state.update { it.copy(email = newValue, errorMessage = null) }
    }

    fun onPhoneChanged(newValue: String) {
        _state.update { it.copy(phone = newValue, errorMessage = null) }
    }

    fun isFormValid(): Boolean {
        val currentState = _state.value
        return currentState.username.isNotBlank()
                && currentState.password.length >= 4
                && currentState.repeatPassword == currentState.password
                && currentState.name.isNotBlank()
                && currentState.surname.isNotBlank()
                && currentState.email.isNotBlank()
                && currentState.email.contains('@')
                && currentState.phone.isNotBlank()
                && !currentState.isLoading
    }

    fun onRegisterClicked() {

        if (!isFormValid()) {
            _state.update {
                it.copy(errorMessage = "Make sure all fields are filled in properly")
            }
            return
        }

        viewModelScope.launch {
            val currentState = _state.value

            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val result = authRepository.register(
                currentState.toDto()
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