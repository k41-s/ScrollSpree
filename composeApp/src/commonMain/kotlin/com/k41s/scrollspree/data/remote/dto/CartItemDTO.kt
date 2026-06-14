package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CartItemDTO(
    val id: Int,
    val product: ProductDTO,
    val quantity: Int
)