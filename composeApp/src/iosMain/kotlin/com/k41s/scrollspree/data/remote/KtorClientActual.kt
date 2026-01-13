package com.k41s.scrollspree.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual val httpClient = HttpClient(Darwin) {
    configureSharedClient()

    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}