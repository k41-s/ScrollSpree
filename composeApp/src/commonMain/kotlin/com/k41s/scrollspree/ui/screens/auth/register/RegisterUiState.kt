package com.k41s.scrollspree.ui.screens.auth.register

import com.k41s.scrollspree.data.remote.dto.RegisterUserDTO
import com.k41s.scrollspree.domain.model.enums.Role

data class RegisterUiState (
    val username: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

fun RegisterUiState.toDto(): RegisterUserDTO =
    RegisterUserDTO(
        username,
        password,
        name,
        surname,
        Role.USER, // Admins cannot register through the mobile app
        email,
        phone
    )