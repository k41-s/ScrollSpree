package com.k41s.scrollspree.ui.screens.auth.login

import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.domain.model.enums.Role

data class LoginUiState (
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val userRole: Role? = null
)

fun LoginUiState.toDto(): LoginDTO =
    LoginDTO(
        username,
        password
    )