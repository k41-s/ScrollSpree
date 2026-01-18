package com.k41s.scrollspree.ui.screens.user.mainTabs.home.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.screens.user.mainTabs.home.UserHomeUiState
import com.k41s.scrollspree.ui.screens.user.mainTabs.home.UserHomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserHomeScreen(
    onProductClick: (Int) -> Unit
) {
    val viewModel: UserHomeViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is UserHomeUiState.Loading -> {
                UserHomeLoadingScreen()
            }
            is UserHomeUiState.Error -> {
                HomeErrorScreen(
                    state.message
                ) {
                    viewModel.loadProducts()
                }
            }
            is UserHomeUiState.Success -> {
                UserEntryScreen(
                    state.products,
                    onProductClick
                )
            }
        }
    }
}