package com.k41s.scrollspree.data.remote

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.dto.AuthenticatedUserDTO
import com.k41s.scrollspree.data.remote.dto.LoginDTO
import com.k41s.scrollspree.data.remote.dto.RefreshTokenRequestDTO
import com.k41s.scrollspree.util.API_URL
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
                val currentToken = tokenManager.token.value
                val currentRefreshToken = tokenManager.refreshToken.first()

                if (!currentToken.isNullOrBlank()) {
                    BearerTokens(
                        accessToken = currentToken,
                        refreshToken = currentRefreshToken
                    )
                } else null
            }

            refreshTokens {
                refreshTokensFlow(tokenManager)
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

private suspend fun refreshTokensFlow(tokenManager: TokenManager): BearerTokens? {
    val currentRefreshToken = tokenManager.refreshToken.first()

    if (currentRefreshToken.isNullOrBlank()) return null

    val refreshClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    return try {
        val response = refreshClient.post("${API_URL}/api/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequestDTO(currentRefreshToken))
        }

        if (response.status == HttpStatusCode.OK) {
            val authDto = response.body<AuthenticatedUserDTO>()

            tokenManager.saveAuthData(
                token = authDto.accessToken,
                refreshToken = authDto.refreshToken,
                role = authDto.role,
                username = authDto.username,
                email = authDto.email
            )

            BearerTokens(
                accessToken = authDto.accessToken,
                refreshToken = authDto.refreshToken
            )
        } else {
            tokenManager.clearAuthData()
            null
        }
    } catch (e: Exception) {
        println("Token Refresh Error: ${e.message}")
        null
    } finally {
        refreshClient.close()
    }
}

expect fun createHttpClient(tokenManager: TokenManager): HttpClient