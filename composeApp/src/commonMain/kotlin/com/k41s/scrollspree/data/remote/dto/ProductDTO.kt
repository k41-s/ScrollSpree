package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    val id: Int? = null,
    val name: String,
    val description: String,
    val price: Double,
    val isDeleted: Boolean = false,
    val categoryId: Int? = null,
    val categoryName: String,
    val imageIds: List<Int>,
    val countryIds: List<Int>,
    val countryNames: List<String>
)