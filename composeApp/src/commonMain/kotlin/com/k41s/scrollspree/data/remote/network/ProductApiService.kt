package com.k41s.scrollspree.data.remote.network

import com.k41s.scrollspree.data.remote.dto.ProductDTO
import com.k41s.scrollspree.data.remote.dto.ProductPageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

private const val BASE_URL = "api/products"

class ProductApiService(private val client: HttpClient) {

    suspend fun getAll(
        search: String? = null,
        categoryId: Int? = null,
        page: Int = 0,
        size: Int = 10,
        sortBy: String = "id",
        direction: String = "asc"
    ): ProductPageResponse
    = client.get(BASE_URL) {
        url {
            search?.let { parameters.append("search", it) }
            categoryId?.let { parameters.append("categoryId", it.toString()) }
            parameters.append("page", page.toString())
            parameters.append("size", size.toString())
            parameters.append("sortBy", sortBy)
            parameters.append("direction", direction)
        }
    }.body()

    suspend fun getById(id: Int): ProductDTO
    = client.get("$BASE_URL/$id").body()

    suspend fun create(dto: ProductDTO): ProductDTO
    = client.post(BASE_URL) {
        setBody(dto)
    }.body()

    suspend fun update(id: Int, dto: ProductDTO): HttpResponse
    = client.put("$BASE_URL/$id") {
        setBody(dto)
    }

    suspend fun delete(id: Int): HttpResponse
    = client.delete("$BASE_URL/$id")

}