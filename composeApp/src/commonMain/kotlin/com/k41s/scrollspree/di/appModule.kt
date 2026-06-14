package com.k41s.scrollspree.di

import coil3.ImageLoader
import com.k41s.scrollspree.data.local.SettingsManager
import com.k41s.scrollspree.data.local.TokenManager
import com.k41s.scrollspree.data.remote.createHttpClient
import com.k41s.scrollspree.data.remote.network.*
import com.k41s.scrollspree.data.repository.*
import com.k41s.scrollspree.domain.manager.CartManager
import com.k41s.scrollspree.ui.main.MainViewModel
import com.k41s.scrollspree.ui.screens.admin.category.AdminCategoryViewModel
import com.k41s.scrollspree.ui.screens.admin.country.AdminCountryViewModel
import com.k41s.scrollspree.ui.screens.admin.product.main.AdminProductViewModel
import com.k41s.scrollspree.ui.screens.admin.userOrder.users.AdminUsersViewModel
import com.k41s.scrollspree.ui.screens.admin.userOrder.users.orders.orderList.AdminOrderListViewModel
import com.k41s.scrollspree.ui.screens.auth.login.LoginViewModel
import com.k41s.scrollspree.ui.screens.auth.register.RegisterViewModel
import com.k41s.scrollspree.ui.screens.user.UserMainViewModel
import com.k41s.scrollspree.ui.screens.user.mainTabs.cart.CartViewModel
import com.k41s.scrollspree.ui.screens.user.mainTabs.home.UserHomeViewModel
import com.k41s.scrollspree.ui.screens.user.mainTabs.profile.UserProfileViewModel
import com.k41s.scrollspree.ui.screens.user.mainTabs.settings.UserSettingsViewModel
import com.k41s.scrollspree.ui.screens.user.myOrders.MyOrdersViewModel
import com.k41s.scrollspree.ui.screens.user.checkout.CheckoutViewModel
import com.k41s.scrollspree.ui.screens.user.myOrders.orderDetail.OrderDetailViewModel
import com.k41s.scrollspree.ui.screens.user.productDetail.ProductDetailViewModel
import com.k41s.scrollspree.util.images.AuthenticatedImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
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
    single { SettingsManager(get()) }
    single { CartManager(get(), get()) }

    single { createHttpClient(get()) }

    single { AuthApiService(get()) }
    single { CategoryApiService(get()) }
    single { CountryApiService(get()) }
    single { OrderApiService(get()) }
    single { ProductApiService(get()) }
    single { ProductImageApiService(get()) }
    single { UserApiService(get()) }
    single { CartApiService(get()) }

    single { AuthRepository(get(), get(), get()) }
    single { CategoryRepository(get()) }
    single { CountryRepository(get()) }
    single { OrderRepository(get()) }
    single { ProductImageRepository(get()) }
    single { ProductRepository(get()) }
    single { UserRepository(get()) }
    single { CartRepository(get()) }

    single<ImageLoader> {
        AuthenticatedImageLoader(
            get(),
            get()
        ).getLoader()
    }

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::UserMainViewModel)
    viewModelOf(::UserHomeViewModel)
    viewModelOf(::UserProfileViewModel)
    viewModelOf(::UserSettingsViewModel)
    viewModelOf(::CheckoutViewModel)
    viewModelOf(::MyOrdersViewModel)
    viewModelOf(::OrderDetailViewModel)
    viewModelOf(::AdminCategoryViewModel)
    viewModelOf(::AdminCountryViewModel)
    viewModelOf(::AdminProductViewModel)
    viewModelOf(::AdminUsersViewModel)
    viewModelOf(::AdminOrderListViewModel)
    viewModelOf(::CartViewModel)

    viewModel { parameters ->
        ProductDetailViewModel(
            get(),
            get(),
            get(),
            parameters.get()
        )
    }
}

expect val platformModule: Module