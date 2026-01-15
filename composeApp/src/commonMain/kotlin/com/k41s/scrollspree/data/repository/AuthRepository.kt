package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.data.remote.dto.RegisterUserDTO
import com.k41s.scrollspree.data.remote.network.AuthApiService
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.util.NetworkResult

class AuthRepository (
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager
) : BaseRepository() {

    suspend fun login(request: LoginDTO): NetworkResult<User> =
        safeApiCall {
            val responseDto = apiService.login(request)
            tokenManager.saveToken(responseDto.token)
            responseDto.toDomain()
        }

    suspend fun register(request: RegisterUserDTO): NetworkResult<User> =
        safeApiCall {
            val responseDto = apiService.register(request)
            tokenManager.saveToken(responseDto.token)
            responseDto.toDomain()
        }

    suspend fun logout() =
        tokenManager.clearToken()
}