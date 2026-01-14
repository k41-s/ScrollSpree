package com.k41s.scrollspree.data.mapper

import com.k41s.scrollspree.data.remote.dto.ProductDTO
import com.k41s.scrollspree.data.remote.dto.ProductImageDTO
import com.k41s.scrollspree.data.remote.dto.ProductPageResponse
import com.k41s.scrollspree.domain.model.Category
import com.k41s.scrollspree.domain.model.Country
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.domain.model.ProductImage
import com.k41s.scrollspree.domain.model.ProductPage
import com.k41s.scrollspree.util.ApiConfig

fun ProductDTO.toDomain(): Product =
    Product(
        id = id ?: -1,
        name = name,
        description = description,
        isDeleted = isDeleted,
        category = Category(
            id = categoryId ?: -1,
            name = categoryName
        ),
        images = imageIds.map { id ->
            ProductImage(
                id = id,
                url = "${ApiConfig.IMAGE_ENDPOINT}/$id"
            )
        },
        countries = countryIds.mapIndexed { index, id ->
            Country(
                id = id,
                name = countryNames.getOrNull(index) ?: "Unknown"
            )
        }
    )

fun ProductImageDTO.toDomain(): ProductImage =
    ProductImage(
        id = id ?: -1,
        url = url
    )

fun ProductPageResponse.toDomain(): ProductPage {
    return ProductPage(
        products = content.map { it.toDomain() },
        isLastPage = last,
        totalCount = totalElements,
        currentPage = number
    )
}