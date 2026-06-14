package com.k41s.scrollspree.data.remote.network

import com.k41s.scrollspree.data.remote.dto.ChangePasswordDTO
import com.k41s.scrollspree.data.remote.dto.UserDTO
import com.k41s.scrollspree.data.remote.dto.UserWithOrdersDTO
import com.k41s.scrollspree.util.clearAuthCache
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

private const val BASE_URL = "api/users"

class UserApiService(private val client: HttpClient) {

    suspend fun getAll(): List<UserDTO> =
        client.get(BASE_URL).body()

    suspend fun getById(id: Int): UserDTO =
        client.get("$BASE_URL/$id").body()

    suspend fun getByEmail(email: String): UserDTO =
        client.get("$BASE_URL/email/$email").body()

    suspend fun getUsersWithOrders(): List<UserWithOrdersDTO> =
        client.get("$BASE_URL/with-orders").body()

    suspend fun update(id: Int, dto: UserDTO): HttpResponse =
        client.put("$BASE_URL/$id") {
            setBody(dto)
        }

    suspend fun updateProfileByEmail(email: String, dto: UserDTO): HttpResponse =
        client.put("$BASE_URL/profile/$email") {
            setBody(dto)
        }

    suspend fun changePassword(dto: ChangePasswordDTO): HttpResponse =
        client.post("$BASE_URL/change-password") {
            setBody(dto)
        }

    suspend fun delete(id: Int): HttpResponse {
        val response = client.delete("$BASE_URL/$id")

        if (response.status.isSuccess()) {
            client.clearAuthCache()
        }

        return response
    }

    suspend fun deleteMyProfile(): HttpResponse {
        val response = client.delete("$BASE_URL/profile")

        if (response.status.isSuccess()) {
            client.clearAuthCache()
        }

        return response
    }

    fun flushTokens() {
        client.clearAuthCache()
    }
}