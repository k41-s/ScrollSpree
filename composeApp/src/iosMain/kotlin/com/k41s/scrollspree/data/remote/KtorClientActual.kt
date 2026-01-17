package com.k41s.scrollspree.data.remote

import com.k41s.scrollspree.data.local.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun createHttpClient(tokenManager: TokenManager): HttpClient =
    HttpClient(Darwin) {
        configureSharedClient(tokenManager)

        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }
    }