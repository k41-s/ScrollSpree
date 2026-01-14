package com.k41s.scrollspree.domain.model

data class OrderProduct(
    val id: Int,
    val name: String,
    val isDeleted: Boolean,
    val mainImageId: Int?
)
