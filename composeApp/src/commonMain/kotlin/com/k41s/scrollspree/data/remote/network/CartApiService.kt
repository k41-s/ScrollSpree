package com.k41s.scrollspree.data.remote.network

import com.k41s.scrollspree.data.remote.dto.CartDTO
import com.k41s.scrollspree.data.remote.dto.CartItemRequestDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CartApiService(private val client: HttpClient) {

    suspend fun getCart(): CartDTO {
        return client.get("/api/cart").body()
    }

    suspend fun addItem(request: CartItemRequestDTO): CartDTO {
        return client.post("/api/cart/items") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateItemQuantity(productId: Int, request: CartItemRequestDTO): CartDTO {
        return client.put("/api/cart/items/$productId") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun removeItem(productId: Int): CartDTO {
        return client.delete("/api/cart/items/$productId").body()
    }

    suspend fun clearCart(): CartDTO {
        return client.delete("/api/cart").body()
    }
}