package com.k41s.scrollspree.data.remote.network

import com.k41s.scrollspree.data.remote.dto.ProductImageDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

private const val BASE_URL = "api/productimages"

class ProductImageApiService(private val client: HttpClient) {

    suspend fun getProductImage(id: Int): ByteArray
    = client.get("$BASE_URL/$id").body()

    suspend fun uploadImage(productId: Int, fileBytes: ByteArray, fileName: String): ProductImageDTO
    = client.submitFormWithBinaryData(
        url = "$BASE_URL/upload/$productId",
        formData = formData {
            append("file", fileBytes, Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
            })
        }
    ).body()

    suspend fun deleteProductImage(id: Int): HttpResponse
    = client.delete("$BASE_URL/$id")

}