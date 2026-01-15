package com.k41s.scrollspree.di

import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.configureSharedClient
import com.k41s.scrollspree.data.remote.network.*
import com.k41s.scrollspree.data.repository.*
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

    single { AuthRepository(get(), get()) }
    single { CategoryRepository(get()) }
    single { CountryRepository(get()) }
    single { OrderRepository(get()) }
    single { ProductImageRepository(get()) }
    single { ProductRepository(get()) }
    single { UserRepository(get()) }
}

expect val platformModule: Module