package com.k41s.scrollspree.ui.screens.user.mainTabs.cart.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.k41s.scrollspree.domain.model.Product
import com.k41s.scrollspree.ui.screens.user.mainTabs.cart.CartUiState
import com.k41s.scrollspree.util.images.AuthenticatedImageLoader
import com.k41s.scrollspree.util.toCurrencyDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    uiState: CartUiState,
    onUpdateQuantity: (Product, Int) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    onCheckoutClicked: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(title = { Text("My Cart") })
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total:",
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        uiState.totalAmount.toCurrencyDisplay(),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onCheckoutClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isEmpty,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Proceed to Checkout", style = typography.titleMedium)
                }
            }
        }
    ) { paddingValues ->
        if (!uiState.isLoggedIn) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Log in to place orders and view your cart.",
                    style = typography.titleMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
        else if (uiState.isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Your cart is empty",
                    style = typography.titleMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    uiState.items.entries.toList(),
                    key = { it.key.id }
                ) { (product, quantity) ->
                    CartItemRow(
                        product = product,
                        quantity = quantity,
                        onUpdateQuantity = { change -> onUpdateQuantity(product, change) },
                        onRemove = { onRemoveProduct(product) }
                    )
                }
            }
        }
    }
}