package com.k41s.scrollspree.ui.screens.user.productDetail

sealed class ProductDetailEvent {
    object ShowLoginRequired : ProductDetailEvent()
    data class ShowSnackbar(val message: String) : ProductDetailEvent()
    data class NavigateToCheckout(val productId: Int) : ProductDetailEvent()
    data class ItemAddedToCart(val message: String) : ProductDetailEvent()
}