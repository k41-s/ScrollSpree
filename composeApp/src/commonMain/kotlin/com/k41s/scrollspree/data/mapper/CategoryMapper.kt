package com.k41s.scrollspree.data.mapper

import com.k41s.scrollspree.data.remote.dto.CategoryDTO
import com.k41s.scrollspree.domain.model.Category

fun CategoryDTO.toDomain(): Category =
    Category(
        id = id ?: -1,
        name = name
    )