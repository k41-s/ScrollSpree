package com.k41s.scrollspree.data.remote

import com.k41s.scrollspree.data.local.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
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
        url("https://conformal-eula-nonapostolically.ngrok-free.dev")
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header("ngrok-skip-browser-warning", "true")
    }

    install(Auth) {
        bearer {
            loadTokens {
                val token = tokenManager.token.first()
                if (token != null) {
                    BearerTokens(accessToken = token, refreshToken = "")
                } else null
            }

            sendWithoutRequest { request ->
                !request.url.encodedPath.contains("/api/auth/")
            }
        }
    }

    expectSuccess = true

//    install(Logging) {
//        level = LogLevel.INFO
//        logger = Logger.SIMPLE
//    }

    install(HttpCallValidator) {
        handleResponseExceptionWithRequest { cause, _ ->
            println("Network Error: ${cause.message}")
            cause.printStackTrace()
        }
    }
}

expect fun createHttpClient(tokenManager: TokenManager): HttpClient