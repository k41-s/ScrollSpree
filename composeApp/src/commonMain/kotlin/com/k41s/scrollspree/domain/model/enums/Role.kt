package com.k41s.scrollspree.domain.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    @SerialName("Admin")
    ADMIN,

    @SerialName("User")
    USER
}
