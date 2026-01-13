package com.k41s.scrollspree.data.remote.dto

import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val email: String,
    val username: String,
    val name: String,
    val surname: String,
    val phone: String,
    val role: Role
)