package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDTO(
    val refreshToken: String
)