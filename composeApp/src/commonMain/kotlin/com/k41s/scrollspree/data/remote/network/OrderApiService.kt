package com.k41s.scrollspree.data.remote.network

import com.k41s.scrollspree.data.remote.dto.OrderDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

private const val BASE_URL = "api/orders"

class OrderApiService(private val client: HttpClient) {

    suspend fun getAll(): List<OrderDTO> =
        client.get(BASE_URL).body()

    suspend fun getUserOrders(id: Int): List<OrderDTO> =
        client.get("$BASE_URL/$id").body()

    suspend fun create(dto: OrderDTO): OrderDTO =
        client.post(BASE_URL) {
            setBody(dto)
        }.body()

}