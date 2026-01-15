package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.dto.CountryDTO
import com.k41s.scrollspree.data.remote.network.CountryApiService
import com.k41s.scrollspree.domain.model.Country
import com.k41s.scrollspree.util.NetworkResult

class CountryRepository (
    private val apiService: CountryApiService
) : BaseRepository() {

    suspend fun getAll(): NetworkResult<List<Country>> =
        safeApiCall {
            apiService.getAll().map { it.toDomain() }
        }

    suspend fun getById(id: Int): NetworkResult<Country> =
        safeApiCall {
            apiService.getById(id).toDomain()
        }

    suspend fun create(name: String): NetworkResult<Country> =
        safeApiCall {
            val requestDto = CountryDTO(name = name)
            apiService.create(requestDto).toDomain()
        }

    suspend fun update(id: Int, name: String): NetworkResult<Boolean> =
        safeApiCall {
            val requestDto = CountryDTO(id, name)
            apiService.update(id, requestDto)
            true
        }

    suspend fun delete(id: Int): NetworkResult<Boolean> =
        safeApiCall {
            apiService.delete(id)
            true
        }

}