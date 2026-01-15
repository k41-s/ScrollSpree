package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.dto.CategoryDTO
import com.k41s.scrollspree.data.remote.network.CategoryApiService
import com.k41s.scrollspree.domain.model.Category
import com.k41s.scrollspree.util.NetworkResult

class CategoryRepository (
    private val apiService: CategoryApiService
) : BaseRepository() {

    suspend fun getAll(): NetworkResult<List<Category>> =
        safeApiCall {
            apiService.getAll().map { it.toDomain() }
        }

    suspend fun getById(id: Int): NetworkResult<Category> =
        safeApiCall {
            apiService.getById(id).toDomain()
        }

    suspend fun create(name: String): NetworkResult<Category> =
        safeApiCall {
            val requestDto = CategoryDTO(name = name)
            apiService.create(requestDto).toDomain()
        }

    suspend fun update(id: Int, name: String): NetworkResult<Boolean> =
        safeApiCall {
            val requestDto = CategoryDTO(id, name)
            apiService.update(id, requestDto)
            true
        }

    suspend fun delete(id: Int): NetworkResult<Boolean> =
        safeApiCall {
            apiService.delete(id)
            true
        }
}