package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val id: Int? = null,
    val name: String
)
