package com.k41s.scrollspree.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

actual val httpClient: HttpClient = HttpClient(Android) {
    configureSharedClient()

    engine {
        connectTimeout = 10_000
        socketTimeout = 10_000
    }
}