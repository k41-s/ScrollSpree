package com.k41s.scrollspree.data.remote

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.dto.AuthenticatedUserDTO
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

const val API_URL = "https://conformal-eula-nonapostolically.ngrok-free.dev"

fun HttpClientConfig<*>.configureSharedClient(tokenManager: TokenManager) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        })
    }

    install(DefaultRequest) {
        url(API_URL)
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header("ngrok-skip-browser-warning", "true")
    }

    install(Auth) {
        bearer {
            loadTokens {
                val token = tokenManager.token.value
                if (!token.isNullOrBlank()) {
                    BearerTokens(accessToken = token, refreshToken = "")
                } else null
            }

            refreshTokens {
                refreshTokens(tokenManager)
            }

            sendWithoutRequest { request ->
                !request.url.encodedPath.contains("/api/auth/")
            }
        }
    }

    expectSuccess = true

    install(HttpCallValidator) {
        handleResponseExceptionWithRequest { cause, _ ->
            println("Network Error: ${cause.message}")
            cause.printStackTrace()
        }
    }
}

private suspend fun refreshTokens(tokenManager: TokenManager): BearerTokens? {
    val username = tokenManager.username.first()
    val password = tokenManager.password.first()

    return if (username != null && password != null) {
        val refreshClient = HttpClient {
            install(ContentNegotiation) { json() }
        }

        try {
            val response = refreshClient.post("$API_URL/api/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginDTO(username, password))
            }

            if (response.status == HttpStatusCode.OK) {
                val authDto = response.body<AuthenticatedUserDTO>()
                tokenManager.saveAuthData(
                    authDto.token,
                    authDto.role,
                    username,
                    password
                )
                BearerTokens(accessToken = authDto.token, refreshToken = "")
            } else null
        } catch (e: Exception) {
            null
        } finally {
            refreshClient.close()
        }
    } else null
}

expect fun createHttpClient(tokenManager: TokenManager): HttpClient