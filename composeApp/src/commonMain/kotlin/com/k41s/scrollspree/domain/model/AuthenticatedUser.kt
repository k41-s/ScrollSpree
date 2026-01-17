package com.k41s.scrollspree.domain.model

import com.k41s.scrollspree.domain.model.enums.Role

data class AuthenticatedUser(
    val username: String,
    val role: Role
)
