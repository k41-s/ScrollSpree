package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemRequestDTO(
    val productId: Int,
    val quantity: Int
)