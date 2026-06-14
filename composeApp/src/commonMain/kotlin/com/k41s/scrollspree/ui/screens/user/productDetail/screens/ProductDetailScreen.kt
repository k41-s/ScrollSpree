package com.k41s.scrollspree.ui.screens.user.productDetail.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.ui.components.ProductImageGallery
import com.k41s.scrollspree.ui.screens.user.productDetail.ProductDetailEvent
import com.k41s.scrollspree.util.ShareManager
import com.k41s.scrollspree.util.toCurrencyDisplay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    events: SharedFlow<ProductDetailEvent>,
    onBack: () -> Unit,
    onBuyClicked: () -> Unit,
    onAddToCartClicked: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val shareManager = remember { ShareManager() }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ProductDetailEvent.ShowLoginRequired -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Log in required to perform this action.",
                        actionLabel = "Log In",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onNavigateToLogin()
                    }
                }
                is ProductDetailEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is ProductDetailEvent.ItemAddedToCart -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "View Cart",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onNavigateToCart()
                    }
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    actionColor = colorScheme.primary
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(product.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                ProductImageGallery(product.images.map { it.url })
            }

            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = product.name,
                        style = typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = product.price.toCurrencyDisplay(),
                        style = typography.titleLarge,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onAddToCartClicked,
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                modifier = Modifier.padding(end = 6.dp).width(20.dp)
                            )
                            Text(
                                "Add to Cart",
                                style = typography.titleSmall,
                                textAlign = TextAlign.Center
                            )
                        }

                        Button(
                            onClick = onBuyClicked,
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Buy Now", style = typography.titleMedium)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    Text(
                        text = "Description",
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = product.description,
                        style = typography.bodyLarge,
                        color = colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    IconButton(
                        onClick = { shareManager.shareProduct(
                            product.name,
                            product.price.toCurrencyDisplay()
                        ) }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}