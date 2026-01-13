package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductImageDTO(
    val id: Int? = null,
    val url: String,
    val mimeType: String
)
