package com.k41s.scrollspree.ui.navigation

import androidx.navigation.NavHostController
import com.k41s.scrollspree.ui.screens.user.UserRoute

class UserNavigationActions(
    private val navController: NavHostController
) {

    val navigateToDetail: (String) -> Unit = { productId ->
        navController.navigate(UserRoute.ProductDetail(productId))
    }

    // Future navigate methods go here

    val goBack: () -> Unit = {
        navController.popBackStack()
    }
}