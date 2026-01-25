package com.k41s.scrollspree.data.repository

import com.k41s.scrollspree.util.NetworkResult
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

open class BaseRepository {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResult.Success(apiCall())
            } catch (e: ClientRequestException) {
                NetworkResult.Error(
                    "Request Error: ${e.response.status.value}",
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