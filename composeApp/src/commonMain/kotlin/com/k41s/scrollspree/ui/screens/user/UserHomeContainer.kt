package com.k41s.scrollspree.ui.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeContainer() {

    val viewModel: UserHomeViewModel = koinViewModel()
    val navController = rememberNavController()
    val actions = remember(navController){
        UserNavigationActions(navController)
    }

    NavHost(
        navController = navController,
        startDestination = UserRoute.MainTabs
    ) {
        composable<UserRoute.MainTabs> {
            MainTabPagerScreen(actions)
        }

        composable<UserRoute.ProductDetail> { backStackEntry ->
            val route: UserRoute.ProductDetail = backStackEntry.toRoute()

            // Need to make actual screen, this is a placeholder for now
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Product ${route.productId}") },
                        navigationIcon = {
                            IconButton(onClick = { actions.goBack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    "Back"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Box(
                    Modifier.padding(padding).fillMaxSize(),
                    Alignment.Center
                ) {
                    Text("Detail View for Product: ${route.productId}")
                }
            }
        }
    }
}