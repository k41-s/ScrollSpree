package com.k41s.scrollspree.data.remote.network

import com.k41s.scrollspree.data.remote.dto.CountryDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

private const val BASE_URL = "api/countries"

class CountryApiService(private val client: HttpClient) {

    suspend fun getAll(): List<CountryDTO> =
        client.get(BASE_URL).body()

    suspend fun getById(id: Int): CountryDTO =
        client.get("$BASE_URL/$id").body()

    suspend fun create(dto: CountryDTO): CountryDTO =
        client.post(BASE_URL) {
            setBody(dto)
        }.body()

    suspend fun update(id: Int, dto: CountryDTO): HttpResponse =
        client.put("$BASE_URL/$id") {
            setBody(dto)
        }

    suspend fun delete(id: Int): HttpResponse =
        client.delete("$BASE_URL/$id")

}