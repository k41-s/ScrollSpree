package com.k41s.scrollspree.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val isDeleted: Boolean,
    val category: Category,
    val images: List<ProductImage>,
    val countries: List<Country>
)
