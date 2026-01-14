package com.k41s.scrollspree.data.mapper

import com.k41s.scrollspree.data.remote.dto.CountryDTO
import com.k41s.scrollspree.domain.model.Country

fun CountryDTO.toDomain(): Country =
    Country(
        id = id ?: -1,
        name = name
    )