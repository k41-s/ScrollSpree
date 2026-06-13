package com.k41s.scrollspree.data.remote.dto

import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatedUserDTO(
    val token: String,
    val refreshToken: String,
    val username: String,
    val email: String,
    val role: Role,
    val name: String? = null,
    val surname: String? = null,
    val phone: String? = null
)
