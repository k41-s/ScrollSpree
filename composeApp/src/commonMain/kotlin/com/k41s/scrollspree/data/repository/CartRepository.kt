package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.data.mapper.mapDtoToState
import com.k41s.scrollspree.data.remote.dto.CartItemRequestDTO
import com.k41s.scrollspree.data.remote.network.CartApiService
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.util.NetworkResult

class CartRepository(
    private val apiService: CartApiService
) : BaseRepository() {

    suspend fun getCart(): NetworkResult<Map<Product, Int>> =
        safeApiCall {
            val response = apiService.getCart()
            mapDtoToState(response)
        }

    suspend fun addToCart(productId: Int, quantity: Int): NetworkResult<Map<Product, Int>> =
        safeApiCall {
            val request = CartItemRequestDTO(productId, quantity)
            val response = apiService.addItem(request)
            mapDtoToState(response)
        }

    suspend fun updateCartItemQuantity(productId: Int, quantity: Int): NetworkResult<Map<Product, Int>> =
        safeApiCall {
            val request = CartItemRequestDTO(productId, quantity)
            val response = apiService.updateItemQuantity(productId, request)
            mapDtoToState(response)
        }

    suspend fun removeFromCart(productId: Int): NetworkResult<Map<Product, Int>> =
        safeApiCall {
            val response = apiService.removeItem(productId)
            mapDtoToState(response)
        }

    suspend fun clearCart(): NetworkResult<Map<Product, Int>> =
        safeApiCall {
            val response = apiService.clearCart()
            mapDtoToState(response)
        }
}