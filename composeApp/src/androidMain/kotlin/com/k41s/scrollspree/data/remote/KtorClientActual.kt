package com.k41s.scrollspree.data.remote

import com.k41s.scrollspree.data.local.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

actual fun createHttpClient(tokenManager: TokenManager): HttpClient =
    HttpClient(Android) {
        configureSharedClient(tokenManager)

        engine {
            connectTimeout = 10_000
            socketTimeout = 10_000
        }
    }