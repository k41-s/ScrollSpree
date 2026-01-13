package com.k41s.scrollspree.data.remote.dto

import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserWithOrdersDTO(
    val username: String,
    val name: String,
    val surname: String,
    val role: Role,
    val orders: List<OrderDTO>
)