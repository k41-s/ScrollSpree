package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.util.API_URL
import com.k41s.scrollspree.data.remote.dto.AuthenticatedUserDTO
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.data.remote.dto.RegisterUserDTO
import com.k41s.scrollspree.data.remote.network.AuthApiService
import com.k41s.scrollspree.domain.model.AuthenticatedUser
import com.k41s.scrollspree.domain.model.User
import com.k41s.scrollspree.util.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
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
            // We use a clean client to ensure no token is sent
            val cleanClient = HttpClient {
                install(ContentNegotiation) {
                    json(json)
                }
            }
            try {
                val responseDto = cleanClient.post("$API_URL/api/auth/login") {
                    header(HttpHeaders.ContentType, io.ktor.http.ContentType.Application.Json)
                    setBody(request)
                    header("ngrok-skip-browser-warning", "true")
                }.body<AuthenticatedUserDTO>()

                tokenManager.saveAuthData(
                    responseDto.token,
                    responseDto.role,
                    responseDto.username,
                    responseDto.email,
                    request.password
                )
                responseDto.toDomain()
            } finally {
                cleanClient.close()
            }
        }

    suspend fun register(request: RegisterUserDTO): NetworkResult<User> =
        safeApiCall {
            val responseDto = apiService.register(request)
            tokenManager.saveAuthData(
                responseDto.token,
                responseDto.role,
                responseDto.username,
                responseDto.email,
                request.password
            )
            responseDto.toDomain()
        }

    suspend fun logout() =
        tokenManager.clearAuthData()
}
