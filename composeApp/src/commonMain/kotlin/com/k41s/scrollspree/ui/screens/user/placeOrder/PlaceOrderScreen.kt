package com.k41s.scrollspree.ui.screens.user.placeOrder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.k41s.scrollspree.domain.model.enums.PaymentMethod
import com.k41s.scrollspree.ui.components.BasicLoadingScreen
import com.k41s.scrollspree.ui.components.ErrorScreen
import com.k41s.scrollspree.util.toCurrencyDisplay
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceOrderScreen(
    productId: Int,
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    val viewModel: PlaceOrderViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }

    val imageLoader = koinInject<ImageLoader>()

    LaunchedEffect(productId) {
        viewModel.loadInitialProduct(productId)
    }

    LaunchedEffect(state.isOrderPlaced) {
        if (state.isOrderPlaced) {
            onOrderPlaced()
            viewModel.resetState()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading && state.cartItems.isEmpty() -> BasicLoadingScreen()
                state.errorMessage != null && state.cartItems.isEmpty() -> {
                    ErrorScreen(state.errorMessage!!) { viewModel.loadInitialProduct(productId) }
                }

                state.cartItems.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Your cart is empty.", style = MaterialTheme.typography.titleMedium)
                    }
                }

                else -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

                        // Cart Items List
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.cartItems.entries.toList()) { (product, quantity) ->
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(product.name, fontWeight = FontWeight.Bold)
                                            Text(
                                                "$${product.price} each",
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }

                                        // + / - Quantity Controls
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(onClick = {
                                                viewModel.updateQuantity(
                                                    product,
                                                    -1
                                                )
                                            }) {
                                                Icon(
                                                    Icons.Default.Remove,
                                                    contentDescription = "Decrease"
                                                )
                                            }
                                            Text(
                                                "$quantity",
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )
                                            IconButton(onClick = {
                                                viewModel.updateQuantity(
                                                    product,
                                                    1
                                                )
                                            }) {
                                                Icon(
                                                    Icons.Default.Add,
                                                    contentDescription = "Increase"
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                OutlinedTextField(
                                    value = state.notes,
                                    onValueChange = viewModel::onNotesChanged,
                                    label = { Text("Order Notes") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Payment Method", fontWeight = FontWeight.Bold)
                                PaymentMethod.entries.forEach { method ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = state.selectedPaymentMethod == method,
                                            onClick = { viewModel.onPaymentMethodSelected(method) }
                                        )
                                        Text(method.name)
                                    }
                                }
                            }
                        }

                        // Footer / Total
                        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total:", style = MaterialTheme.typography.titleLarge)
                                // Displaying the calculated property from our UiState
                                Text(
                                    "$${state.cartTotal}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = viewModel::placeOrder,
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !state.isLoading
                            ) {
                                Text(if (state.isLoading) "Processing..." else "Place Order")
                            }
                            if (state.errorMessage != null) {
                                Text(
                                    text = state.errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}