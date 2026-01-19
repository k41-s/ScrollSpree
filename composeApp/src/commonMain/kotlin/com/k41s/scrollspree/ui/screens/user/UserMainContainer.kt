package com.k41s.scrollspree.ui.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.k41s.scrollspree.ui.navigation.UserNavigationActions
import com.k41s.scrollspree.ui.screens.user.mainTabs.MainTabPagerScreen
import com.k41s.scrollspree.ui.screens.user.productDetail.ProductDetailContainer
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserMainContainer() {

    val viewModel: UserMainViewModel = koinViewModel()
    val navController = rememberNavController()
    val actions = remember(navController){
        UserNavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = UserRoute.MainTabs
    ) {
        composable<UserRoute.MainTabs> {
            MainTabPagerScreen(actions) {
                viewModel.onLogoutClicked()
            }
        }

        composable<UserRoute.ProductDetail> { backStackEntry ->
            val route: UserRoute.ProductDetail = backStackEntry.toRoute()

            ProductDetailContainer(route.productId, actions.goBack, {})
        }
    }
}