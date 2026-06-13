package com.k41s.scrollspree.data.remote.dto

import com.k41s.scrollspree.domain.model.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserWithOrdersDTO(
    val id: Int,
    val username: String,
    val email: String,
    val name: String,
    val surname: String,
    val phone: String,
    val role: Role,
    val orders: List<OrderDTO>
)