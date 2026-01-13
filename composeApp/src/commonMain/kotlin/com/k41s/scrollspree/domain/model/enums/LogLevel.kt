package com.k41s.scrollspree.domain.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LogLevel {
    @SerialName("Information")
    INFORMATION,

    @SerialName("Warning")
    WARNING,

    @SerialName("ERROR")
    ERROR,

    @SerialName("Debug")
    DEBUG
}
