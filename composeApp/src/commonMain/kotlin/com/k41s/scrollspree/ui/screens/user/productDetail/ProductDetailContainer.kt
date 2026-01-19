package com.k41s.scrollspree.ui.screens.user.productDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.ui.screens.user.productDetail.screens.ProductDetailLoadingScreen
import com.k41s.scrollspree.ui.screens.user.productDetail.screens.ProductDetailScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailContainer(
    productId: Int,
    onBack: () -> Unit,
    onNavigateToCheckout: (Int) -> Unit
) {

    val viewModel: ProductDetailViewModel = koinViewModel {
        parametersOf(productId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ProductDetailUiState.Loading -> {
                ProductDetailLoadingScreen()
            }
            is ProductDetailUiState.Error -> {
                ErrorScreen(state.message) {
                    viewModel.loadProduct()
                }
            }
            is ProductDetailUiState.Success -> {
                ProductDetailScreen(
                    state.product,
                    onBack
                ) { id ->
                    onNavigateToCheckout(id)
                }
            }
        }
    }
}