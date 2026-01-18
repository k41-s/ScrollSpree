package com.k41s.scrollspree.di

import coil3.ImageLoader
import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.configureSharedClient
import com.k41s.scrollspree.data.remote.createHttpClient
import com.k41s.scrollspree.data.remote.network.*
import com.k41s.scrollspree.data.repository.*
import com.k41s.scrollspree.ui.main.MainViewModel
import com.k41s.scrollspree.ui.screens.auth.login.LoginViewModel
import com.k41s.scrollspree.ui.screens.auth.register.RegisterViewModel
import com.k41s.scrollspree.ui.screens.user.UserHomeViewModel
import com.k41s.scrollspree.util.AuthenticatedImageLoader
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }

    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }

    single { TokenManager(get(), get()) }

    single { createHttpClient(get()) }

    single { AuthApiService(get()) }
    single { CategoryApiService(get()) }
    single { CountryApiService(get()) }
    single { OrderApiService(get()) }
    single { ProductApiService(get()) }
    single { ProductImageApiService(get()) }
    single { UserApiService(get()) }

    single { AuthRepository(get(), get(), get()) }
    single { CategoryRepository(get()) }
    single { CountryRepository(get()) }
    single { OrderRepository(get()) }
    single { ProductImageRepository(get()) }
    single { ProductRepository(get()) }
    single { UserRepository(get()) }

    single<ImageLoader> {
        AuthenticatedImageLoader(
            get(),
            get()
        ).getLoader()
    }

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::UserHomeViewModel)
}

expect val platformModule: Module