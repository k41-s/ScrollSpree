package com.k41s.scrollspree.ui.screens.user.mainTabs.cart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.k41s.scrollspree.ui.screens.user.mainTabs.cart.components.CartScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartContainer(
    onNavigateToCheckout: () -> Unit
) {
    val viewModel: CartViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    CartScreen(
        uiState = uiState,
        onUpdateQuantity = viewModel::updateQuantity,
        onRemoveProduct = viewModel::removeProduct,
        onCheckoutClicked = onNavigateToCheckout
    )
}