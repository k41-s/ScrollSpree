package com.k41s.scrollspree.domain.model

data class ProductPage(
    val products: List<Product>,
    val isLastPage: Boolean,
    val totalCount: Long,
    val currentPage: Int
)
