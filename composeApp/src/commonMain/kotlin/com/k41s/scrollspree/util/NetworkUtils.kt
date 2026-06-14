package com.k41s.scrollspree.util

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

fun HttpClient.clearAuthCache() =
    this.authProvider<BearerAuthProvider>()?.clearToken()