package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.dto.ChangePasswordDTO
import com.k41s.scrollspree.data.remote.dto.UserDTO
import com.k41s.scrollspree.data.remote.network.UserApiService
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.util.NetworkResult

class UserRepository (
    private val apiService: UserApiService
) : BaseRepository() {

    suspend fun getAll(): NetworkResult<List<User>> =
        safeApiCall {
            apiService.getAll().map { it.toDomain() }
        }

    suspend fun getById(id: Int): NetworkResult<User> =
        safeApiCall {
            apiService.getById(id).toDomain()
        }

    suspend fun getByEmail(email: String): NetworkResult<User> =
        safeApiCall {
            apiService.getByEmail(email).toDomain()
        }

    suspend fun getUsersWithOrders(): NetworkResult<List<User>> =
        safeApiCall {
            apiService.getUsersWithOrders().map { it.toDomain() }
        }

    suspend fun update(id: Int, request: UserDTO): NetworkResult<Boolean> =
        safeApiCall {
            apiService.update(id, request)
            true
        }

    suspend fun updateProfileByEmail(email: String, dto: UserDTO): NetworkResult<Boolean> =
        safeApiCall {
            apiService.updateProfileByEmail(email, dto)
            true
        }

    suspend fun changePassword(dto: ChangePasswordDTO): NetworkResult<Boolean> =
        safeApiCall {
            apiService.changePassword(dto)
            true
        }

    suspend fun delete(id: Int): NetworkResult<Boolean> =
        safeApiCall {
            apiService.delete(id)
            true
        }

}