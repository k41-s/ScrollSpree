package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.data.remote.dto.RegisterUserDTO
import com.k41s.scrollspree.data.remote.network.AuthApiService
import com.k41s.scrollspree.domain.model.AuthenticatedUser
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.util.NetworkResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.json.Json
import kotlin.io.encoding.ExperimentalEncodingApi

class AuthRepository (
    private val apiService: AuthApiService,
    private val tokenManager: TokenManager,
    private val json: Json
) : BaseRepository() {

    @OptIn(ExperimentalEncodingApi::class)
    fun getCurrentUser(): Flow<AuthenticatedUser?> =
        combine(
            tokenManager.token,
            tokenManager.role,
            tokenManager.username
        ) { token, role, username ->
            if (token != null && role != null && username != null) {
                AuthenticatedUser(
                    username = username,
                    role = role
                )
            } else {
                null
            }
        }

    suspend fun login(request: LoginDTO): NetworkResult<User> =
        safeApiCall {
            val responseDto = apiService.login(request)
            tokenManager.saveAuthData(
                responseDto.token,
                responseDto.role,
                responseDto.username
            )
            responseDto.toDomain()
        }

    suspend fun register(request: RegisterUserDTO): NetworkResult<User> =
        safeApiCall {
            val responseDto = apiService.register(request)
            tokenManager.saveAuthData(
                responseDto.token,
                responseDto.role,
                responseDto.username
            )
            responseDto.toDomain()
        }

    suspend fun logout() =
        tokenManager.clearAuthData()
}
