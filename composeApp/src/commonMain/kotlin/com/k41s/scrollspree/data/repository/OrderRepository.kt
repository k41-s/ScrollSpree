package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.mapper.toDomain
import com.k41s.scrollspree.data.remote.dto.OrderDTO
import com.k41s.scrollspree.data.remote.network.OrderApiService
import com.k41s.scrollspree.domain.model.Order
import com.k41s.scrollspree.util.NetworkResult

class OrderRepository (
    private val apiService: OrderApiService
) : BaseRepository() {

    suspend fun getAll(): NetworkResult<List<Order>> =
        safeApiCall {
            apiService.getAll().map { it.toDomain() }
        }

    suspend fun getUserOrders(userId: Int): NetworkResult<List<Order>> =
        safeApiCall {
            apiService.getUserOrders(userId).map { it.toDomain() }
        }

    suspend fun create(request: OrderDTO): NetworkResult<Order> =
        safeApiCall {
            apiService.create(request).toDomain()
        }

    suspend fun getUserOrdersByDateRange(userId: Int, start: String, end: String): NetworkResult<List<Order>> =
        safeApiCall {
            apiService.getUserOrdersByDateRange(userId, start, end).map { it.toDomain() }
        }
}