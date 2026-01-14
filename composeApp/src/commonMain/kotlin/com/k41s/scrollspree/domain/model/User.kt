package com.k41s.scrollspree.domain.model

import com.k41s.scrollspree.domain.model.enums.Role

data class User(
    val id: Int? = null,
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val role: Role,
    val token: String? = null,
    val orders: List<Order>? = null
) {
    val fullName: String get() = "$firstName $lastName"
}
