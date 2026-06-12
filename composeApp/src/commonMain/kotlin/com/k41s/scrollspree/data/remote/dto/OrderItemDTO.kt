package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDTO(
    val productId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val mainImgId: Int? = null,
    val isProductDeleted: Boolean = false
)