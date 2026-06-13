package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.util.NetworkResult
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

open class BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(apiCall())
            } catch (e: ClientRequestException) {
                var errorMessage = "Request Error: ${e.response.status.value}"

                try {
                    val errorString = e.response.bodyAsText()
                    val jsonError = Json.parseToJsonElement(errorString).jsonObject
                    val backendMessage = jsonError["message"]?.jsonPrimitive?.content

                    if (backendMessage != null) {
                        errorMessage = backendMessage
                    }
                } catch (parseException: Exception) {
                    // Ignore JSON parse errors and just use the fallback message
                }

                NetworkResult.Error(
                    errorMessage,
                    e.response.status.value
                )
            } catch (e: ServerResponseException) {
                NetworkResult.Error(
                    "Server is currently unavailable",
                    e.response.status.value
                )
            } catch (e: Exception) {
                NetworkResult.Error(e.message ?: "An unexpected error occurred")
            }
        }
}