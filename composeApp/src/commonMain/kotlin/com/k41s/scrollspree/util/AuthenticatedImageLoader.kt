package com.k41s.scrollspree.util

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.k41s.scrollspree.data.local.TokenManager

class AuthenticatedImageLoader(
    private val tokenManager: TokenManager,
    private val platformContext: PlatformContext
) {
    fun getLoader(): ImageLoader =
        ImageLoader.Builder(platformContext)
            .components {
                add(KtorNetworkFetcherFactory())
                add { chain ->
                    val tokenValue = tokenManager.token.value

                    val newRequest = chain.request.newBuilder()
                        .apply {
                            if (tokenValue != null) {
                                val authHeaders = NetworkHeaders.Builder()
                                    .add("Authorization", "Bearer $tokenValue")
                                    .build()

                                httpHeaders(authHeaders)
                            }
                        }
                        .build()

                    chain.withRequest(newRequest).proceed()
                }
            }
            .crossfade(true)
            .build()
}