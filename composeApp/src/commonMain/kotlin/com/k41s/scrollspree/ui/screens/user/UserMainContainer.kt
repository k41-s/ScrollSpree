package com.k41s.scrollspree.ui.screens.user

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.k41s.scrollspree.ui.navigation.UserNavigationActions
import com.k41s.scrollspree.ui.screens.auth.AuthContainer
import com.k41s.scrollspree.ui.screens.user.mainTabs.MainTabPagerScreen
import com.k41s.scrollspree.ui.screens.user.myOrders.MyOrdersScreen
import com.k41s.scrollspree.ui.screens.user.checkout.CheckoutScreen
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
    val snackbarHostState = remember { SnackbarHostState() }

    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = UserRoute.MainTabs,
            modifier = Modifier.padding(padding)
        ) {
            composable<UserRoute.MainTabs> {
                MainTabPagerScreen(
                    actions = actions,
                    isAuthenticated = isAuthenticated,
                    onNavigateToAuth = { navController.navigate(UserRoute.Auth) },
                    onLogout = { viewModel.onLogoutClicked() }
                )
            }

            composable<UserRoute.ProductDetail> { backStackEntry ->
                val route: UserRoute.ProductDetail = backStackEntry.toRoute()

                ProductDetailContainer(
                    route.productId,
                    actions.goBack
                ) { id ->
                    navController.navigate(UserRoute.PlaceOrder(id))
                }
            }

            composable<UserRoute.PlaceOrder> { backStackEntry ->
                val route: UserRoute.PlaceOrder = backStackEntry.toRoute()

                CheckoutScreen(
                    route.productId,
                    actions.goBack
                ) {
                    viewModel.showMessage("Order placed successfully!")

                    navController.navigate(UserRoute.MyOrders) {
                        popUpTo(UserRoute.MainTabs) { inclusive = false }
                    }
                }
            }

            composable<UserRoute.MyOrders> {
                MyOrdersScreen(
                    onBack = actions.goBack,
                    onOrderClick = { productId ->
                        navController.navigate(UserRoute.ProductDetail(productId))
                    }
                )
            }

            composable<UserRoute.Auth> {
                AuthContainer()
            }
        }
    }
}