package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordDTO(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)
