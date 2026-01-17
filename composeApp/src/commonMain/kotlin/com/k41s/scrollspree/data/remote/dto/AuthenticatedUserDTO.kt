package com.k41s.scrollspree.data.remote.dto

import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatedUserDTO(
    val token: String,
    val username: String,
    val email: String,
    val role: Role,
    val name: String?,
    val surname: String?,
    val phone: String?
)
