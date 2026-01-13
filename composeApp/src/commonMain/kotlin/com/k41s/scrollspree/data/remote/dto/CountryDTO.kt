package com.k41s.scrollspree.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CountryDTO(
    var id: Int? = null,
    var name: String
)
