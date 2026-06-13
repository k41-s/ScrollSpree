package com.k41s.scrollspree.ui.navigation

import androidx.navigation.NavHostController
import com.k41s.scrollspree.ui.screens.user.UserRoute

class UserNavigationActions(
    private val navController: NavHostController
) {

    val navigateToDetail: (Int) -> Unit = { productId ->
        navController.navigate(UserRoute.ProductDetail(productId))
    }

    val navigateToMyOrders: () -> Unit = {
        navController.navigate(UserRoute.MyOrders)
    }

    val navigateToCheckout: (Int?) -> Unit = { productId ->
        navController.navigate(UserRoute.PlaceOrder(productId))
    }

    // Future user navigation methods go here

    val goBack: () -> Unit = {
        navController.popBackStack()
    }
}