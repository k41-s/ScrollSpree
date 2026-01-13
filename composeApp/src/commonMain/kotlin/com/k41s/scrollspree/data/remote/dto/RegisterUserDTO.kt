package com.k41s.scrollspree.data.remote.dto

import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDTO(
     val username: String,
     val password: String,
     val name: String,
     val surname: String,
     val role: Role,
     val email: String,
     val phone: String
)

