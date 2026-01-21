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
        price = price,
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

fun Product.toDto(): ProductDTO {
    return ProductDTO(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        categoryId = this.category.id,
        categoryName = this.category.name,
        isDeleted = this.isDeleted,
        imageIds = this.images.map { it.id },
        countryIds = this.countries.map { it.id },
        countryNames = this.countries.map { it.name }
    )
}

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