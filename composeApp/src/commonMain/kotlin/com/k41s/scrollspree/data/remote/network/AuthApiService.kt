package com.k41s.scrollspree.data.remote.network

import com.k41s.scrollspree.data.remote.dto.AuthenticatedUserDTO
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.data.remote.dto.RegisterUserDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

private const val BASE_URL = "/api/auth"

class AuthApiService(private val client: HttpClient) {

    suspend fun login(request: LoginDTO) : AuthenticatedUserDTO
        = client.post("$BASE_URL/login") {
            setBody(request)
        }.body()

    suspend fun register(request: RegisterUserDTO) : AuthenticatedUserDTO
        = client.post("$BASE_URL/register") {
            setBody(request)
        }.body()

}