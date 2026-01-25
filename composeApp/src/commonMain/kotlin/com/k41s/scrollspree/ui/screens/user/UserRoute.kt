package com.k41s.scrollspree.ui.screens.user

import kotlinx.serialization.Serializable

sealed class UserRoute {

    @Serializable
    data object MainTabs : UserRoute()
    @Serializable
    data class ProductDetail(val productId: Int) : UserRoute()
    @Serializable
    data class PlaceOrder(val productId: Int) : UserRoute()
}