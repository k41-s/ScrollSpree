package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartDTO(
    val id: Int,
    val userId: Int,
    val items: List<CartItemDTO>
)

