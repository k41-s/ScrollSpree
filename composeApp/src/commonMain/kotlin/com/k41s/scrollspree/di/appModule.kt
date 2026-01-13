package com.k41s.scrollspree.di

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.configureSharedClient
import com.k41s.scrollspree.data.remote.network.AuthApiService
import com.k41s.scrollspree.data.remote.network.CategoryApiService
import com.k41s.scrollspree.data.remote.network.CountryApiService
import com.k41s.scrollspree.data.remote.network.OrderApiService
import com.k41s.scrollspree.data.remote.network.ProductApiService
import com.k41s.scrollspree.data.remote.network.ProductImageApiService
import com.k41s.scrollspree.data.remote.network.UserApiService
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {

    single { TokenManager(get()) }

    single {
        HttpClient {
            configureSharedClient(get())
        }
    }

    single { AuthApiService(get()) }
    single { CategoryApiService(get()) }
    single { CountryApiService(get()) }
    single { OrderApiService(get()) }
    single { ProductApiService(get()) }
    single { ProductImageApiService(get()) }
    single { UserApiService(get()) }
}

expect val platformModule: Module