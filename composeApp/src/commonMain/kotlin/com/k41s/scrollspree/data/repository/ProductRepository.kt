package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.dto.ProductDTO
import com.k41s.scrollspree.data.remote.network.ProductApiService
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.domain.model.ProductPage
import com.k41s.scrollspree.util.NetworkResult

class ProductRepository (
    private val apiService: ProductApiService
) : BaseRepository() {

    suspend fun getAll(
        search: String? = null,
        categoryId: Int? = null,
        page: Int = 0,
        size: Int = 10,
        sortBy: String = "id",
        direction: String = "asc"
    ): NetworkResult<ProductPage> =
        safeApiCall {
            apiService.getAll(
                search,
                categoryId,
                page,
                size,
                sortBy,
                direction
            ).toDomain()
        }

    suspend fun getById(id: Int): NetworkResult<Product> =
        safeApiCall {
            apiService.getById(id).toDomain()
        }

    suspend fun create(request: ProductDTO): NetworkResult<Product> =
        safeApiCall {
            apiService.create(request).toDomain()
        }

    suspend fun update(id: Int, request: ProductDTO): NetworkResult<Boolean> =
        safeApiCall {
            apiService.update(id, request)
            true
        }

    suspend fun delete(id: Int): NetworkResult<Boolean> =
        safeApiCall {
            apiService.delete(id)
            true
        }

}