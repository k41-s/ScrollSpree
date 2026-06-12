package com.k41s.scrollspree.domain.model

data class OrderItem(
    val productId: Int,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val isDeleted: Boolean,
    val mainImageId: Int?
)
